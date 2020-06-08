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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText editText8;//用户名
    private ListView listView;//选课表
    private Button button5;//选课和退课
    private EditText editText9;//用户ID


    dbHelper dbHelper;
    String DB_Name = "mydb";
    SQLiteDatabase db;
    Cursor cursor,cursor2,cursor3;//游标
    ContentValues selCV;

    private ArrayList<Map<String,Object>> data;
    private Map<String,Object> item;
    private SimpleAdapter listAdapter;
    View view;

    private String selId;//查找用户的id
    boolean flag = true;//注册时查询是否为重复注册，重复为false，


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText8 = (EditText) findViewById(R.id.editText8);
        listView = (ListView) findViewById(R.id.listView);
        button5 = (Button) findViewById(R.id.button5);
        editText9 = (EditText) findViewById(R.id.editText9);

        Intent intent = getIntent();
        if(intent.getStringExtra("name")!=null) {
            editText8.setText( intent.getStringExtra("name"));
        }

        //创建连接，并打开数据库
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();

        //查找用户的id
        String[] whereArgs = {String.valueOf(editText8.getText().toString())};
        cursor = db.query(dbHelper.TB_Name,null,"uname = ?",whereArgs,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            selId = cursor.getString(0);
            cursor.moveToNext();
        }

        editText9.setText(selId);
        dbFindAll();//查询所有信息

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("u_id",editText9.getText().toString());//传递数据
                intent.putExtra("u_name",editText8.getText().toString());//传递数据
                intent.setClass(MainActivity.this,getLessonActivity.class);
                startActivity(intent);
            }
        });



    }
    private void dbFindAll(){
        //如果管理员删除用户已经选的课，那么在这里将找不到用户的已选课信息
        //根据用户id查找课程id
        data.clear();
        cursor3 = db.query(dbHelper.TB_Name3,null,null,null,null,null,null);
        cursor3.moveToFirst();
        int num = 1;
        while(!cursor3.isAfterLast()){
            //String uid = cursor.getString(0);
            item = new HashMap<String,Object>();
            if(cursor3.getString(1).equals(editText9.getText().toString())){
                String name  = cursor3.getString(2);//课程id
                item.put("xid",num);//为了让用户的已选课表排序好看
                //根据课程id查找课程名
                cursor2 = db.query(dbHelper.TB_Name2,null,null,null,null,null,null);
                cursor2.moveToFirst();
                while(!cursor2.isAfterLast()){
                    //String uid = cursor.getString(0);
                    if(cursor2.getString(0).equals(name)){
                        item.put("lessonname",cursor2.getString(1));
                    }else{
                        //item.put("lessonname","此课程已取消");
                    }
                    cursor2.moveToNext();
                }
                data.add(item);//如果查到的课程名不为空，那么才显示在个人的已选课表上，实际上这一条错误的数据在个人已选课表上任然存在
                num++;

            }
            cursor3.moveToNext();
        }
        showList();
    }

    public void showList(){
        listAdapter = new SimpleAdapter(this,data,
                R.layout.sqllist3,
                new String[]{"xid","lessonname"},
                new int[]{R.id.textView17,R.id.textView18});
        listView.setAdapter(listAdapter);
    }
}
