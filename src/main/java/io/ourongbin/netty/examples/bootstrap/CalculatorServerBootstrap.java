package io.ourongbin.netty.examples.bootstrap;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.ourongbin.netty.examples.handler.CalculatorServerHandler;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public final class CalculatorServerBootstrap {

    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            Server.newAndStart(port, new ChannelHandlersProvider() {
                @Override
                public ChannelHandler[] provide() {
                    return new ChannelHandler[]{new LineBasedFrameDecoder(5, true, true), new CalculatorServerHandler()};
                }
            });
        } catch (Exception e) {
            log.info("Server exception...", e);
            return;
        }

        log.info("Server shutdown...");
    }
}

