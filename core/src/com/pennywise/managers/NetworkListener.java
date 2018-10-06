package com.pennywise.managers;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public interface NetworkListener {

    public void onReceive(String msg);

    public void onConnect();

    public void onDisconnect();

    public void onDiscovery();

    public void discovering();
}
