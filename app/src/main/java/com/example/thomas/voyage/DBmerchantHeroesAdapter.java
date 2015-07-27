package com.example.thomas.voyage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBmerchantHeroesAdapter {

    DBmerchantHeroesHelper helper;
    Context context1;

    public DBmerchantHeroesAdapter(Context context) {
        helper = new DBmerchantHeroesHelper(context);
        context1 = context;
    }

    public long insertData(String name, int hitpoints, String classOne, String classTwo, int costs) {

        Log.i("INSERT", "insert Data called");

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBmerchantHeroesHelper.NAME, name);
        contentValues.put(DBmerchantHeroesHelper.HITPOINTS, hitpoints);
        contentValues.put(DBmerchantHeroesHelper.CLASS_ONE, classOne);
        contentValues.put(DBmerchantHeroesHelper.CLASS_TWO, classTwo);
        contentValues.put(DBmerchantHeroesHelper.COSTS, costs);

        long id = db.insert(DBmerchantHeroesHelper.TABLE_NAME, null, contentValues);

        Log.i("INSERT", "long id = " + id);

        db.close();
        return id;
        // .) wenn id = -1, dann fehler, sonst ok
        // .) kann auf VivzHelper zugreifen, da dies eine innere Klasse ist,
        //    niemand sonst kann Variablen von VivzHelper verwenden = kein falscher Zugriff
    }

    public String getAllData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        //helper.get... = sql database object das database repräsentiert

        String[] columns = {DBmerchantHeroesHelper.UID, DBmerchantHeroesHelper.NAME, DBmerchantHeroesHelper.HITPOINTS, DBmerchantHeroesHelper.CLASS_ONE, DBmerchantHeroesHelper.CLASS_TWO, DBmerchantHeroesHelper.COSTS};
        //Spalten für db.query Abfrage

        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, null, null, null, null, null);
        //1. null = selectoin = Filtern nach bestimmten Kritieren
        //2. null = selectionArgs
        //letzten drei = groupBy, having, orderBy

        //noch enthält cursor subset der Tabelle, deshalb ->

        StringBuilder buffer = new StringBuilder();
        int indexUID = cursor.getColumnIndex(DBmerchantHeroesHelper.UID);
        int indexName = cursor.getColumnIndex(DBmerchantHeroesHelper.NAME);
        int indexHitpoints = cursor.getColumnIndex(DBmerchantHeroesHelper.HITPOINTS);
        int indexClassOne = cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_ONE);
        int indexClassTwo = cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_TWO);
        int indexCosts = cursor.getColumnIndex(DBmerchantHeroesHelper.COSTS);

        while (cursor.moveToNext()) {

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            int hitpoints = cursor.getInt(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);
            int costs = cursor.getInt(indexCosts);

            buffer.append(cid + " " + name + " " + hitpoints + " " + classOne + " " + classTwo + "\n");
        }

        return buffer.toString();
    }

    public String getOneHeroRow(int id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.UID, DBmerchantHeroesHelper.NAME, DBmerchantHeroesHelper.HITPOINTS, DBmerchantHeroesHelper.CLASS_ONE, DBmerchantHeroesHelper.CLASS_TWO, DBmerchantHeroesHelper.COSTS};
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        StringBuilder buffer = new StringBuilder();

        try {
            int indexUID = cursor.getColumnIndex(DBmerchantHeroesHelper.UID);
            int indexName = cursor.getColumnIndex(DBmerchantHeroesHelper.NAME);
            int indexHitpoints = cursor.getColumnIndex(DBmerchantHeroesHelper.HITPOINTS);
            int indexClassOne = cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_ONE);
            int indexClassTwo = cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_TWO);
            int indexCosts = cursor.getColumnIndex(DBmerchantHeroesHelper.COSTS);

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            int hitpoints = cursor.getInt(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);
            int costs = cursor.getInt(indexCosts);

            buffer.append(cid + " " + name + "\n" + hitpoints + "\n" + classOne + "\n" + classTwo + "\n" + costs);
        }catch (SQLiteException e){
            Message.message(context1, "ERROR @ getOneHeroRow with exception: " + e);
        }

        return buffer.toString();
    }

    public String getHeroName(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor.getString(cursor.getColumnIndex(DBmerchantHeroesHelper.NAME));
    }

    public int getTableRowCount(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = { DBmerchantHeroesHelper.UID };
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, null, null, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int deleteRow(int index) {
        String[] whereArgs = {index + ""};
        SQLiteDatabase db = helper.getWritableDatabase();

        int count = db.delete(DBmerchantHeroesHelper.TABLE_NAME, DBmerchantHeroesHelper.UID + "=?", whereArgs);
        db.execSQL("VACUUM");
        db.close();

        return count;
    }

    public int updateRow(int id, String newName) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBmerchantHeroesHelper.NAME, newName);
        String[] whereArgs = {id + ""};

        return db.update(DBmerchantHeroesHelper.TABLE_NAME, cv, DBmerchantHeroesHelper.UID + " =? ", whereArgs);
    }

    static class DBmerchantHeroesHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "merchantherosdatabase";
        private static final String TABLE_NAME = "MERCHANTTABLE";
        private static final int DATABASE_VERSION = 9;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String HITPOINTS = "Hitpoints";
        private static final String CLASS_ONE = "ClasstypeOne";
        private static final String CLASS_TWO = "ClasstypeSecondary";
        private static final String COSTS = "Costs";
        // hier Spalten deklarieren, die für Helden benötigt werden
        // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + HITPOINTS + " INT, "
                + CLASS_ONE + " VARCHAR(255), "
                + CLASS_TWO + " VARCHAR(255), "
                + COSTS + " INT);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBmerchantHeroesHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
            //com.example.thomas.voyage.Message.message(context, "constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            com.example.thomas.voyage.Message.message(context, "onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROP_TABLE);
            onCreate(db);
            com.example.thomas.voyage.Message.message(context, "onUpgrade called");
        }
    }
}
