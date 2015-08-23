package com.example.thomas.voyage.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.R;

public class DBheroesAdapter {

    DBheroesHelper helper;
    Context context1;

    public DBheroesAdapter(Context context) {
        helper = new DBheroesHelper(context);
        helper.getWritableDatabase();
        context1 = context;
    }

    public long insertData(String name, int hitpoints, String classOne, String classTwo, int costs, String image) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBheroesHelper.NAME, name);
        contentValues.put(DBheroesHelper.HITPOINTS, hitpoints);
        contentValues.put(DBheroesHelper.CLASS_ONE, classOne);
        contentValues.put(DBheroesHelper.CLASS_TWO, classTwo);
        contentValues.put(DBheroesHelper.COSTS, costs);
        contentValues.put(DBheroesHelper.IMAGE_RESOURCE, image);

        long id = db.insert(DBheroesHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;
        // .) wenn id = -1, dann fehler, sonst ok
        // .) kann auf VivzHelper zugreifen, da dies eine innere Klasse ist,
        //    niemand sonst kann Variablen von VivzHelper verwenden = kein falscher Zugriff
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBheroesHelper.TABLE_NAME);
    }

    public String getAllData(){
        SQLiteDatabase db = helper.getReadableDatabase();
        //helper.get... = sql database object das database repräsentiert

        String[] columns = {
                DBheroesHelper.UID,
                DBheroesHelper.NAME,
                DBheroesHelper.HITPOINTS,
                DBheroesHelper.CLASS_ONE,
                DBheroesHelper.CLASS_TWO,
                DBheroesHelper.COSTS,
                DBheroesHelper.IMAGE_RESOURCE};
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
        int indexCosts = cursor.getColumnIndex(DBheroesHelper.COSTS);
        int indexImage = cursor.getColumnIndex(DBheroesHelper.IMAGE_RESOURCE);

        while(cursor.moveToNext()){

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            String hitpoints = cursor.getString(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);
            int costs = cursor.getInt(indexCosts);
            String image = cursor.getString(indexImage);

            buffer.append(cid + " " + name + " " + hitpoints + " " + classOne + " " + classTwo + "\n" + costs + "\n" + image);
        }

        cursor.close();
        return buffer.toString();
    }

    public String getOneHeroRow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {
                DBheroesHelper.UID,
                DBheroesHelper.NAME,
                DBheroesHelper.HITPOINTS,
                DBheroesHelper.CLASS_ONE,
                DBheroesHelper.CLASS_TWO,
                DBheroesHelper.COSTS,
                DBheroesHelper.IMAGE_RESOURCE};

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
            int indexCosts = cursor.getColumnIndex(DBheroesHelper.COSTS);
            int indexImage = cursor.getColumnIndex(DBheroesHelper.IMAGE_RESOURCE);

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            int hitpoints = cursor.getInt(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);
            int costs = cursor.getInt(indexCosts);
            String image = cursor.getString(indexImage);

            buffer.append(cid + " " + name + "\n" + hitpoints + "\n" + classOne + "\n" + classTwo + "\n" + costs + "\n" + image);

        } catch (SQLiteException e) {

            Message.message(context1, "ERROR @ getOneHeroRow with exception: " + e);

        } catch (NullPointerException n) {

            Message.message(context1, "ERROR @ getOneHeroRow with exception: " + n);
        }

        db.close();
        return buffer.toString();
    }

    public String getHeroName(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBheroesHelper.NAME));
        } catch (NullPointerException n) {
            Message.message(context1, "ERROR @ getHeroName with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getHeroHitpoints(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.HITPOINTS};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBheroesHelper.HITPOINTS));
        } catch (NullPointerException n) {
            Message.message(context1, "ERROR @ getHeroHitpoints with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getHeroPrimaryClass(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.CLASS_ONE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBheroesHelper.CLASS_ONE));
        } catch (NullPointerException n) {
            Message.message(context1, "ERROR @ getHeroClassOne with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getHeroSecondaryClass(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.CLASS_TWO};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBheroesHelper.CLASS_TWO));
        } catch (NullPointerException n) {
            Message.message(context1, "ERROR @ getHeroClassTwo with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getHeroCosts(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.COSTS};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBheroesHelper.COSTS));
        } catch (NullPointerException n) {
            Message.message(context1, "ERROR @ getHeroHitpoints with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getHeroImgRes(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.IMAGE_RESOURCE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBheroesHelper.IMAGE_RESOURCE));
        } catch (NullPointerException n) {
            Message.message(context1, "ERROR @ getHeroImgRes with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int updateRowWithHeroData(int UID, String name, int hitpoints, String primaryClass, String secondaryClass, int costs, String image) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.NAME, name);
        cv.put(DBheroesHelper.HITPOINTS, hitpoints);
        cv.put(DBheroesHelper.CLASS_ONE, primaryClass);
        cv.put(DBheroesHelper.CLASS_TWO, secondaryClass);
        cv.put(DBheroesHelper.COSTS, costs);
        cv.put(DBheroesHelper.IMAGE_RESOURCE, image);

        String[] whereArgs = {UID + "", context1.getString(R.string.indicator_unused_row)};

        int validation = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.UID + "=? AND " + DBheroesHelper.NAME + "=?", whereArgs);
        db.close();

        return validation;
    }

    public int markOneRowAsUnused(int id){

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.NAME, context1.getResources().getString(R.string.indicator_unused_row));
        cv.put(DBheroesHelper.HITPOINTS, 0);
        cv.put(DBheroesHelper.CLASS_ONE, "");
        cv.put(DBheroesHelper.CLASS_TWO, "");
        cv.put(DBheroesHelper.COSTS, 0);
        cv.put(DBheroesHelper.IMAGE_RESOURCE, context1.getResources().getString(R.string.indicator_unused_row));

        String[] whereArgs = {Integer.toString(id)};

        int validation = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.UID + "=?", whereArgs);
        db.close();

        return validation;
    }

    static class DBheroesHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "heroesdatabase";
        private static final String TABLE_NAME = "HEROESTABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String HITPOINTS = "Hitpoints";
        private static final String CLASS_ONE = "ClasstypeOne";
        private static final String CLASS_TWO = "ClasstypeSecondary";
        private static final String COSTS = "Costs";
        private static final String IMAGE_RESOURCE = "ImageRessource";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + HITPOINTS + " INT, "
                + CLASS_ONE + " VARCHAR(255), "
                + CLASS_TWO + " VARCHAR(255), "
                + COSTS + " INT, "
                + IMAGE_RESOURCE + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBheroesHelper (Context context){
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
            Message.message(context, "HerosDatabse onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Message.message(context, "HerosDatabse onUpgrade called");
            Log.v("HEROES UPGRADE", "heroes db upgraded");
        }
    }
}
