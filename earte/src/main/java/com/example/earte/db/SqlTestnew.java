package com.example.earte.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlTestnew extends SQLiteOpenHelper {

    public static final String BOOKTEST_NAME = "nigers";
    private static SqlTestnew sqlTestnew;
    private static int version = 1;
    private static String name = "EARTE_DB";



    public static  SqlTestnew getInstance(Context context){
        if (sqlTestnew == null){
            sqlTestnew = new SqlTestnew(context,name,null,version);
        }
        return sqlTestnew;
    }

    private SqlTestnew(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE "+BOOKTEST_NAME+"(" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "ctime VARCHAR," +
                "title VARCHAR," +
                "description VARCHAR," +
                "source VARCHAR," +
                "picUrl VARCHAR," +
                "url VARCHAR);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
