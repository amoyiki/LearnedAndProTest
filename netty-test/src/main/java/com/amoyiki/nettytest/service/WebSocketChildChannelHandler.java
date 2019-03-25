package com.amoyiki.nettytest.service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author amoyiki
 * @since 2019/3/25
 */
@Component
@Slf4j
public class WebSocketChildChannelHandler extends ChannelInitializer<SocketChannel> {
    @Resource
    private WebSocketServerHandler webSocketServerHandler;
    private String wsUrl;

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    /**
     * 初始化配置
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 请求/应答 的 编码/解码 为HTTP消息
        log.debug("├ [断点处] 每个链接都会初始化一次");
        socketChannel.pipeline().addLast("http-codec", new HttpServerCodec());
        // HTTP消息的多个部分合成一条完整的HTTP消息
        socketChannel.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        // 向客户端发送HTML5文件
        socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        // 心跳
        socketChannel.pipeline().addLast(
                new IdleStateHandler(240,0,0, TimeUnit.SECONDS));
        // 添加日志
//        socketChannel.pipeline().addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        // 服务端 handler
        webSocketServerHandler.setWsUrl(this.wsUrl);
        socketChannel.pipeline().addLast("handler", webSocketServerHandler);

    }
}
