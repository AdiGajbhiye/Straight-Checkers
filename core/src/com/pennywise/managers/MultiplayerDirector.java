package com.pennywise.managers;

import com.pennywise.checkers.core.engine.CBMove;
import com.pennywise.multiplayer.TransmissionPackage;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public interface MultiplayerDirector {

    void updatePeer(CBMove move);

    void updateReceived(TransmissionPackage transmissionPackage);

    void commandReceived(byte[] cmd);

}
