package com.example.administrator.myapp3;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class getLessonActivity extends AppCompatActivity {

    private EditText editText10;//用户ID
    private EditText editText14;//用户姓名
    private EditText editText15;//课程ID
    private EditText editText16;//课程名
    private ListView showInfo3;
    private Button button10;//选课
    private Button button11;//退选

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
        setContentView(R.layout.activity_get_lesson);

        editText10 = (EditText) findViewById(R.id.editText10);
        editText14 = (EditText) findViewById(R.id.editText14);
        editText15 = (EditText) findViewById(R.id.editText15);
        editText16 = (EditText) findViewById(R.id.editText16);
        showInfo3 = (ListView) findViewById(R.id.showInfo3);
        button10 = (Button) findViewById(R.id.button10);
        button11 = (Button) findViewById(R.id.button11);
        Intent intent = getIntent();
        if(intent.getStringExtra("u_id")!=null) {
            editText10.setText( intent.getStringExtra("u_id"));
        }
        if(intent.getStringExtra("u_name")!=null) {
            editText14.setText( intent.getStringExtra("u_name"));
        }


        //创建连接，并打开数据库
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();


        dbFindAll();//查询所有信息

        //修改代码，实现界面显示连贯顺序
        showInfo3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> listItem = (Map<String, Object>) showInfo3.getItemAtPosition(i);
                editText16.setText((String) listItem.get("lessonname"));
                String[] whereArgs = {String.valueOf(editText16.getText().toString())};
                cursor = db.query(dbHelper.TB_Name2,null,"lessonname = ?",whereArgs,null,null,null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    selId = cursor.getString(0);
                    cursor.moveToNext();
                }
                editText15.setText(selId);//这个是总课表中的课程对应的id,那这个是不是没用了？？？

                //应该查找出个人选课表中的课程id对应的xid,而不是总课表的id

            }
        });

        //选课   讲用户ID和课程ID作为数据插入
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAdd();
                dbFindAll();
                editText15.setText("");
                editText16.setText("");
            }
        });

        //退课   将用户ID和课程ID作为条件删除
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbDelete();
                dbFindAll();
                editText15.setText("");
                editText16.setText("");
            }
        });
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
        showInfo3.setAdapter(listAdapter);
    }

    private void dbAdd(){
        //选的课程不能重复
        selCV = new ContentValues();
        cursor = db.query(dbHelper.TB_Name3,null,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            if(editText10.getText().toString().trim().equals(cursor.getString(1))
                    &&editText15.getText().toString().trim().equals(cursor.getString(2))){
                flag=false;
            }
            cursor.moveToNext();
        }//课程id为空时，无法选课
        if(editText15.getText().toString().equals("")){
            flag=false;
        }
        // TODO: 2020/6/5
        if(flag==true){
            selCV.put("userid",editText10.getText().toString().trim());
            selCV.put("lessonid",editText15.getText().toString().trim());
            long rowId = db.insert(dbHelper.TB_Name3,null,selCV);
            if(rowId ==-1){
                Toast.makeText(getLessonActivity.this,"发生未知错误",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getLessonActivity.this,"选课成功",Toast.LENGTH_SHORT).show();
            }
        }else if(editText15.getText().toString().equals("")){
            Toast.makeText(getLessonActivity.this,"请输入课程",Toast.LENGTH_SHORT).show();
            flag=true;
        } else{
            Toast.makeText(getLessonActivity.this,"课程已存在",Toast.LENGTH_SHORT).show();
            flag=true;
        }
    }


    //退课
    private void dbDelete() {
        cursor = db.query(dbHelper.TB_Name3, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String x_id = "";
            if (editText10.getText().toString().trim().equals(cursor.getString(1))
                    && editText15.getText().toString().trim().equals(cursor.getString(2))) {
                x_id = cursor.getString(0);
            }
            cursor.moveToNext();

            //删除条件-根据uid来删除
            String whereClause = "xid = ?";
            //删除的值
            String[] whereArgs = {String.valueOf(x_id)};//这个值应该是个人已选课表的xid，通过用户id和课程id找到
            db.delete(dbHelper.TB_Name3, whereClause, whereArgs);

        }
    }



}
