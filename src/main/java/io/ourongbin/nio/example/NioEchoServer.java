package io.ourongbin.nio.example;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NioEchoServer {
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        ServerSocket serverSocket = serverChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        for (;;) {
            try {
                selector.select();
            } catch (IOException e) {
                log.error("selector.select()", e);
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        log.info("Accepted connection from {}", client);
                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        if (client.read(buffer) < 0) {
                            key.cancel();
                            client.close();
                        } else {
                            buffer.flip();
                            client.write(buffer);
                            buffer.clear();
                        }
                    }
                } catch (IOException e) {
                    log.error("Error process selected keys", e);
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        log.error("Close channel");
                    }
                }
            }
        }
    }
}