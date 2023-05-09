package com.example.earte.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.earte.db.BookTESST;
import com.example.earte.db.SqlTestnew;

import java.util.ArrayList;
import java.util.List;

public class SqlTer {


    private final SqlTestnew sqlTestnew;
    private static SqlTer sqlTer;

    private SqlTer(Context context) {
        sqlTestnew = SqlTestnew.getInstance(context);
    }

    public static SqlTer getInstance(Context context){
        if (sqlTer == null){
            sqlTer = new SqlTer(context);
        }
        return sqlTer;
    }

    public void addTester(List<BookTESST> bookTESST){
        Log.d("ninger",bookTESST.toString());

        for (int i = 0; i < bookTESST.size(); i++) {
            ContentValues values = new ContentValues();
            BookTESST bookTESST1 = bookTESST.get(i);
            values.put("ctime",bookTESST1.ctime);
            Log.d("ninger",bookTESST1.ctime);

            values.put("title",bookTESST1.title);
            Log.d("ninger",bookTESST1.title);

            values.put("description",bookTESST1.description);
            Log.d("ninger",bookTESST1.description);

            values.put("source",bookTESST1.source);
            Log.d("ninger",bookTESST1.source);

            values.put("picUrl",bookTESST1.picUrl);
            Log.d("ninger",bookTESST1.picUrl);

            values.put("url",bookTESST1.url);
            Log.d("ninger",bookTESST1.url);

            sqlTestnew.getWritableDatabase().insert(sqlTestnew.BOOKTEST_NAME,null,values);

        }

    }

    public void delete(){
       sqlTestnew.getWritableDatabase().delete(sqlTestnew.BOOKTEST_NAME, null, null);
    }

    public List<BookTESST> query(){
        List<BookTESST> list = new ArrayList<>();
        Cursor cursor = sqlTestnew.getReadableDatabase().query(sqlTestnew.BOOKTEST_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            BookTESST bookTESST = new BookTESST();
            bookTESST.id = cursor.getString(0);
            bookTESST.ctime = cursor.getString(1);
            bookTESST.title = cursor.getString(2);
            bookTESST.description = cursor.getString(3);
            bookTESST.source = cursor.getString(4);
            bookTESST.picUrl = cursor.getString(5);
            bookTESST.url = cursor.getString(6);
            list.add(bookTESST);
        }
        return list;
    }

}
