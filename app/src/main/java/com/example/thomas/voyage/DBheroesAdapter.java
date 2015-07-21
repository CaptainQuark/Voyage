package com.example.thomas.voyage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

/**
 * Created by Thomas on 21-Jul-15.
 */
public class DBheroesAdapter {

    DBheroesHelper helper;
    public DBheroesAdapter (Context context) { helper = new DBheroesHelper(context);}

    public long insertData(String name, int hitpoints){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBheroesHelper.NAME, name);
        contentValues.put(DBheroesHelper.HITPOINTS, hitpoints);

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

        String[] columns = {DBheroesHelper.UID, DBheroesHelper.NAME, DBheroesHelper.HITPOINTS};
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

        while(cursor.moveToNext()){

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            String password = cursor.getString(indexHitpoints);

            buffer.append(cid + " " + name + " " + password + "\n");
        }

        return buffer.toString();
    }

    public int updateName(String oldName, String newName){
        //UPDATE VIVZTABLE SET Name = 'Lukas' where Name=? 'Thomas'

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.NAME, newName);
        String[] whereArgs = {oldName};

        int count = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.NAME + " =? ", whereArgs);

        return count;
    }

    public int deleteRow(){
        //DELETE * FROM VIVZTABLE Where Name='Thomas'

        String[] whereArgs = {"Thomas"};
            //zum Test hardcoded

        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete(DBheroesHelper.TABLE_NAME, DBheroesHelper.NAME + "=?", whereArgs);

        return count;
    }

    static class DBheroesHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "heroesdatabase";
        private static final String TABLE_NAME = "HEROESTABLE";
        private static final int DATABASE_VERSION = 2;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String HITPOINTS = "Hitpoints";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + HITPOINTS + " INTEGER);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBheroesHelper (Context context){
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
                //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
            com.example.thomas.voyage.Message.message(context, "constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            com.example.thomas.voyage.Message.message(context, "onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            com.example.thomas.voyage.Message.message(context, "onUpgrade called");
        }
    }
}
