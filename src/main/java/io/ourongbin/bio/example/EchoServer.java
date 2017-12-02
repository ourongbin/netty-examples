package io.ourongbin.bio.example;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class EchoServer {
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            final Socket finalClientSocket = serverSocket.accept();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    serveClient(finalClientSocket);
                }
            });
        }
    }

    private static void serveClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request;
            while ((request = in.readLine()) != null) {
                if ("Done".equals(request)) {
                    return;
                }
                out.println(processRequest(request));
            }
        } catch (IOException e) {
            log.error("serveClient(clientSocket: {})", clientSocket, e);
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    log.error("client.close(), client={}", clientSocket, e);
                }
            }
        }
    }

    private static String processRequest(String request) {
        return "EchoServer Response: " + request;
    }
}