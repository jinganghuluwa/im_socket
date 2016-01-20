package com.tzc.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by tzc on 2016/1/20.
 */
public class LogoutThread extends Thread {
    private BufferedWriter mBufferedWriter;
    private String mLogMsg;
    private LogoutListener mLoginListener;

    public LogoutThread(String msg, LogoutListener loginListener) {
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
            mBufferedWriter.flush();
            byte[] buffer = new byte[1024];
            int len=-1;
            len=socket.getInputStream().read(buffer);
            if(len!=-1&&mLoginListener!=null){
                mLoginListener.logout(new String(buffer,0,len,"UTF-8"));
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
