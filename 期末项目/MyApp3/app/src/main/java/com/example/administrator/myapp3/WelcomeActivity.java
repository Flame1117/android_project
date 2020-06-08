package com.example.administrator.myapp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    private TextView timeover;
    private RelativeLayout backgroud;
    Timer timer=new Timer();
    int num=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //多线程
        timeover = (TextView)findViewById(R.id.textView10);
        backgroud = (RelativeLayout)findViewById(R.id.layout);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run(){
                        num--;
                        timeover.setText("-"+num+"秒\n跳过-");
                        if(num<=1) {
                            timer.cancel();
                            Intent intent = new Intent();
                            intent.setClass(WelcomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            WelcomeActivity.this.finish();
                        }
                    }
                });
            }
        };
        timer.schedule(task,1000,1000);

        timeover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });


/*
        //点广告其他位置转到其他页面
        backgroud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this,FlashLightActivity.class);
                startActivity(intent);
            }
        });*/


    }
}
