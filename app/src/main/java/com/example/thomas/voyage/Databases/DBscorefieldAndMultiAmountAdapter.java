package com.example.thomas.voyage.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Msg;

public class DBscorefieldAndMultiAmountAdapter {

    DBscoreAndMultiAmountHelper helper;
    Context c;

    public DBscorefieldAndMultiAmountAdapter(Context context) {
        helper = new DBscoreAndMultiAmountHelper(context);
        helper.getWritableDatabase();
        c = context;
    }

    public long insertData(int value, int multiOne, int multiTwo, int multiThree) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBscoreAndMultiAmountHelper.VALUE, value);
        contentValues.put(DBscoreAndMultiAmountHelper.X1, multiOne);
        contentValues.put(DBscoreAndMultiAmountHelper.X2, multiTwo);
        contentValues.put(DBscoreAndMultiAmountHelper.X3, multiThree);

        long id = db.insert(DBscoreAndMultiAmountHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBscoreAndMultiAmountHelper.TABLE_NAME);
    }





    public int updateX1(int value, int newVal) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBscoreAndMultiAmountHelper.X1, newVal);

        String[] whereArgs = {value + ""};

        int validation = db.update(DBscoreAndMultiAmountHelper.TABLE_NAME, cv, DBscoreAndMultiAmountHelper.VALUE + "=?", whereArgs);
        db.close();

        return validation;
    }

    public int updateX2(int value, int newVal) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBscoreAndMultiAmountHelper.X2, newVal);

        String[] whereArgs = {value + ""};

        int validation = db.update(DBscoreAndMultiAmountHelper.TABLE_NAME, cv, DBscoreAndMultiAmountHelper.VALUE + "=?", whereArgs);
        db.close();

        return validation;
    }

    public int updateX3(int value, int newVal) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBscoreAndMultiAmountHelper.X3, newVal);

        String[] whereArgs = {value + ""};

        int validation = db.update(DBscoreAndMultiAmountHelper.TABLE_NAME, cv, DBscoreAndMultiAmountHelper.VALUE + "=?", whereArgs);
        db.close();

        return validation;
    }














    public int getMulitplierOne(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBscoreAndMultiAmountHelper.X1};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBscoreAndMultiAmountHelper.TABLE_NAME, columns, DBscoreAndMultiAmountHelper.VALUE + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBscoreAndMultiAmountHelper.X1));
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getMulitplierOne with exception: " + n);
        }

        db.close();

        return value;
    }

    public int getMultiplierTwo(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBscoreAndMultiAmountHelper.X2};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBscoreAndMultiAmountHelper.TABLE_NAME, columns, DBscoreAndMultiAmountHelper.VALUE + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBscoreAndMultiAmountHelper.X2));
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getMultiplierTwo with exception: " + n);
        }

        db.close();

        return value;
    }

    public int getMulitplierThree(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBscoreAndMultiAmountHelper.X3};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBscoreAndMultiAmountHelper.TABLE_NAME, columns, DBscoreAndMultiAmountHelper.VALUE + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBscoreAndMultiAmountHelper.X3));
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getMulitplierThree with exception: " + n);
        }

        db.close();

        return value;
    }

    static class DBscoreAndMultiAmountHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "scoreandmultiamountdatabase";
        private static final String TABLE_NAME = "SCORE_MULTI_AMOUNT_TABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String VALUE = "Name";
        private static final String X1 = "Hitpoints";
        private static final String X2 = "ClasstypeOne";
        private static final String X3 = "ClasstypeSecondary";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VALUE + " INT, "
                + X1 + " INT, "
                + X2 + " INT, "
                + X3 + " INT);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBscoreAndMultiAmountHelper(Context context){
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
                //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
            //com.example.thomas.voyage.ContainerClasses.Msg.msg(context, "HerosDatabse constructor called");
            //com.example.thomas.voyage.ContainerClasses.Msg.msg(context, "HerosDatabse constructor called");

        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            Msg.msg(context, "MetaHelper onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Msg.msg(context, "MetaHelper onUpgrade called");
            Log.v("HEROES UPGRADE", "heroes db upgraded");
        }
    }
}
