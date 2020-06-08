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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager3Activity extends AppCompatActivity {


    private EditText editText4;//输入框输入的课程名称
    private Button button6;//刷新课程
    private Button button7;//添加课程
    private Button button8;//删除课程
    private Button button9;//修改课程
    private ListView showInfo2;//

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
        setContentView(R.layout.activity_manager3);

        editText4 = (EditText) findViewById(R.id.editText4);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        showInfo2 = (ListView) findViewById(R.id.showInfo2);

        //创建连接，并打开数据库
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();

        dbFindAll();//查询所有信息


        //刷新课程
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbFindAll();
            }
        });

        //添加课程
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAdd();
                dbFindAll();
                editText4.setText("");
            }
        });

        //删除课程
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbDelete();
                dbFindAll();
                editText4.setText("");
                button8.setEnabled(false);
                button9.setEnabled(false);
                button6.setEnabled(true);
                editText4.setEnabled(true);//可编辑
                //Toast.makeText(Manager3Activity.this,"删除成功！",Toast.LENGTH_SHORT).show();
            }
        });


        //修改课程
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUpdate();
                dbFindAll();
                editText4.setText("");
                button8.setEnabled(false);
                button9.setEnabled(false);
                button6.setEnabled(true);
                //Toast.makeText(Manager3Activity.this,"修改成功！",Toast.LENGTH_SHORT).show();
            }
        });

        //修改代码，实现界面显示连贯顺序
        showInfo2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                button6.setEnabled(false);
                button8.setEnabled(true);
                button9.setEnabled(true);
                editText4.setEnabled(true);//可编辑
                Map<String, Object> listItem = (Map<String, Object>) showInfo2.getItemAtPosition(i);
                editText4.setText((String) listItem.get("lessonname"));
                String[] whereArgs = {String.valueOf(editText4.getText().toString())};
                cursor = db.query(dbHelper.TB_Name2,null,"lessonname = ?",whereArgs,null,null,null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    selId = cursor.getString(0);
                    cursor.moveToNext();
                }

            }
        });


    }

    private void dbUpdate(){
        selCV = new ContentValues();
        if(editText4.getText().toString().equals("")){
            Toast.makeText(Manager3Activity.this,"课程名不为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        selCV.put("lessonname",editText4.getText().toString());
        //更新条件-根据uid来修改
        String whereClause = "lid = ?";
        //更新的值
        String[] whereArgs = {String.valueOf(selId)};
        //selCV.put("uname",inputName.getText().toString().trim());
        db.update(dbHelper.TB_Name2,selCV,whereClause,whereArgs);
    }

    private void dbDelete(){
        //删除条件-根据uid来删除
        String whereClause = "lid = ?";
        //删除的值
        String[] whereArgs = {String.valueOf(selId)};
        db.delete(dbHelper.TB_Name2,whereClause,whereArgs);
    }

    private void dbAdd(){
        //新增的课程不能重复
        selCV = new ContentValues();
        cursor = db.query(dbHelper.TB_Name2,null,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            if(editText4.getText().toString().trim().equals(cursor.getString(1))){
                flag=false;
            }
            cursor.moveToNext();
        }

        if(editText4.getText().toString().trim().equals("")){
            flag=false;
        }

        if(flag==true){
            selCV.put("lessonname",editText4.getText().toString().trim());
            long rowId = db.insert(dbHelper.TB_Name2,null,selCV);
            if(rowId ==-1){
                Toast.makeText(Manager3Activity.this,"发生未知错误",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Manager3Activity.this,"添加课程成功",Toast.LENGTH_SHORT).show();
            }
        }else if(editText4.getText().toString().trim().equals("")){
            Toast.makeText(Manager3Activity.this,"课程名不能为空",Toast.LENGTH_SHORT).show();
            flag=true;
        }else{
            Toast.makeText(Manager3Activity.this,"课程已存在",Toast.LENGTH_SHORT).show();
            flag=true;
        }
    }

    private void dbFindAll(){
        data.clear();
        cursor = db.query(dbHelper.TB_Name2,null,null,null,null,null,null);
        cursor.moveToFirst();
        int num = 1;
        while(!cursor.isAfterLast()){
            //String uid = cursor.getString(0);
            String name  = cursor.getString(1);
            item = new HashMap<String,Object>();
            item.put("lid",num);
            item.put("lessonname",name);
            data.add(item);
            num++;
            cursor.moveToNext();
        }
        showList();
    }

    public void showList(){
        listAdapter = new SimpleAdapter(this,data,
                R.layout.sqllist2,
                new String[]{"lid","lessonname"},
                new int[]{R.id.textView12,R.id.textView13});
        showInfo2.setAdapter(listAdapter);
    }

}
