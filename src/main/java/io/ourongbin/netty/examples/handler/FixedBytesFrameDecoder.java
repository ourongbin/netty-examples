package io.ourongbin.netty.examples.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FixedBytesFrameDecoder extends ChannelInboundHandlerAdapter {
    private static final int N_FIXED_BYTES = 4;
    private ByteBuf buf;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        while (m.isReadable()) {
            if (buf == null) {
                buf = ctx.alloc().buffer(N_FIXED_BYTES, N_FIXED_BYTES); // (1)
            }
            buf.writeBytes(m, Math.min(buf.writableBytes(), m.readableBytes())); // (2)

            if (buf.readableBytes() >= N_FIXED_BYTES) { // (3)
                ctx.fireChannelRead(buf);
                buf = null;
            }
        }
        m.release();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (buf != null) {
            buf.release();
            buf = null;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}