package io.gncloud.tcp;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import java.util.Date;
import java.util.Random;

/**
 * Created by swsong on 2017. 1. 13..
 */
public class VertxTcpServerVerticle extends AbstractVerticle {


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VertxTcpServerVerticle(),new DeploymentOptions().setWorker(true));
    }




    @Override
    public void start() throws Exception {
        NetServer server = vertx.createNetServer();

        server.connectHandler(new Handler<NetSocket>() {

            RandomMessageMan mm;

            @Override
            public void handle(NetSocket netSocket) {
                System.out.println("Incoming connection!");

                String welcomeMessage = "어서옵쇼. 서버에 접속성공! " + (new Date());
                netSocket.write(welcomeMessage);

                mm = new RandomMessageMan(netSocket);
                mm.start();
                netSocket.handler(new Handler<Buffer>() {

                    @Override
                    public void handle(Buffer buffer) {
                        System.out.println("incoming data len : "+buffer.length());

                        int len = buffer.length();
                        String data = buffer.getString(0, len);

                        System.out.println("incoming data : "+data);

                        Buffer outBuffer = Buffer.buffer();
                        outBuffer.appendString("response...");

                        netSocket.write(outBuffer);
                    }
                });
            }
        });

        server.listen(10000);
        System.out.println("Listen on 10000..");
    }
}

class RandomMessageMan extends Thread {
    NetSocket netSocket;
    Random r = new Random();

    public RandomMessageMan(NetSocket netSocket) {
        this.netSocket = netSocket;
    }

    public void run() {
        while(true) {
            netSocket.write("The time is " + new Date());
            try {
                Thread.sleep((r.nextInt(9000) + 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
