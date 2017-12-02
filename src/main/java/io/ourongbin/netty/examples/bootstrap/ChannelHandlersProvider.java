package io.ourongbin.netty.examples.bootstrap;

import io.netty.channel.ChannelHandler;

public interface ChannelHandlersProvider {
    ChannelHandler[] provide();
}
