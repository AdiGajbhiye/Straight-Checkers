package com.pennywise.managers;

import com.pennywise.multiplayer.TransmissionPackage;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public interface MultiplayerDirector {

    public  void updatePeer();

    public  void notify_PeerDataReceived(TransmissionPackage transmissionPackage);
}
