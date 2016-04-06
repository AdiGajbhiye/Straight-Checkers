package com.pennywise.managers;

import com.pennywise.checkers.core.engine.CBMove;
import com.pennywise.checkers.core.persistence.Player;
import com.pennywise.multiplayer.TransmissionPackage;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public interface MultiplayerDirector {

    public void updatePeer(CBMove move);

    public void sendPlayerData(Player player);

    public void notify_PeerDataReceived(TransmissionPackage transmissionPackage);
}
