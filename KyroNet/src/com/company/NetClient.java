package com.company;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.ClientDiscoveryHandler;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by joshua.nabongo on 11/25/2015.
 */
public class NetClient extends Network{
    Client client;
    String name;
    final AtomicInteger reconnetCount = new AtomicInteger();

    public NetClient() throws IOException {


            startClient();

    }

    ClientDiscoveryHandler clientDiscoveryHandler = new ClientDiscoveryHandler() {
        private Input input = null;

        @Override
        public DatagramPacket onRequestNewDatagramPacket () {
            byte[] buffer = new byte[1024];
            input = new Input(buffer);
            return new DatagramPacket(buffer, buffer.length);
        }

        @Override
        public void onDiscoveredHost (DatagramPacket datagramPacket, Kryo kryo) {
            if (input != null) {
                DiscoveryResponsePacket packet;
                packet = (DiscoveryResponsePacket)kryo.readClassAndObject(input);
                log("packet.id = " + packet.id);
                log("packet.gameName = " + packet.gameName);
                log("packet.playerName = " + packet.playerName);
                log("datagramPacket.getAddress() = " + datagramPacket.getAddress());
                log( "datagramPacket.getPort() = " + datagramPacket.getPort());
            }
        }

        @Override
        public void onFinally () {
            if (input != null) {
                input.close();
            }
        }
    };


    protected  void startClient() throws IOException {

        final Client client = new Client();

        client.getKryo().register(DiscoveryResponsePacket.class);
        client.setDiscoveryHandler(clientDiscoveryHandler);

        log("Discovering host....");
        InetAddress host = client.discoverHost(udpPort, 2000);

        if (host == null) {
            client.close();
            return;
        }

        startEndPoint(client);

        log("Startint client....");

        client.addListener(new Listener() {
            public void disconnected(Connection connection) {
                if (reconnetCount.getAndIncrement() == 2) {
                    stopEndPoints();
                    return;
                }
                new Thread() {
                    public void run() {
                        try {
                            System.out.println("Reconnecting: " + reconnetCount.get());
                            client.reconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        client.connect(2000, host, tcpPort);
        client.stop();
    }

}
