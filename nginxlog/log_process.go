package main

import (
	"bufio"
	"encoding/json"
	"flag"
	"fmt"
	"github.com/influxdata/influxdb/client/v2"
	"io"
	"log"
	"net/http"
	"net/url"
	"os"
	"regexp"
	"strconv"
	"strings"
	"time"
)

type Reader interface {
	Read(rc chan []byte)
}

type Writer interface {
	Write(wc chan *Message)
}

type LogProcess struct {
	rc     chan []byte   // 读取 -> 解析
	wc     chan *Message // 解析 -> 写入
	reader Reader
	writer Writer
}

type Message struct {
	TimeLocal                    time.Time
	ByteSent                     int
	Path, Method, Scheme, Status string
	RequestTime    float64
}

type SystemInfo struct {
	HandleLine   int     `json: "handleLine"`   // 总日志行数
	Tps          float64 `json: "tps"`          // 系统吞吐量
	ReadChanLen  int     `json: "readChanLen"`  // read channel 长度
	WriteChanLen int     `json: "wirteChanLen"` // wirte channel 长度
	RunTime      string  `json:"runTime"`       // 总运行时间
	ErrNum       int     `json:"errTime"`       // 错误数
}

const (
	TypeHandleLine = 0
	TypeErrNum     = 1
)

var TypeMonitorChan = make(chan int, 200)

type Monitor struct {
	startTime time.Time
	data      SystemInfo
	tpsSlic   []int
}

func (m *Monitor) start(lp *LogProcess) {

	go func() {
		for n := range TypeMonitorChan {
			switch n {
			case TypeErrNum:
				m.data.ErrNum += 1
			case TypeHandleLine:
				m.data.HandleLine += 1
			}
		}
	}()

	ticker := time.NewTicker(time.Second * 5)

	go func() {
		for {
			<-ticker.C
			m.tpsSlic = append(m.tpsSlic, m.data.HandleLine)
			if len(m.tpsSlic) > 2 {
				m.tpsSlic = m.tpsSlic[1:]
			}
		}
	}()

	http.HandleFunc("/monitor", func(writer http.ResponseWriter, request *http.Request) {
		m.data.RunTime = time.Now().Sub(m.startTime).String()
		m.data.ReadChanLen = len(lp.rc)
		m.data.WriteChanLen = len(lp.wc)
		if len(m.tpsSlic) >= 2 {

			m.data.Tps = float64(m.tpsSlic[1]-m.tpsSlic[0]) / 5
		}
		ret, _ := json.MarshalIndent(m.data, "", "\t")
		io.WriteString(writer, string(ret))
	})
	http.ListenAndServe(":9999", nil)
}

type ReadFromFile struct {
	path string // 读取文件的地址
}

func (r *ReadFromFile) Read(rc chan []byte) {
	// 打开文件
	f, err := os.Open(r.path)
	if err != nil {
		panic(fmt.Sprintln("open file error: %s", err.Error()))
	}
    // 从文件末尾逐行读取文件内容
	f.Seek(0, 2)
	rd := bufio.NewReader(f)
	for {
		line, err := rd.ReadBytes('\n')
		if err == io.EOF {
			time.Sleep(500 * time.Millisecond)
			continue
		} else if err != nil {
			panic(fmt.Sprintln("read file error: %s", err.Error()))
		}
		TypeMonitorChan <- TypeHandleLine
		rc <- line[:len(line)-1]
	}

}

type WriteToInfluxDB struct {
	influxDBsn string
}

