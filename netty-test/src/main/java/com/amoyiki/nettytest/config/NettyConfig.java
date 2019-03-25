package com.amoyiki.nettytest.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author amoyiki
 * @since 2019/3/25
 */
@Configuration
@Data
public class NettyConfig {

    @Value("${netty.web.socket.port}")
    private Integer port;
    @Value("${netty.web.socket.url}")
    private String url;

}
