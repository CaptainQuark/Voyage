package com.example.thomas.voyage.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Message;

public class DBghostScoresAdapter {

    DBghostScoresHelper helper;
    Context c;

    public DBghostScoresAdapter(Context context) {
        helper = new DBghostScoresHelper(context);
        helper.getWritableDatabase();
        c = context;
    }

    public long insertData(int rowId, int first, int second, int third) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBghostScoresHelper.VALUE, rowId);
        contentValues.put(DBghostScoresHelper.THROW_ONE, first);
        contentValues.put(DBghostScoresHelper.THROW_TWO, second);
        contentValues.put(DBghostScoresHelper.THROW_THREE, third);

        long id = db.insert(DBghostScoresHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBghostScoresHelper.TABLE_NAME);
    }








    public int getFirstThrow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostScoresHelper.THROW_ONE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBghostScoresHelper.TABLE_NAME, columns, DBghostScoresHelper.VALUE + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBghostScoresHelper.THROW_ONE));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getMulitplierOne with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getSecondThrow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostScoresHelper.THROW_TWO};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBghostScoresHelper.TABLE_NAME, columns, DBghostScoresHelper.VALUE + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBghostScoresHelper.THROW_TWO));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getMultiplierTwo with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getThirdThrow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostScoresHelper.THROW_THREE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBghostScoresHelper.TABLE_NAME, columns, DBghostScoresHelper.VALUE + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBghostScoresHelper.THROW_THREE));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getMulitplierThree with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    static class DBghostScoresHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "scoreandmultiamountdatabase";
        private static final String TABLE_NAME = "SCORE_MULTI_AMOUNT_TABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String VALUE = "ThrowId";
        private static final String THROW_ONE = "First";
        private static final String THROW_TWO = "Second";
        private static final String THROW_THREE = "Third";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VALUE + " INT, "
                + THROW_ONE + " INT, "
                + THROW_TWO + " INT, "
                + THROW_THREE + " INT);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBghostScoresHelper(Context context){
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
