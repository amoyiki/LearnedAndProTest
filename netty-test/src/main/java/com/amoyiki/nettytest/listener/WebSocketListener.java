package com.amoyiki.nettytest.listener;

import com.amoyiki.nettytest.config.NettyConfig;
import com.amoyiki.nettytest.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author amoyiki
 * @since 2019/3/25
 */
@Component
@Order(1)
public class WebSocketListener implements CommandLineRunner {
    @Autowired
    private NettyConfig nettyConfig;
    @Autowired
    private WebSocketServer webSocketServer;
    @Override
    public void run(String... args) throws Exception {
        webSocketServer.run(nettyConfig.getPort(), nettyConfig.getUrl());
    }
}
