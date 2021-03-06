package space.akvo.scpfoundation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by akvo on 2017/9/6.
 */

public class readDatabase {
    public ArrayList[] reDa(String s){
        AssetsDatabaseManager.initManager(getApplication.getInstance());
        AssetsDatabaseManager adm = AssetsDatabaseManager.getManager();
        SQLiteDatabase db1 = adm.getDatabase("scp.db");
        Cursor cursor = db1.rawQuery(s,null);
        ArrayList al = new ArrayList();
        ArrayList bl = new ArrayList();
        if (cursor.moveToFirst()){
            do {
                String num = cursor.getString(cursor.getColumnIndex("Num"));
                String nam = cursor.getString(cursor.getColumnIndex("Nam"));
                String lev = cursor.getString(cursor.getColumnIndex("Lev"));
                al.add(num+"!@"+nam+"!@"+lev);
                bl.add(num+"!@"+nam);
                //System.out.println(num+"\t"+nam+"\t"+lev);
            }while (cursor.moveToNext());
        }
        //db1.close();
        return new ArrayList[]{al,bl};
    }

    public ArrayList reNum(String s){
        AssetsDatabaseManager.initManager(getApplication.getInstance());
        AssetsDatabaseManager adm = AssetsDatabaseManager.getManager();
        SQLiteDatabase db1 = adm.getDatabase("scp.db");
        Cursor cursor = db1.rawQuery(s,null);
        ArrayList nums = new ArrayList();
        if (cursor.moveToFirst()){
            do {
                String num = cursor.getString(cursor.getColumnIndex("Num"));
                String nam = cursor.getString(cursor.getColumnIndex("Nam"));
                nums.add(num);
                //System.out.println(num+"\t"+nam+"\t"+lev);
            }while (cursor.moveToNext());
        }
        //db1.close();
        return nums;
    }
}
