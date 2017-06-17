package com.wordpress.persiandudeblog.dailyqoute;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private String webAddress = "hashanp.xyz";
    private Handler mainThreadHandler;

    @Override
    public void onClick(View v) {
        EditText webEdit = (EditText) findViewById(R.id.MainActivity_EditText_Website);

        switch (v.getId()) {

            case R.id.MainActivity_RadioBtn_1st:
                webEdit.setEnabled(false);
                webAddress = "hashanp.xyz";
                break;
            case R.id.MainActivity_RadioBtn_2nd:
                webEdit.setEnabled(false);
                webAddress = "cygnus-x.net";
                break;
            case R.id.MainActivity_RadioBtn_3rd:
                webEdit.setEnabled(false);
                webAddress = "djxmmx.net";
                break;
            case R.id.MainActivity_RadioBtn_4th:
                webEdit.setEnabled(true);
                break;
            case R.id.MainActivity_Button_Show:
                if (webEdit.isEnabled()) {
                    webAddress = webEdit.getText().toString();
                }

                Toast.makeText(MainActivity.this, "Trying " + webAddress, Toast.LENGTH_SHORT).show();
                RunBackgroundTask rbt = RunBackgroundTask.getInstance();
                rbt.startSocketThread(mainThreadHandler,webAddress);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
         /*if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                String msg = (String) message.obj;

                updateUI(msg);
            }
        };

        RunBackgroundTask rbt = RunBackgroundTask.getInstance();
        rbt.startSocketThread(mainThreadHandler,webAddress);


        Button btnShowQuote = (Button) findViewById(R.id.MainActivity_Button_Show);
        btnShowQuote.setOnClickListener(this);
    }

    private void updateUI(String message) {
        TextView textView = (TextView)findViewById(R.id.MainActivity_TextView_Quote);
        textView.setText(message);
    }
}
