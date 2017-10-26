package io.ourongbin.netty.examples;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscardServer {

    private void start() {
        log.info("Server started...");
    }

    public static void main(String[] args) {
        newServer().start();
        log.info("Server shutdown...");
    }

    private static DiscardServer newServer() {
        return new DiscardServer();
    }
}

