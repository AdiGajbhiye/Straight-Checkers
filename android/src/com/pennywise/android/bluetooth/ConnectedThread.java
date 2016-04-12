package com.pennywise.android.bluetooth;

import android.bluetooth.BluetoothSocket;

import com.badlogic.gdx.Gdx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        //mmInStream = tmpIn;
        //mmOutStream = tmpOut;
    }

    public void run() {
        final byte[] buffer = new byte[2048]; // buffer store for the stream
        // int bytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        Gdx.app.log(LOG, "LISTENING");
        try {
            // Read from the InputStream
            int length = dis.readInt();
            int read, offset = 0;
            byte[] temp = new byte[1024];
            while (length > 0) {
                read = dis.read(temp, 0, length);
                System.arraycopy(temp, 0, buffer, offset, read);
                offset += read;
                length -= read;
            }

            Gdx.app.log(LOG, "Read BYTES: " + length + "/" + temp.length);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    // Let BluetoothManager's handler handle the incoming bytes
                    mBluetoothManager
                            .getHandler()
                            .obtainMessage(BluetoothManager.MESSAGE_READ, buffer).sendToTarget();
                }
            });
        } catch (IOException e) {
            Gdx.app.log(LOG, "Read: " + e.getMessage());
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            // byte[] compressed = Util.compress(bytes);
            Gdx.app.log(LOG, "BYTES: " + bytes.length);
            dos.writeInt(bytes.length);
            dos.write(bytes);
            dos.flush();
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
