package com.tzc.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread {

    private SocketListener mSocketListener;

    public ServerThread(SocketListener socketListener) {
        mSocketListener = socketListener;
    }
    private Server server;
    private boolean work;
    public void start(){
        if(work)
        {
            return;
        }
        work=true;
        if(server==null){
            server=new Server();
        }
        server.start();
    }

    public void stop(){
        work=false;
    }

    public class Server extends Thread {


        private ServerSocket serv;

        public Server() {
            work = true;

        }


        @Override
        public void run() {

            while (serv == null && work) {
                try {
                    serv = new ServerSocket(IpPort.getInstance().getLocalport());
                    Log.e("serverPort",IpPort.getInstance().getLocalport()+"");
                } catch (Exception e) {
                    serv = null;
                    e.printStackTrace();
                }
            }

            Socket sock = null;

            while (work) {
                try {
                    sock = serv.accept();
                    new Client(sock).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                serv.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public class Client extends Thread {
        private Socket sock;

        private InputStream is;

        private OutputStream os;

        private byte[] buff;

        public Client(Socket sock) {
            this.sock = sock;
            buff = new byte[1024 * 20];
        }

        @Override
        public void run() {
            super.run();
            try {
                is = sock.getInputStream();
                os = sock.getOutputStream();
                int len = is.read(buff);
                String head = new String(buff, 0, len);
                mSocketListener.getMsg(head);
                String reslut = "ok";
                os.write(reslut.getBytes("UTF-8"));
                os.flush();
                os.close();
                is.close();
                sock.close();
            } catch (IOException e) {
                Log.e("ServerThread", e.getMessage());
            }

        }
    }
}