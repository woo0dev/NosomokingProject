package com.example.somok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somok.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarLogin extends AppCompatActivity {

    EditText idEdit;
    EditText passwordEdit;
    EditText ageEdit;
    TextView result;
    DBHelper dbHelper;
    Button selectbutton;
    Button loginButton;

    final static String dbId = "t3.db";
    final static int dbVersion = 3;

    static int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView textView = (TextView)this.findViewById(R.id.login);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);

        idEdit =  findViewById(R.id.idEdit);
        passwordEdit =  findViewById(R.id.passwordEdit);
        ageEdit =  findViewById(R.id.ageEdit);
        result =  findViewById(R.id.result);
        dbHelper = new DBHelper(this, dbId, null, dbVersion);
        selectbutton = findViewById(R.id.selectbutton);
        loginButton = findViewById(R.id.loginButton);

        final Intent intent2 = new Intent(this.getIntent());

    }

    public void mOnClick(View v) {
        SQLiteDatabase db;
        String sql;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        switch (v.getId()) {
            case R.id.savebutton: // 저장버튼
                Intent intent = new Intent(getApplicationContext(), CalendarLogin.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
                break;
            case R.id.insertbutton: // 추가버튼
                RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
                String id = idEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String age = ageEdit.getText().toString();

                db = dbHelper.getWritableDatabase();
                score = CalendarActivity.score;
                sql = String.format("INSERT INTO t3 VALUES('" + id + "','" + password + "','" + age + "','" + score + "');");
                //sql = String.format("INSERT INTO t3 VALUES('" + id + "','" + password + "','" + age + "');");

                db.execSQL(sql);
                result.append("\nInsert Success");
                break;
            case R.id.delete: //전체삭제 버튼(delete)
                db = dbHelper.getWritableDatabase();
                sql = "DELETE FROM t3;";
                db.execSQL(sql);
                result.append("\nDelete Success");
                break;

            case R.id.selectbutton: //조회버튼
                db = dbHelper.getReadableDatabase();
                sql = "SELECT * FROM t3;";
                Cursor cursor = db.rawQuery(sql, null);

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        result.append(String.format("\nID = %s, PASSWORD = %s, AGE = %s, SCORE = %s",
                                cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                        /*result.append(String.format("\nID = %s, PASSWORD = %s, AGE = %s",
                                                        cursor.getString(0), cursor.getString(1), cursor.getString(2)));*/
                    }
                }
                else {
                    result.append("\n조회결과가 없습니다.");
                }
                cursor.close();
                break;
            case R.id.loginButton:
                String loginId = idEdit.getText().toString();
                db = dbHelper.getReadableDatabase();
                sql = "SELECT * FROM t3;";
                Cursor cursor2 = db.rawQuery(sql, null);
                if (cursor2.getCount() > 0) {
                    while (cursor2.moveToNext()) {
                        if (cursor2.getString(0).equals(loginId)) {
                            result.append("\n로그인했습니다.");
                            Intent intent2 = new Intent(getApplicationContext(), CalendarActivity.class);
                            startActivity(intent2);
                        }
                    }
                } else {
                    result.append("\n로그인에 실패했습니다.");
                }
                cursor2.close();
                break;

        }
        dbHelper.close();
    }
    static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE t3 (name TEXT, password TEXT, age TEXT, score TEXT);");
            //db.execSQL("CREATE TABLE t3 (name TEXT, password TEXT, age TEXT);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS t3");
            onCreate(db);
        }

    }
}