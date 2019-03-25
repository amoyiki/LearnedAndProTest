package com.amoyiki.nettytest.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author amoyiki
 * @since 2019/3/25
 */
@Component
@Slf4j
public class WebSocketServer {
    @Resource
    private WebSocketChildChannelHandler webSocketChildChannelHandler;

    public void run(int port, String url) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            webSocketChildChannelHandler.setWsUrl(url);
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(webSocketChildChannelHandler);
            Channel ch = bootstrap.bind(port).sync().channel();
            log.info("├ [服务器启动端口]: {}\n", port);
            ch.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
