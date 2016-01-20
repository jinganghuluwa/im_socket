package com.tzc.socket;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by tzc on 2016/1/15.
 */
public class SendThread extends Thread {
    private String msg;
    BufferedWriter bufferedWriter;
    InputStream inputStream;

    public SendThread(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        super.run();
        try {
            Socket socket = new Socket(IpPort.getInstance().getIp(), IpPort.getInstance().getPort());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(msg);
            bufferedWriter.flush();
            inputStream = socket.getInputStream();
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[1024 * 4];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len, "UTF-8"));
            }
            inputStream.close();
            String content = sb.toString();
            Log.e("SendThread", content);
            socket.close();
        } catch (IOException e) {
            Log.e("SendThread", e.getMessage());
        }
    }
}
