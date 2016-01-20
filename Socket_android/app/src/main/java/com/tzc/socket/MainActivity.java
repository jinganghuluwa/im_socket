package com.tzc.socket;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStart;
    private TextView mMsg;
    private Handler h = new Handler(Looper.getMainLooper());
    private EditText mContent;
    private Button mStop;
    private Button mSend;
    private ServerThread mClientThread;
    private EditText mIp;
    private EditText mPort;
    public static boolean mIsRun = false;
    public static String cmsg = "";
    private EditText loginname;
    private EditText loginport;
    ServerThread clientThread;

    private SocketListener mSocketListener = new SocketListener() {
        @Override
        public void getMsg(final String msg) {
            h.post(new Runnable() {
                @Override
                public void run() {
                    mMsg.setText(mMsg.getText() + "\r\n" + msg);
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStart = (Button) findViewById(R.id.btn);
        mStart.setOnClickListener(this);
        mMsg = (TextView) findViewById(R.id.txt);
        mContent = (EditText) findViewById(R.id.edit);
        mStop = (Button) findViewById(R.id.dest);
        mStop.setOnClickListener(this);
        mSend = (Button) findViewById(R.id.send);
        mSend.setOnClickListener(this);
        mIp = (EditText) findViewById(R.id.ip);
        mPort = (EditText) findViewById(R.id.port);
        loginname = (EditText) findViewById(R.id.loginname);
        loginport = (EditText) findViewById(R.id.loginport);
    }

    private LoginListener loginListener = new LoginListener() {
        @Override
        public void login(String infomation) {
            if (infomation.equals("login")) {
                //启动本地服务器
                clientThread=new ServerThread(mSocketListener);
                clientThread.start();

            } else if (infomation.equals("logout")) {
                clientThread.stop();
            } else if (infomation.equals("already login")) {
            }
        }
    };

    private LogoutListener logoutListener = new LogoutListener() {

        @Override
        public void logout(String msg) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void start() {
        IpPort.getInstance().setIp(mIp.getText().toString());
        IpPort.getInstance().setPort(Integer.parseInt(mPort.getText().toString()));
        IpPort.getInstance().setLocalport(Integer.parseInt(loginport.getText().toString()));

        JSONObject login = new JSONObject();
        try {
            //0 登陆,1 发消息,  2登出
            login.put("action", 0);
            login.put("loginName", loginname.getText().toString());
            login.put("loginport", Integer.parseInt(loginport.getText().toString()));
        } catch (JSONException e) {
        }
        new LoginThread(login.toString(), loginListener).start();
    }

    private void stop() {
        JSONObject login = new JSONObject();
        try {
            //0 登陆,1 发消息,  2登出
            login.put("action", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new LogoutThread(login.toString(), logoutListener).start();
    }

    private void send() {
        cmsg = mContent.getText().toString();
        JSONObject login = new JSONObject();
        try {
            //0 登陆,1 发消息,  2登出
            login.put("action", 1);
            login.put("content", cmsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendThread(login.toString()).start();
        mMsg.setText(mMsg.getText() + "\r\n" + cmsg);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                start();
                break;
            case R.id.dest:
                stop();
                break;
            case R.id.send:
                send();
                break;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
