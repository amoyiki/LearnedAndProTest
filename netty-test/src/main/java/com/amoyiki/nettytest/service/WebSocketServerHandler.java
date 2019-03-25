package com.amoyiki.nettytest.service;

import com.amoyiki.nettytest.constant.NettyConstant;
import com.amoyiki.nettytest.util.TokenUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author amoyiki
 * @since 2019/3/25
 */
@Component
@Sharable  // 线程安全， 每次 initChannel 使用不同的实例
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private String wsUrl;
    @Autowired
    private TokenUtil tokenUtil;

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));

    /**
     * 用于打开关闭握手
     */
    private WebSocketServerHandshaker socketServerHandShaker;

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    /**
     * 捕获异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.error("├ [服务端捕捉异常]: {}", cause);
        // 关闭channel
        ctx.close();
    }

    /**
     * 客户端主动与服务端连接, 调用此方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("├ [与客户端[{}]建立连接]: {}", ctx.channel().remoteAddress(), ctx.channel().id());
    }

    /**
     * 与客户端断开连接时调用方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("├ 与客户端[{}]断开连接", channel.remoteAddress());
        for (ChannelGroup channelGroup : NettyConstant.groups.values()) {
            channelGroup.remove(ctx.channel());
        }
    }

    /**
     *  channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.debug("├ [handlerAdded]: -----");
    }

    /**
     * 接受客户端发送的消息
     *
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // 传统方式 HTTP 方式接入, 第一次需要建立 HTTP 握手
        if (o instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) o;
            log.debug("├ [握手]: {}", fullHttpRequest.uri());
            // 握手
            handlerHttpRequest(channelHandlerContext, fullHttpRequest);
            // 连接成功后返回给客户端
            channelHandlerContext.channel().write(new TextWebSocketFrame("连接成功！"));
        } else if (o instanceof WebSocketFrame) {
            WebSocketFrame webSocketFrame = (WebSocketFrame) o;
            if ("kpi".equals(channelHandlerContext.channel().attr(AttributeKey.valueOf("type")).get())) {
                kpiHandlerWebSocketFrame(channelHandlerContext, webSocketFrame);
            } else {
                otherHandlerWebSocketFrame(channelHandlerContext, webSocketFrame);
            }

        }else{
            log.debug("├ [断点处4] =======================");
        }
    }



    /**
     * 第一次握手
     *
     * @param channelHandlerContext
     * @param req
     */
    private void handlerHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(channelHandlerContext, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        HttpMethod method = req.method();
        String urlBase = req.uri();
        // 截取uri数据 获得用户
        String uri = null;
        int i = urlBase.lastIndexOf("/");
        String path = urlBase.substring(i);
        int j = path.lastIndexOf("?");
        if (j > 0){
            uri = path.substring(0, j);
        }else{
            uri = path;
        }

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(urlBase);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        validParamters(channelHandlerContext, req, parameters);

        if (method == HttpMethod.GET && "/kpi".equals(uri)) {
            ChannelGroup cg = NettyConstant.groups.get("kpi");
            cg.add(channelHandlerContext.channel());
            channelHandlerContext.channel().attr(AttributeKey.valueOf("type")).set("kpi");
        }else if (method == HttpMethod.GET && "/other".equals(uri)) {
            ChannelGroup cg = NettyConstant.groups.get("other");
            cg.add(channelHandlerContext.channel());
            channelHandlerContext.channel().attr(AttributeKey.valueOf("type")).set("other");
        }
        // ws://localhost:9999/ws/{uri}
        // 构建握手响应返回
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(this.wsUrl, null, false);

        Channel connectChannel = channelHandlerContext.channel();
        socketServerHandShaker = wsFactory.newHandshaker(req);
        if (socketServerHandShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(connectChannel);
        }else {
            // 握手响应
            socketServerHandShaker.handshake(connectChannel, req);
        }

    }

    private void validParamters(ChannelHandlerContext channelHandlerContext, FullHttpRequest req, Map<String, List<String>> parameters) {
        log.debug("{}",parameters);
        // 验证是否有带token
        boolean notToken = CollectionUtils.isEmpty(parameters.get("token"));
        if (!notToken){
            log.debug("{}",parameters.get("token").get(0));
            boolean token = tokenUtil.checkToken(parameters.get("token").get(0));
            if(!token){
                // 关闭链接
                sendHttpResponse(channelHandlerContext, req,
                        new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            }
        }else {
            // 关闭链接
            sendHttpResponse(channelHandlerContext, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
    }

    /**
     *  websocket 信息处理
     * @param channelHandlerContext
     * @param frame
     */
    private void kpiHandlerWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame) {
        Channel channel = channelHandlerContext.channel();
        // 判断是否是关闭连接请求
        if (frame instanceof CloseWebSocketFrame) {
            log.debug("├ 关闭与客户端[{}]链接", channel.remoteAddress());
            socketServerHandShaker.close(channel, ((CloseWebSocketFrame) frame).retain());
            return;
        }

        // 判断是否是 ping 消息
        if (frame instanceof PingWebSocketFrame) {
            log.debug("├ [Ping消息]");
            channel.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 文本消息
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            log.debug("├ [接收到客户端的消息]: {}", text);
            ChannelGroup cg = NettyConstant.groups.get("kpi");
            cg.writeAndFlush(new TextWebSocketFrame(text));
        }
    }

    private void otherHandlerWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame) {
        Channel channel = channelHandlerContext.channel();
        // 判断是否是关闭连接请求
        if (frame instanceof CloseWebSocketFrame) {
            log.debug("├ 关闭与客户端[{}]链接", channel.remoteAddress());
            socketServerHandShaker.close(channel, ((CloseWebSocketFrame) frame).retain());
            return;
        }

        // 判断是否是 ping 消息
        if (frame instanceof PingWebSocketFrame) {
            log.debug("├ [Ping消息]");
            channel.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 文本消息
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            log.debug("├ [接收到客户端的消息]: {}", text);
            ChannelGroup cg = NettyConstant.groups.get("other");
            cg.writeAndFlush(new TextWebSocketFrame(text));
        }

    }
    /**
     *  心跳机制
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 服务端发送消息
                ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
