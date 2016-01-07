package com.pennywise.managers;

/**
 * Created by Joshua.Nabongo on 12/17/2015.
 */
public interface GameManager {
    public void showBannerAd();

    public void hideBannerAd();

    public void advertise();

    public void discover();

    public void connect();

    public void send(String data);

    public void receiver(NetworkListener receiver);


}
