package com.pennywise.checkers.core;

import com.pennywise.multiplayer.TransmissionPackage;
import org.apache.commons.lang3.SerializationUtils;
import java.io.IOException;
import java.nio.ByteBuffer;

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
        byte[] cmd = new byte[4];
        byte[] len = int2byte(cmd.length - 1);
        cmd[0] = len[0];
        cmd[1] = len[1];
        cmd[1] = COMMAND_ACCEPT;
        cmd[2] = (byte) command;
        return cmd;
    }

    public static byte[] commandDecline(int command) {
        byte[] cmd = new byte[4];
        byte[] len = int2byte(cmd.length - 1);
        cmd[0] = len[0];
        cmd[1] = len[1];
        cmd[2] = COMMAND_DECLINE;
        cmd[3] = (byte) command;
        return cmd;
    }

    public static byte[] commandResign(int colour) {
        byte[] command = new byte[4];
        byte[] len = int2byte(command.length - 1);
        command[0] = len[0];
        command[1] = len[1];
        command[2] = COMMAND_RESIGN;
        command[3] = (byte) colour;
        return command;
    }

    public static byte[] commandDraw(int colour) {
        byte[] command = new byte[4];
        byte[] len = int2byte(command.length - 1);
        command[0] = len[0];
        command[1] = len[1];
        command[2] = COMMAND_DRAW;
        command[3] = (byte) colour;
        return command;
    }

    public static byte[] commandRematch(int colour) {
        byte[] command = new byte[4];
        byte[] len = int2byte(command.length - 1);
        command[0] = len[0];
        command[1] = len[1];
        command[2] = COMMAND_REMATCH;
        command[3] = (byte) colour;
        return command;
    }

    public static byte[] commandPoke(int colour) {
        byte[] command = new byte[4];
        byte[] len = int2byte(command.length - 1);
        command[0] = len[0];
        command[1] = len[1];;
        command[2] = COMMAND_POKE;
        command[3] = (byte) colour;
        return command;
    }

    public static byte[] commandQuit(int colour) {
        byte[] command = new byte[4];
        byte[] len = int2byte(command.length - 1);
        command[0] = len[0];
        command[1] = len[1];
        command[2] = COMMAND_QUIT;
        command[3] = (byte) colour;
        return command;
    }

    public static byte[] commandUpdate(TransmissionPackage transmissionPackage) throws IOException {
        byte[] data = SerializationUtils.serialize(transmissionPackage);
        byte[] compressed = Util.compress(data);
        int dataBytesLength = compressed.length;
        byte[] command = new byte[dataBytesLength + 3];
        byte[] len = int2byte(dataBytesLength);
        command[0] = len[0];
        command[1] = len[1];
        command[2] = COMMAND_UPDATE;
        System.arraycopy(compressed, 0, command, 3, dataBytesLength);
        return command;
    }

    // copied from jpos

    public static byte[] int2byte(int value) {
        if (value < 0) {
            return new byte[]{(byte) (value >>> 24 & 0xFF), (byte) (value >>> 16 & 0xFF),
                    (byte) (value >>> 8 & 0xFF), (byte) (value & 0xFF)};
        } else if (value <= 0xFF) {
            return new byte[]{(byte) 0, (byte) (value & 0xFF)};
        } else if (value <= 0xFFFF) {
            return new byte[]{(byte) (value >>> 8 & 0xFF), (byte) (value & 0xFF)};
        } else if (value <= 0xFFFFFF) {
            return new byte[]{(byte) (value >>> 16 & 0xFF), (byte) (value >>> 8 & 0xFF),
                    (byte) (value & 0xFF)};
        } else {
            return new byte[]{(byte) (value >>> 24 & 0xFF), (byte) (value >>> 16 & 0xFF),
                    (byte) (value >>> 8 & 0xFF), (byte) (value & 0xFF)};
        }
    }

    public static int byte2int(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        for (int i = 0; i < 4 - bytes.length; i++) {
            byteBuffer.put((byte) 0);
        }
        for (int i = 0; i < bytes.length; i++) {
            byteBuffer.put(bytes[i]);
        }
        byteBuffer.position(0);
        return byteBuffer.getInt();
    }

}
