package com.tzc.socket;

/**
 * Created by tzc on 2016/1/15.
 */
public final class IpPort {

    public String getIp() {
        synchronized (ipLock) {
            return ip;
        }

    }

    public void setIp(String ip) {

        synchronized (ipLock) {
            this.ip = ip;
        }
    }

    public int getPort() {

        synchronized (portLock) {
            return port;
        }
    }

    public void setPort(int port) {

        synchronized (portLock) {
            this.port = port;
        }
    }

    private String ip;
    private int port;

    public int getLocalport() {
        return localport;
    }

    public void setLocalport(int localport) {
        this.localport = localport;
    }

    private int localport;
    private IpPort() {

    }

    private static final Object ipLock = new Object();
    private static final Object portLock = new Object();

    private static IpPort instance;
    private static final Object lock = new Object();

    public static IpPort getInstance() {
        synchronized (lock) {
            if (instance == null) {
                synchronized (lock) {
                    instance = new IpPort();
                }

            }
        }
        return instance;
    }

}
