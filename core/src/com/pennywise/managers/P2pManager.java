package com.pennywise.managers;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public interface P2pManager {

    public void advertise();

    public void discover();

    public void connect();

    public void send(String data);

    public void receiver(NetworkListener receiver);

    public void startClient();

    public void startServer();
}
