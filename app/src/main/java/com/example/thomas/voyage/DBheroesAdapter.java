package com.example.thomas.voyage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBheroesAdapter {

    DBheroesHelper helper;
    Context context1;

    public DBheroesAdapter (Context context) { helper = new DBheroesHelper(context); context1 = context;}

    public long insertData(String name, int hitpoints, String classOne, String classTwo){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBheroesHelper.NAME, name);
        contentValues.put(DBheroesHelper.HITPOINTS, hitpoints);
        contentValues.put(DBheroesHelper.CLASS_ONE, classOne);
        contentValues.put(DBheroesHelper.CLASS_TWO, classTwo);

        long id = db.insert(DBheroesHelper.TABLE_NAME, null, contentValues);
        db.close();
        return id;
        // .) wenn id = -1, dann fehler, sonst ok
        // .) kann auf VivzHelper zugreifen, da dies eine innere Klasse ist,
        //    niemand sonst kann Variablen von VivzHelper verwenden = kein falscher Zugriff
    }

    public String getAllData(){
        SQLiteDatabase db = helper.getReadableDatabase();
        //helper.get... = sql database object das database repräsentiert

        String[] columns = {DBheroesHelper.UID, DBheroesHelper.NAME, DBheroesHelper.HITPOINTS, DBheroesHelper.CLASS_ONE, DBheroesHelper.CLASS_TWO};
        //Spalten für db.query Abfrage

        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, null, null, null, null, null);
        //1. null = selectoin = Filtern nach bestimmten Kritieren
        //2. null = selectionArgs
        //letzten drei = groupBy, having, orderBy

        //noch enthält cursor subset der Tabelle, deshalb ->

        StringBuilder buffer = new StringBuilder();
        int indexUID = cursor.getColumnIndex(DBheroesHelper.UID);
        int indexName = cursor.getColumnIndex(DBheroesHelper.NAME);
        int indexHitpoints = cursor.getColumnIndex(DBheroesHelper.HITPOINTS);
        int indexClassOne = cursor.getColumnIndex(DBheroesHelper.CLASS_ONE);
        int indexClassTwo = cursor.getColumnIndex(DBheroesHelper.CLASS_TWO);

        while(cursor.moveToNext()){

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            String hitpoints = cursor.getString(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);

            buffer.append(cid + " " + name + " " + hitpoints + " " + classOne + " " + classTwo + "\n");
        }

        cursor.close();
        return buffer.toString();
    }

    public String getOneHeroRow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.UID, DBheroesHelper.NAME, DBheroesHelper.HITPOINTS, DBheroesHelper.CLASS_ONE, DBheroesHelper.CLASS_TWO};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        StringBuilder buffer = new StringBuilder();

        try {
            int indexUID = cursor.getColumnIndex(DBheroesHelper.UID);
            int indexName = cursor.getColumnIndex(DBheroesHelper.NAME);
            int indexHitpoints = cursor.getColumnIndex(DBheroesHelper.HITPOINTS);
            int indexClassOne = cursor.getColumnIndex(DBheroesHelper.CLASS_ONE);
            int indexClassTwo = cursor.getColumnIndex(DBheroesHelper.CLASS_TWO);

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            int hitpoints = cursor.getInt(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);

            buffer.append(cid + " " + name + "\n" + hitpoints + "\n" + classOne + "\n" + classTwo);
        } catch (SQLiteException e) {
            Message.message(context1, "ERROR @ getOneHeroRow with exception: " + e);
        }

        db.close();
        return buffer.toString();
    }

    public String getHeroName(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = cursor.getString(cursor.getColumnIndex(DBheroesHelper.NAME));
        cursor.close();
        db.close();

        return value;
    }

    public int updateRow(int UID, String name, int hitpoints, String primaryClass, String secondaryClass) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.NAME, name);
        cv.put(DBheroesHelper.HITPOINTS, hitpoints);
        cv.put(DBheroesHelper.CLASS_ONE, primaryClass);
        cv.put(DBheroesHelper.CLASS_TWO, secondaryClass);

        String[] whereArgs = {UID + "", context1.getString(R.string.indicator_unused_row)};

        int validation = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.UID + "=? AND " + DBheroesHelper.NAME + "=?", whereArgs);
        db.close();

        return validation;

    }

    static class DBheroesHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "heroesdatabase";
        private static final String TABLE_NAME = "HEROESTABLE";
        private static final int DATABASE_VERSION = 5;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String HITPOINTS = "Hitpoints";
        private static final String CLASS_ONE = "ClasstypeOne";
        private static final String CLASS_TWO = "ClasstypeSecondary";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + HITPOINTS + " INT, "
                + CLASS_ONE + " VARCHAR(255), "
                + CLASS_TWO + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBheroesHelper (Context context){
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
                //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
            com.example.thomas.voyage.Message.message(context, "HerosDatabse constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            com.example.thomas.voyage.Message.message(context, "HerosDatabse onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            com.example.thomas.voyage.Message.message(context, "HerosDatabse onUpgrade called");
        }
    }
}