func (w *WriteToInfluxDB) Write(wc chan *Message) {
	// 写入模块
	infSli := strings.Split(w.influxDBsn, "&")
	// Create a new HTTPClient
	c, err := client.NewHTTPClient(client.HTTPConfig{
		Addr:     infSli[0],
		Username: infSli[1],
		Password: infSli[2],
	})
	if err != nil {
		log.Fatal(err)
	}
	defer c.Close()

	// Create a new point batch
	bp, err := client.NewBatchPoints(client.BatchPointsConfig{
		Database:  infSli[3],
		Precision: infSli[4],
	})
	if err != nil {
		log.Fatal(err)
	}

	for v := range wc {
		// Create a point and add to batch
		tags := map[string]string{"Path": v.Path, "Method": v.Method, "Scheme": v.Scheme,
			"Status": v.Status}
		fields := map[string]interface{}{
			"RequestTime":  v.RequestTime,
			"BytesSent":    v.ByteSent,
		}

		pt, err := client.NewPoint("nginx_log", tags, fields, v.TimeLocal)
		if err != nil {
			log.Fatal(err)
		}
		bp.AddPoint(pt)

		// Write the batch
		if err := c.Write(bp); err != nil {
			log.Fatal(err)
		}

		// Close client resources
		if err := c.Close(); err != nil {
			log.Fatal(err)
		}
		log.Println("write influxdb success ...")
	}
}
func (l *LogProcess) Process() {
	// 解析模块
	/**
	172.0.0.12 - - [04/Mar/2018:13:49:52 +0000] http "GET /foo?query=t HTTP/1.0" 200 2133 "-" "KeepAliveClient" "-" 1.005 1.854
	([\d\.]+)\s+([^ \[]+)\s+([^ \[]+)\s+\[([^\]]+)\]\s+([a-z]+)\s+\"([^"]+)\"\s+(\d{3})\s+(\d+)\s+\"([^"]+)\"\s+\"(.*?)\"\s+\"([\d\.-]+)\"\s+([\d\.-]+)\s+([\d\.-]+)


	 */
	r := regexp.MustCompile(`([\d\.]+)\s+([^ \[]+)\s+([^ \[]+)\s+\[([^\]]+)\]\s+\"([^"]+)\"\s+(\d{3})\s+(\d+)\s+\"([^"]+)\"\s+\"(.*?)\"\s+\"([^"]+)\"\s+\"([^"]+)\"`)
	loc, _ := time.LoadLocation("Asia/Shanghai")

	for v := range l.rc {
		ret := r.FindStringSubmatch(string(v))
		if len(ret) != 12 {
            log.Println("ret len is: ", len(ret))
			TypeMonitorChan <- TypeErrNum
			log.Println("FindStringSubmatch fail: ", string(v))
			continue
		}
		message := &Message{
		}
		t, err := time.ParseInLocation("02/Jan/2006:15:04:05 +0800", ret[4], loc)
		if err != nil {
			TypeMonitorChan <- TypeErrNum
			log.Println("ParseInLocation failed: ", err.Error(), ret[4])
			continue
		}
		message.TimeLocal = t
		byteSent, _ := strconv.Atoi(ret[7])
		message.ByteSent = byteSent
		reqSli := strings.Split(ret[5], " ")
		if len(reqSli) != 3 {
			TypeMonitorChan <- TypeErrNum
			log.Println("strings.Split fail", ret[5])
			continue
		}
		message.Method = reqSli[0]
		u, err := url.Parse(reqSli[1])
		if err != nil {
			TypeMonitorChan <- TypeErrNum
			log.Println("url parse fail: ", err)
			continue
		}
		message.Path = u.Path
		message.Scheme = "https"
		message.Status = ret[6]
		requestTime, _ := strconv.ParseFloat(ret[11], 64)
		message.RequestTime = requestTime

		l.wc <- message
	}

}

func main() {
	var path, influxDsn string
	flag.StringVar(&path, "path", "./access.log", "read file path")
	flag.StringVar(&influxDsn, "influxDsn", "http://localhost:8086&nginx&123456&monitor&s",
		"influx data source")
	flag.Parse()

	r := &ReadFromFile{
		path: path,
	}
	w := &WriteToInfluxDB{
		influxDBsn: influxDsn,
	}
	lp := &LogProcess{
		rc:     make(chan []byte),
		wc:     make(chan *Message),
		reader: r,
		writer: w,
	}
	go lp.reader.Read(lp.rc)
	go lp.Process()
	go lp.writer.Write(lp.wc)

	m := &Monitor{
		startTime: time.Now(),
		data:      SystemInfo{},
	}
	m.start(lp)

}

