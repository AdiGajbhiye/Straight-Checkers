package com.pennywise.android.bluetooth;

import android.bluetooth.BluetoothSocket;

import com.badlogic.gdx.Gdx;
import com.pennywise.checkers.core.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;

public class ConnectedThread extends Thread {

    public static final String LOG = "TRX";

    private BluetoothManager mBluetoothManager;
    private final BluetoothSocket mmSocket;
    private final DataInputStream dis;
    private final DataOutputStream dos;


    public ConnectedThread(BluetoothManager mBluetoothManager,
                           BluetoothSocket socket) {
        this.mBluetoothManager = mBluetoothManager;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();

        } catch (IOException e) {
            Gdx.app.log(LOG, "Constructor: " + e.getMessage());
        }

        dis = new DataInputStream(tmpIn);
        dos = new DataOutputStream(tmpOut);
    }

    public void run() {
        final byte[] buffer = new byte[2048]; // buffer store for the stream
        // int bytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream

                int lenght = dis.readInt(); //.read(buffer);
                Gdx.app.log(LOG, "Read BYTES: " + lenght);
                dis.readFully(buffer, 0, lenght);
                byte[] temp = Util.decompress(buffer);
                System.arraycopy(temp, 0, buffer, 0, temp.length);
                // Post a Runnable to the rendering thread that processes the
                // result
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // Let BluetoothManager's handler handle the incoming
                        // bytes
                        mBluetoothManager
                                .getHandler()
                                .obtainMessage(BluetoothManager.MESSAGE_READ, buffer).sendToTarget();
                    }
                });
            } catch (IOException e) {
                Gdx.app.log(LOG, "Read: " + e.getMessage());
                break;
            } catch (DataFormatException e) {
                e.printStackTrace();
            }

        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            byte[] compressed = Util.compress(bytes);
            dos.writeInt(compressed.length);
            dos.write(compressed);
            Gdx.app.log(LOG, "Written: " + compressed.length);
        } catch (IOException e) {
            Gdx.app.log(LOG, "Write: " + e.getMessage());
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            Gdx.app.log(LOG, "Will shutdown the connection");
            mmSocket.close();
        } catch (IOException e) {
            Gdx.app.log(LOG, "Cancel: " + e.getMessage());
        }
    }

}
