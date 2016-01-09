
package com.company;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    static public String host = "localhost";
    static public int tcpPort = 54555, udpPort = 54777;
    protected ArrayList<Thread> threads = new ArrayList();
    protected ArrayList<EndPoint> endPoints = new ArrayList();
    protected Timer timer;
    protected boolean fail;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(RegistrationRequired.class);
    }

    static public class Login {
        public String name;
    }

    static public class RegistrationRequired {
    }

    public static class DiscoveryResponsePacket {

        public DiscoveryResponsePacket() {
            //
        }

        public int id;
        public String gameName;
        public String playerName;
    }

    public void startEndPoint (EndPoint endPoint) {
        endPoints.add(endPoint);
        Thread thread = new Thread(endPoint, endPoint.getClass().getSimpleName());
        threads.add(thread);
        thread.start();
    }

    public void stopEndPoints () {
        stopEndPoints(0);
    }

    public void stopEndPoints (int stopAfterMillis) {
        timer.schedule(new TimerTask() {
            public void run () {
                for (EndPoint endPoint : endPoints)
                    endPoint.stop();
                endPoints.clear();
            }
        }, stopAfterMillis);
    }

    public void waitForThreads (int stopAfterMillis) {
        if (stopAfterMillis > 10000) throw new IllegalArgumentException("stopAfterMillis must be < 10000");
        stopEndPoints(stopAfterMillis);
        waitForThreads();
    }

    public void waitForThreads () {
        fail = false;
        TimerTask failTask = new TimerTask() {
            public void run () {
                stopEndPoints();
                fail = true;
            }
        };
        timer.schedule(failTask, 11000);
        while (true) {
            for (Iterator iter = threads.iterator(); iter.hasNext();) {
                Thread thread = (Thread)iter.next();
                if (!thread.isAlive()) iter.remove();
            }
            if (threads.isEmpty()) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        failTask.cancel();
        if (fail) log("Test did not complete in a timely manner.");
        // Give sockets a chance to close before starting the next test.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }



    public void log(String log) {
        System.out.println(log);
    }
}
