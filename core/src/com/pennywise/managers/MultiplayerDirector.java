package com.pennywise.managers;

import com.pennywise.multiplayer.TransmissionPackage;
import java.util.Vector;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public interface MultiplayerDirector {

    public void updatePeer(Vector move);

    public void notify_PeerDataReceived(TransmissionPackage transmissionPackage);
}
