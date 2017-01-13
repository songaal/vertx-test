package io.gncloud.tcp;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;


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

            @Override
            public void handle(NetSocket netSocket) {
                System.out.println("Incoming connection!");

                netSocket.handler(new Handler<Buffer>() {

                    @Override
                    public void handle(Buffer buffer) {
                        System.out.println("incoming data len : "+buffer.length());

                        String data = buffer.getString(0, 4);
                        int num = buffer.getInt(4);

                        System.out.println("incoming data : "+data + " , " + num);

                        Buffer outBuffer = Buffer.buffer();
                        outBuffer.appendString("response...");

                        netSocket.write(outBuffer);
                    }
                });
            }
        });

//        server.close(new Handler<AsyncResult<Void>>() {
//            @Override
//            public void handle(AsyncResult result) {
//                if(result.succeeded()){
//                    //TCP server fully closed
//                    System.out.println("Closed.. "+result.toString());
//                }
//            }
//        });

        server.listen(10000);
        System.out.println("Listen on 10000..");
    }
}
