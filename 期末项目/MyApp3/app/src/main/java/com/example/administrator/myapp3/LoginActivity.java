package com.example.administrator.myapp3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    EditText username,pwd,inputCode;
    Button login,exit;
    TextView register,showCode;
    String sname,spwd;
    private Button button14;//忘记密码


    dbHelper dbHelper;
    String DB_Name = "mydb";
    SQLiteDatabase db;
    Cursor cursor;//游标

    boolean flag = false;//登录时判断是否存在该用户及密码是否正确

    private CheckBox checkBox;
    // 文件名
    private String fileName = "my_file";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.editText);
        pwd = (EditText)findViewById(R.id.editText2);
        inputCode = (EditText)findViewById(R.id.editText3);
        login = (Button)findViewById(R.id.button);
        exit = (Button)findViewById(R.id.button2);
        register = (TextView)findViewById(R.id.textView4);
        showCode = (TextView)findViewById(R.id.textView8);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        button14 = (Button) findViewById(R.id.button14);


        final SharedPreferences sp = getSharedPreferences(fileName,MODE_PRIVATE);
        sname = sp.getString("sname","");
        if(sp!=null &&!sname.equals("")){
            username.setText(sp.getString("sname",""));
            pwd.setText(sp.getString("spwd",""));
            checkBox.setChecked(true);
        }

        //显示验证码
        showCode.setText(yzm());

        //点击验证码调用方法刷新验证码
        showCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCode.setText(yzm());
            }
        });

        //创建连接，并打开数据库
        dbHelper = new dbHelper(this,DB_Name,null,1);
        db = dbHelper.getWritableDatabase();

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                cursor = db.query(dbHelper.TB_Name,null,null,null,null,null,null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    if(username.getText().toString().trim().equals(cursor.getString(1))
                            &&pwd.getText().toString().trim().equals(cursor.getString(2))){
                        flag = true;
                    }
                    cursor.moveToNext();
                }

                if(username.getText().toString().trim().equals("admin")
                        &&pwd.getText().toString().trim().equals("123")
                        &&inputCode.getText().toString().equals(showCode.getText())){
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,ManagerActivity.class);
                    startActivity(intent);
                }else if(flag==true&&inputCode.getText().toString().equals(showCode.getText())){
                    Toast.makeText(LoginActivity.this,"欢迎回来，"+username.getText().toString(),Toast.LENGTH_SHORT).show();
                    flag=false;
                    Intent intent = new Intent();
                    intent.putExtra("name",username.getText().toString());//传递数据
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    if(checkBox.isChecked()==true){
                        sname = username.getText().toString();
                        spwd =pwd.getText().toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sname",sname);
                        editor.putString("spwd",spwd);
                        editor.commit();
                    }else{
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                    }
                }else if(flag==false&&inputCode.getText().toString().equals(showCode.getText())){
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }else if(!inputCode.getText().toString().equals(showCode.getText())){
                    Toast.makeText(LoginActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                    flag=false;//避免了更改验证码后，密码由正确改成错误时而无法判断的bug
                }
            }
        });

        //跳转到注册界面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,resetPasswordActivity.class);
                startActivity(intent);
            }
        });



    }

    //产生随机4位数的验证码
    public String yzm(){
        String str ="0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
        String str2[] = str.split(",");
        Random rand = new Random();
        int index = 0;
        String randStr = "";
        for(int i=0;i<4;i++){
            index = rand.nextInt(str2.length-1);
            randStr += str2[index];
        }
        return randStr;
    }

    long firstTime = 0;
    //返回键的重写
    public boolean onKeyDown(int keyCode,KeyEvent event){
        long secondTime = System.currentTimeMillis();
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(secondTime-firstTime<2000){
                System.exit(0);
            }else{
                Toast.makeText(LoginActivity.this,"再按一次退出!",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
