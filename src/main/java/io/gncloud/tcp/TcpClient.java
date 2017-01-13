package io.gncloud.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

/**
 * Created by swsong on 2017. 1. 13..
 */
public class TcpClient {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        NetClientOptions options = new NetClientOptions().setConnectTimeout(10000);
//        options.setSsl(true);
//                .setKeyStoreOptions(
//                new JksOptions().
//                        setPath("/path/to/your/server-keystore.jks").
//                        setPassword("password-of-your-keystore")
//        );
        NetClient client = vertx.createNetClient(options);
        client.connect(4321, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Connected!");
                NetSocket socket = res.result();
            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());
            }
        });
    }
}
