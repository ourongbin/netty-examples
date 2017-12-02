package io.ourongbin.netty.examples.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.ourongbin.netty.examples.pojo.CalculateReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

@Slf4j
@ChannelHandler.Sharable
public class CalculatorServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inLine = (ByteBuf) msg;
        try {
            byte[] bytes = new byte[inLine.readableBytes()];
            inLine.readBytes(bytes);
            String expression = new String(bytes);
            log.info("expression={}", expression);

            int idx = StringUtils.indexOfAny(expression, '+', '-', '*', '/');
            if (idx < 0) {
                log.error("invalid expression");
                return;
            }

            CalculateReq req = new CalculateReq();
            try {
                req.setFirstOperand(Integer.parseInt(expression.substring(0, idx).trim()));
                req.setSecondOperand(Integer.parseInt(expression.substring(idx + 1).trim()));
                req.setOperator(expression.charAt(idx));
            } catch (Exception e) {
                log.error("invalid expression", e);
                return;
            }
            log.info("req={}", req);

            ByteBuf out = ctx.alloc().buffer();

            out.writeCharSequence("The answer is: " + String.valueOf(req.getResult()), Charset.defaultCharset());
            out.writeChar('\r');
            out.writeChar('\n');

            ctx.write(out);
            ctx.flush();
        } finally {
            inLine.release();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
    }
}

