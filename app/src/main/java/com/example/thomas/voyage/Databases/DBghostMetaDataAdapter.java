package com.example.thomas.voyage.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Message;

public class DBghostMetaDataAdapter {

    DBghostMetaDataHelper helper;
    Context c;

    public DBghostMetaDataAdapter(Context context) {
        helper = new DBghostMetaDataHelper(context);
        helper.getWritableDatabase();
        c = context;
    }

    public long insertData(int rowId, String name, int count, float avg, String date) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBghostMetaDataHelper.VAL, rowId);
        contentValues.put(DBghostMetaDataHelper.NAME, name);
        contentValues.put(DBghostMetaDataHelper.THROW_COUNT, count);
        contentValues.put(DBghostMetaDataHelper.THROW_AVG, avg);
        contentValues.put(DBghostMetaDataHelper.DATE, date);

        long id = db.insert(DBghostMetaDataHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBghostMetaDataHelper.TABLE_NAME);
    }








    public int getThrowCount(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostMetaDataHelper.THROW_COUNT};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBghostMetaDataHelper.TABLE_NAME, columns, DBghostMetaDataHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBghostMetaDataHelper.THROW_COUNT));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getThrowCount with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public float getPointsAveragePerThrow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostMetaDataHelper.THROW_AVG};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBghostMetaDataHelper.TABLE_NAME, columns, DBghostMetaDataHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        float value = -1;

        try {
            value = cursor.getFloat(cursor.getColumnIndex(DBghostMetaDataHelper.THROW_AVG));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getPointsAveragePerThrow with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getDate(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostMetaDataHelper.DATE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBghostMetaDataHelper.TABLE_NAME, columns, DBghostMetaDataHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "-1";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBghostMetaDataHelper.DATE));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getDate with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getName(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostMetaDataHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBghostMetaDataHelper.TABLE_NAME, columns, DBghostMetaDataHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "-1";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBghostMetaDataHelper.NAME));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getName with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    static class DBghostMetaDataHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "ghostmetadatabase";
        private static final String TABLE_NAME = "SCORE_MULTI_AMOUNT_TABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String VAL = "Val";
        private static final String NAME = "ThrowId";
        private static final String THROW_COUNT = "First";
        private static final String THROW_AVG = "Second";
        private static final String DATE = "Third";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VAL + " INT, "
                + NAME + " VARCHAR(255), "
                + THROW_COUNT + " INT, "
                + THROW_AVG + " REAL, "
                + DATE + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBghostMetaDataHelper(Context context){
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
                //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
            //com.example.thomas.voyage.ContainerClasses.Message.message(context, "HerosDatabse constructor called");
            //com.example.thomas.voyage.ContainerClasses.Message.message(context, "HerosDatabse constructor called");

        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            Message.message(context, "DBghostMetaDataHelper onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Message.message(context, "DBghostMetaDataHelper onUpgrade called");
            Log.v("HEROES UPGRADE", "heroes db upgraded");
        }
    }
}
