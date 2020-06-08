package com.example.administrator.myapp3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class resetPasswordActivity extends AppCompatActivity {


    private EditText editText17;//输入的用户名
    private EditText editText18;//输入的年龄
    private EditText editText19;//输入的新密码
    private Button button17;//确定
    private Button button18;//退出

    private String selId;//删除时查找的id


    dbHelper dbHelper;
    String DB_Name = "mydb";
    SQLiteDatabase db;
    Cursor cursor;//游标
    ContentValues selCV;

    private ArrayList<Map<String,Object>> data;
    private Map<String,Object> item;
    private SimpleAdapter listAdapter;
    View view;

    boolean flag = true;//注册时查询是否为重复注册，重复为false，



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        editText17 = (EditText) findViewById(R.id.editText17);
        editText18 = (EditText) findViewById(R.id.editText18);
        editText19 = (EditText) findViewById(R.id.editText19);
        button17 = (Button) findViewById(R.id.button17);
        button18 = (Button) findViewById(R.id.button18);

        button18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordActivity.this.finish();
                Intent intent = new Intent();
                intent.setClass(resetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUpdate();
                editText17.setText("");
                editText18.setText("");
                editText19.setText("");
                //Toast.makeText(resetPasswordActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dbUpdate(){

        //创建连接，并打开数据库
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();
        //查找用户的id
        cursor = db.query(dbHelper.TB_Name,null,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            if(editText17.getText().toString().trim().equals(cursor.getString(1))
                    &&editText18.getText().toString().trim().equals(cursor.getString(3))){
                selId = cursor.getString(0);
            }
            cursor.moveToNext();
        }
        selCV = new ContentValues();
        if(editText17.getText().toString().equals("")){
            Toast.makeText(resetPasswordActivity.this,"用户名错误！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText18.getText().toString().equals("")){
            Toast.makeText(resetPasswordActivity.this,"年龄错误！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText19.getText().toString().equals("")){
            Toast.makeText(resetPasswordActivity.this,"新密码不为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        selCV.put("pwd",editText19.getText().toString());
        //更新条件-根据uid来修改
        String whereClause = "uid = ?";
        //更新的值
        String[] whereArgs = {String.valueOf(selId)};
        //selCV.put("uname",inputName.getText().toString().trim());
        db.update(dbHelper.TB_Name,selCV,whereClause,whereArgs);
    }
}
