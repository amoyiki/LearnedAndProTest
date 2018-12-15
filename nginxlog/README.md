# go + influxdb + grafana 日志监控系统
## docker 运行 influxdb grafana
### docker 启动 influxdb
```bash
# 启动 docker
$ sudo docker run -d -p 8083:8083 -p8086:8086 --expose 8090 --expose 8099 --name indb -v /data/dockerdata/influxdb:/var/lib/influxdb docker.io/influxdb
# 创建数据库和用户
$ sudo docker exec -it indb /bin/bash
> create User nginx with password '123456'
> GRANT ALL PRIVILEGES ON monitor TO nginx
> CREATE RETENTION POLICY "monitor_retention" ON "monitor" DURATION 30d REPLICATION 1 DEFAULT
```
### docker 启动 grafana
```bash
# grafana 5.10 后创建数据卷需要传入权限, 使用 nginx 反代需要设置 server root
# 使用 link 连接其他容器，访问该容器内容直接使用 容器名称
sudo docker run -d \
  -p 3000:3000 \
  -e INFLUXDB_HOST=localhost \
  -e INFLUXDB_PORT=8086 \
  -e INFLUXDB_NAME=monitor \
  -e INFLUXDB_USER=nginx \
  -e INFLUXDB_PASS=123456 \
  -e "GF_SECURITY_ADMIN_PASSWORD=123456" \
  -e "GF_SERVER_ROOT_URL=https://www.amoyiki.com/monitor/"  \
  -v /data/dockerdata/grafana:/var/lib/grafana \
  --link indb:indb \
  --user root  \
  --name grafana \
  grafana/grafana
```

#### 配置 grafana 数据源
![](http://img.amoyiki.com/5c417ec5-7c9c-4fdb-b861-85723cf30465.png)
**PS** Access 使用 Server 即可

<!--more-->

## go 项目
### 编写 go 代码
本代码完全照搬[慕课网视频教程](https://www.imooc.com/learn/982)
```go
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
 rc chan []byte // 读取 -> 解析
 wc chan *Message // 解析 -> 写入
 reader Reader
 writer Writer
}

type Message struct {
 TimeLocal time.Time
 ByteSent int
 Path, Method, Scheme, Status string
 RequestTime float64
}

type SystemInfo struct {
 HandleLine int `json: "handleLine"` // 总日志行数
 Tps float64 `json: "tps"` // 系统吞吐量
 ReadChanLen int `json: "readChanLen"` // read channel 长度
 WriteChanLen int `json: "wirteChanLen"` // wirte channel 长度
 RunTime string `json:"runTime"` // 总运行时间
 ErrNum int `json:"errTime"` // 错误数
}

const (
 TypeHandleLine = 0
 TypeErrNum = 1
)

var TypeMonitorChan = make(chan int, 200)

type Monitor struct {
 startTime time.Time
 data SystemInfo
 tpsSlic []int
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
  Addr: infSli[0],
  Username: infSli[1],
  Password: infSli[2],
 })
 if err != nil {
  log.Fatal(err)
 }
 defer c.Close()

 // Create a new point batch
 bp, err := client.NewBatchPoints(client.BatchPointsConfig{
  Database: infSli[3],
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
   "RequestTime": v.RequestTime,
   "BytesSent": v.ByteSent,
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
 139.199.10.130 - - [12/Dec/2018:16:02:34 +0800] "POST /wp-cron.php HTTP/1.1" 200 0 "https://xxx.exmple.com/wp-cron.php?doing_wp_cron=1544601753.9868400096893310546875" "WordPress/4.9.8; https://xxx.exmple.com" "-" "0.058"
 ([\d\.]+)\s+([^ \[]+)\s+([^ \[]+)\s+\[([^\]]+)\]\s+\"([^"]+)\"\s+(\d{3})\s+(\d+)\s+\"([^"]+)\"\s+\"(.*?)\"\s+\"([^"]+)\"\s+\"([^"]+)\"
  */
 r := regexp.MustCompile(`([\d\.]+)\s+([^ \[]+)\s+([^ \[]+)\s+\[([^\]]+)\]\s+\"([^"]+)\"\s+(\d{3})\s+(\d+)\s+\"([^"]+)\"\s+\"(.*?)\"\s+\"([^"]+)\"\s+\"([^"]+)\"`)
 loc, _ := time.LoadLocation("Asia/Shanghai")

 for v := range l.rc {
  ret := r.FindStringSubmatch(string(v))
  if len(ret) != 14 {
   TypeMonitorChan <- TypeErrNum
   log.Println("FindStringSubmatch fail: ", string(v))
   continue
  }
  message := &Message{
  }
  t, err := time.ParseInLocation("02/Jan/2006:15:04:05 +0000", ret[4], loc)
  if err != nil {
   TypeMonitorChan <- TypeErrNum
   log.Println("ParseInLocation failed: ", err.Error(), ret[4])
   continue
  }
  message.TimeLocal = t
  byteSent, _ := strconv.Atoi(ret[8])
  message.ByteSent = byteSent
  reqSli := strings.Split(ret[6], " ")
  if len(reqSli) != 3 {
   TypeMonitorChan <- TypeErrNum
   log.Println("strings.Split fail", ret[6])
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
  message.Scheme = ret[5]
  message.Status = ret[7]

  requestTime, _ := strconv.ParseFloat(ret[12], 64)
  message.RequestTime = requestTime

  l.wc <- message
 }

}

func main() {
 var path, influxDsn string
 // 利用命令行参数 传入配置
 flag.StringVar(&path, "path", "./access.log", "read file path")
 flag.StringVar(&influxDsn, "influxDsn", "http://localhost:8086&root&123456&imooc&s",
  "influx data source")
 flag.Parse()

 r := &ReadFromFile{
  path: path,
 }
 w := &WriteToInfluxDB{
  influxDBsn: influxDsn,
 }
 lp := &LogProcess{
  rc: make(chan []byte),
  wc: make(chan *Message),
  reader: r,
  writer: w,
 }
 go lp.reader.Read(lp.rc)
 go lp.Process()
 go lp.writer.Write(lp.wc)

 m := &Monitor{
  startTime: time.Now(),
  data: SystemInfo{},
 }
 m.start(lp)

}

```

### 编写启动脚本
(稍后用 docker 部署时使用)
```bash
./log_process --path "/var/log/nginx/access.log" --influxDsn "http://indb:8086&root&root&monitor&s"
```
### 编译 go 项目
```bash
go build log_process.go
```
### 编写Dockerfile
```Dockerfile
FROM golang:latest
MAINTAINER amoyiki "amoyiki@gmail.com"
WORKDIR $GOPATH/src/amoyiki.com/nginxlog
ADD . $GOPATH/src/amoyiki.com/nginxlog
EXPOSE 9999
ENTRYPOINT ["sh", "./start_process.sh"]
```
### 编译并启动镜像
```bash
$ sudo docker build -t log_process . 
# 指定数据卷方便容器读取 nginx 日志， 指定关联 influxdb 容器，确保 go 项目能连接到 influxdb 容器
$ sudo docker run --name log1 -d -v /var/log/nginx:/var/log/nginx --link indb:indb log_process  
# 查看当前所有容器
$ sudo docker ps -a
```

### 结果展示
![](http://img.amoyiki.com/Snipaste_2018-12-13_21-19-13.png)


更多内容, 请访问[我的 blog](https://blog.amoyiki.com)





