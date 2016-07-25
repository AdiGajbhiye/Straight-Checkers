package com.pennywise.checkers.core;

import com.pennywise.multiplayer.TransmissionPackage;
import org.apache.commons.lang3.SerializationUtils;
import java.io.IOException;

/**
 * Created by Root on 7/22/2016.
 */

public class CommandBytes {

    //Message Type
    public static final byte COMMAND_UPDATE = (byte) 0xf18;
    public static final byte COMMAND_RESIGN = (byte) 0xf16;
    public static final byte COMMAND_DRAW = (byte) 0xf22;
    public static final byte COMMAND_POKE = (byte) 0xf23;
    public static final byte COMMAND_REMATCH = (byte) 0xf15;
    public static final byte COMMAND_QUIT = (byte) 0xf14;
    public static final byte COMMAND_ACCEPT = (byte) 0xf24;
    public static final byte COMMAND_DECLINE = (byte) 0xf25;


    public static byte[] commandAccept(int command) {
        byte[] cmd = new byte[3];
        cmd[0] = (byte) (cmd.length - 1);
        cmd[1] = COMMAND_ACCEPT;
        cmd[2] = (byte) command;
        return cmd;
    }

    public static byte[] commandDecline(int command) {
        byte[] cmd = new byte[3];
        cmd[0] = (byte) (cmd.length - 1);
        cmd[1] = COMMAND_DECLINE;
        cmd[2] = (byte) command;
        return cmd;
    }

    public static byte[] commandResign(int colour) {
        byte[] command = new byte[3];
        command[0] = (byte) (command.length - 1);
        command[1] = COMMAND_RESIGN;
        command[2] = (byte) colour;
        return command;
    }

    public static byte[] commandDraw(int colour) {
        byte[] command = new byte[3];
        command[0] = (byte) (command.length - 1);
        command[1] = COMMAND_DRAW;
        command[2] = (byte) colour;
        return command;
    }

    public static byte[] commandRematch(int colour) {
        byte[] command = new byte[3];
        command[0] = (byte) (command.length - 1);
        command[1] = COMMAND_REMATCH;
        command[2] = (byte) colour;
        return command;
    }

    public static byte[] commandPoke(int colour) {
        byte[] command = new byte[3];
        command[0] = (byte) (command.length - 1);
        command[1] = COMMAND_POKE;
        command[2] = (byte) colour;
        return command;
    }

    public static byte[] commandQuit(int colour) {
        byte[] command = new byte[3];
        command[0] = (byte) (command.length - 1);
        command[1] = COMMAND_QUIT;
        command[2] = (byte) colour;
        return command;
    }

    public static byte[] commandUpdate(TransmissionPackage transmissionPackage) throws IOException {
        byte[] data = SerializationUtils.serialize(transmissionPackage);
        byte[] compressed = Util.compress(data);
        int dataBytesLength = compressed.length;
        byte[] command = new byte[dataBytesLength + 2];
        command[0] = (byte) (command.length - 1);
        command[1] = COMMAND_UPDATE;
        System.arraycopy(compressed, dataBytesLength, command, 2, dataBytesLength);
        return command;
    }

}
