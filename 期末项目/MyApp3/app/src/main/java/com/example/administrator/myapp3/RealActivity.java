package com.example.administrator.myapp3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RealActivity extends AppCompatActivity {

    boolean isFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real);

        //读取数据
        SharedPreferences sp = getSharedPreferences("isFirst",MODE_PRIVATE);
        isFirst = sp.getBoolean("isFirst",true);//取值，没找到的时候默认为true
        //如果用户是第一次 进入到滑动image页面，否则进入倒计时页面
        if(isFirst){
            startActivity(new Intent(RealActivity.this,Welcome2Activity.class));
        }else{
            startActivity(new Intent(RealActivity.this,WelcomeActivity.class));
        }
        finish();
        //实例化编辑器
        SharedPreferences.Editor editor = sp.edit();
        //存入数据
        editor.putBoolean("isFirst",false);
        //提交修改
        editor.commit();
    }

}
