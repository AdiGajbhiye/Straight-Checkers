package com.company;

public class Main {

    static public int tcpPort = 54555, udpPort = 54777;

    public static void main(String[] args) {

        try {
            new NetServer();
            Thread.sleep(5000);
            new NetClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
