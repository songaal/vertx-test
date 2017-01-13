package io.gncloud.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

/**
 * Created by swsong on 2017. 1. 13..
 */
public class TcpServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        NetServerOptions options = new NetServerOptions().setPort(4321);
        NetServer server = vertx.createNetServer(options);
        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                // Just echo back the data
                socket.write(buffer);
            });
        });
        // The default host is 0.0.0.0 which means 'listen on all available addresses'
        // and the default port is 0, which is a special value that instructs the server
        // to find a random unused local port and use that.
        server.listen();

    }
}
