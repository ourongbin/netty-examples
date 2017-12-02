package io.ourongbin.netty.examples.bootstrap;

import io.netty.channel.ChannelHandler;
import io.ourongbin.netty.examples.handler.EchoServerHandler;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public final class EchoServerBootstrap {

    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            Server.newAndStart(port, new ChannelHandlersProvider() {
                @Override
                public ChannelHandler[] provide() {
                    return new ChannelHandler[]{new EchoServerHandler()};
                }
            });
        } catch (Exception e) {
            log.info("Server exception...", e);
            return;
        }

        log.info("Server shutdown...");
    }
}

