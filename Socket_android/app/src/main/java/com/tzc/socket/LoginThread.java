package com.tzc.socket;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by tzc on 2016/1/20.
 */
public class LoginThread extends Thread {

    private BufferedWriter mBufferedWriter;
    private String mLogMsg;
    private LoginListener mLoginListener;

    public LoginThread(String msg, LoginListener loginListener) {
        mLogMsg = msg;
        mLoginListener = loginListener;
    }

    @Override
    public void run() {
        super.run();
        try {
            Socket socket = new Socket(IpPort.getInstance().getIp(), IpPort.getInstance().getPort());
            mBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            mBufferedWriter.write(mLogMsg);
            mBufferedWriter.flush();//不flush服务器收不到
            byte[] buffer = new byte[1024 * 5];
            int len = -1;
            while ((len = socket.getInputStream().read(buffer)) != -1) {
                if(mLoginListener!=null){
                    mLoginListener.login(new String(buffer,0,len,"UTF-8"));
                }
            }
            mLoginListener.login("disconnect");

            socket.close();

        } catch (IOException e) {
            Log.e("login", e.getMessage());
        }
    }
}
