package com.amoyiki.nettytest.constant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author amoyiki
 * @since 2019/3/25
 */
public final class NettyConstant {
    /**
     *  存放所有 ChannelHandleContext
     */
    public static final Map<String, ChannelHandlerContext> pushCtxMap = new ConcurrentHashMap<>();

    /**
     *  存放某一类 channel
     */
    public static final ChannelGroup aaChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static final Map<String, ChannelGroup> groups = new HashMap<>();

    static {
        groups.put("test", new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
        groups.put("other", new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
    }
}
