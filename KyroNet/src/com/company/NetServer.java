
package com.company;

import com.esotericsoftware.kryonet.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class NetServer extends Network {
    Server server;


    public NetServer() throws IOException {

            startServer();

    }


    protected void startServer() throws IOException {
        // This server exists solely to reply to Client#discoverHost.
        // It wouldn't be needed if the real server was using UDP.
        final Server broadcastServer = new Server();

        broadcastServer.getKryo().register(DiscoveryResponsePacket.class);
        broadcastServer.setDiscoveryHandler(serverDiscoveryHandler);

        startEndPoint(broadcastServer);
        broadcastServer.bind(0, udpPort);

        final Server server = new Server();
        startEndPoint(server);
        server.bind(54555);
        server.addListener(new Listener() {
            public void disconnected (Connection connection) {
                broadcastServer.stop();
                server.stop();
            }
        });

        log("Server started and listening....");
    }

    ServerDiscoveryHandler serverDiscoveryHandler = new ServerDiscoveryHandler() {
        @Override
        public boolean onDiscoverHost (DatagramChannel datagramChannel, InetSocketAddress fromAddress,
                                       Serialization serialization) throws IOException {

            DiscoveryResponsePacket packet = new DiscoveryResponsePacket();
            packet.id = 42;
            packet.gameName = "gameName";
            packet.playerName = "playerName";

            ByteBuffer buffer = ByteBuffer.allocate(256);
            serialization.write(null, buffer, packet);
            buffer.flip();

            datagramChannel.send(buffer, fromAddress);

            return true;
        }
    };
}
