package com.example.administrator.myapp3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Manager2Activity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputPwd;
    private EditText inputAge;
    private Button select;
    private Button insert;
    private Button delete;
    private Button update;
    private ListView showInfo;

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
        setContentView(R.layout.activity_manager2);

        inputName = (EditText) findViewById(R.id.editText11);
        inputPwd = (EditText) findViewById(R.id.editText12);
        inputAge = (EditText) findViewById(R.id.editText13);
        select = (Button) findViewById(R.id.button19);
        insert = (Button) findViewById(R.id.button20);
        delete = (Button) findViewById(R.id.button21);
        update = (Button) findViewById(R.id.button22);
        showInfo = (ListView) findViewById(R.id.showInfo);



        //创建连接，并打开数据库
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();


        dbFindAll();//查询所有信息

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbFindAll();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAdd();
                dbFindAll();
                inputName.setText("");
                inputPwd.setText("");
                inputAge.setText("");
            }
        });



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbDelete();
                dbFindAll();
                inputName.setText("");
                inputPwd.setText("");
                inputAge.setText("");
                delete.setEnabled(false);
                update.setEnabled(false);
                insert.setEnabled(true);
                inputName.setEnabled(true);//可编辑
                Toast.makeText(Manager2Activity.this,"删除成功！",Toast.LENGTH_SHORT).show();
            }
        });

        //修改代码，实现界面显示连贯顺序
        showInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                insert.setEnabled(false);
                delete.setEnabled(true);
                update.setEnabled(true);
                inputName.setEnabled(false);//不可编辑
                Map<String, Object> listItem = (Map<String, Object>) showInfo.getItemAtPosition(i);
                inputName.setText((String) listItem.get("uname"));
                inputPwd.setText((String) listItem.get("pwd"));
                inputAge.setText((String) listItem.get("age"));

                String[] whereArgs = {String.valueOf(inputName.getText().toString())};
                cursor = db.query(dbHelper.TB_Name,null,"uname = ?",whereArgs,null,null,null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    selId = cursor.getString(0);
                    cursor.moveToNext();
                }

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUpdate();
                dbFindAll();
                inputName.setText("");
                inputPwd.setText("");
                inputAge.setText("");
                delete.setEnabled(false);
                update.setEnabled(false);
                inputName.setEnabled(true);//不可编辑
                insert.setEnabled(true);
                Toast.makeText(Manager2Activity.this,"修改成功！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dbUpdate(){
        selCV = new ContentValues();
        if(inputPwd.getText().toString().equals("")){
            Toast.makeText(Manager2Activity.this,"密码不为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        selCV.put("pwd",inputPwd.getText().toString());
        selCV.put("age",inputAge.getText().toString());
        //更新条件-根据uid来修改
        String whereClause = "uid = ?";
        //更新的值
        String[] whereArgs = {String.valueOf(selId)};
        //selCV.put("uname",inputName.getText().toString().trim());
        db.update(dbHelper.TB_Name,selCV,whereClause,whereArgs);
    }

    private void dbDelete(){
        //删除条件-根据uid来删除
        String whereClause = "uid = ?";
        //删除的值
        String[] whereArgs = {String.valueOf(selId)};
        db.delete(dbHelper.TB_Name,whereClause,whereArgs);
    }

    private void dbAdd(){
        //新增的用户不能重复，用户名和密码都不能为空
        //密码的显示与不显示，添加一个imageView或者imageButton之类的
        selCV = new ContentValues();
        cursor = db.query(dbHelper.TB_Name,null,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            if(inputName.getText().toString().trim().equals(cursor.getString(1))){
                flag=false;
            }
            cursor.moveToNext();
        }
        if(inputName.getText().toString().trim().equals("admin")){
            flag=false;
        }
        if(inputName.getText().toString().trim().equals("")){
            flag=false;
        }
        if(inputPwd.getText().toString().trim().equals("")){
            flag=false;
        }
        if(flag==true){
            selCV.put("uname",inputName.getText().toString().trim());
            selCV.put("pwd",inputPwd.getText().toString().trim());
            selCV.put("age",inputAge.getText().toString().trim());
            long rowId = db.insert(dbHelper.TB_Name,null,selCV);
            if(rowId ==-1){
                Toast.makeText(Manager2Activity.this,"发生未知错误",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Manager2Activity.this,"注册成功",Toast.LENGTH_SHORT).show();
            }
        }else if(inputName.getText().toString().trim().equals("admin")){
            Toast.makeText(Manager2Activity.this,"不能使用admin作为用户名",Toast.LENGTH_SHORT).show();
            flag=true;
        }else if(inputName.getText().toString().trim().equals("")){
            Toast.makeText(Manager2Activity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            flag=true;
        }else if(inputPwd.getText().toString().trim().equals("")){
            Toast.makeText(Manager2Activity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            flag=true;
        }else{
            Toast.makeText(Manager2Activity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
            flag=true;
        }
    }

    private void dbFindAll(){
        data.clear();
        cursor = db.query(dbHelper.TB_Name,null,null,null,null,null,null);
        cursor.moveToFirst();
        int num = 1;
        while(!cursor.isAfterLast()){
            //String uid = cursor.getString(0);
            String name  = cursor.getString(1);
            String pwd = cursor.getString(2);
            String age = cursor.getString(3);
            item = new HashMap<String,Object>();
            item.put("uid",num);
            item.put("uname",name);
            item.put("pwd",pwd);
            item.put("age",age);
            data.add(item);
            num++;
            cursor.moveToNext();
        }
        showList();
    }

    public void showList(){
        listAdapter = new SimpleAdapter(this,data,
                R.layout.sqllist,
                new String[]{"uid","uname","pwd","age"},
                new int[]{R.id.textView32,R.id.textView33,R.id.textView34,R.id.textView35});
        showInfo.setAdapter(listAdapter);
    }

}
