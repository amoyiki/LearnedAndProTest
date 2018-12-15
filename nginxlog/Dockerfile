FROM golang:latest
MAINTAINER amoyiki "amoyiki@gmail.com"
WORKDIR $GOPATH/src/amoyiki.com/nginxlog
ADD . $GOPATH/src/amoyiki.com/nginxlog
EXPOSE 9999
ENTRYPOINT ["sh", "./start_process.sh"]
