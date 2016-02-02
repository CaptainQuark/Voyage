package com.example.thomas.voyage.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Msg;

public class DBghostScoreDataAdapter {

    DBghostScoreHelper helper;
    Context context1;

    public DBghostScoreDataAdapter(Context context) {
        helper = new DBghostScoreHelper(context);
        helper.getWritableDatabase();
        context1 = context;
    }

    public long insertData(int gameId, int throwId, int fist, int second, int third) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBghostScoreHelper.GAME_ID, gameId);
        contentValues.put(DBghostScoreHelper.THROW_ID_PER_GAME, throwId);
        contentValues.put(DBghostScoreHelper.FIRST_THROW, fist);
        contentValues.put(DBghostScoreHelper.SECOND_THROW, second);
        contentValues.put(DBghostScoreHelper.THIRD_THROW, third);

        long id = db.insert(DBghostScoreHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;
        // .) wenn id = -1, dann fehler, sonst ok
        // .) kann auf VivzHelper zugreifen, da dies eine innere Klasse ist,
        //    niemand sonst kann Variablen von VivzHelper verwenden = kein falscher Zugriff
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBghostScoreHelper.TABLE_NAME);
    }

    public long getRowsByGameCount(int gameId){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] selectionArgs = {String.valueOf(gameId)};

        long count = DatabaseUtils.queryNumEntries(db, DBghostScoreHelper.TABLE_NAME,DBghostScoreHelper.GAME_ID + "=?" + gameId, selectionArgs);
        return count;
    }

    public String getAllData(){
        SQLiteDatabase db = helper.getReadableDatabase();
        //helper.get... = sql database object das database repräsentiert

        String[] columns = {
                DBghostScoreHelper.UID,
                DBghostScoreHelper.GAME_ID,
                DBghostScoreHelper.THROW_ID_PER_GAME,
                DBghostScoreHelper.FIRST_THROW,
                DBghostScoreHelper.SECOND_THROW,
                DBghostScoreHelper.THIRD_THROW};
        //Spalten für db.query Abfrage

        Cursor cursor = db.query(DBghostScoreHelper.TABLE_NAME, columns, null, null, null, null, null);
        //1. null = selectoin = Filtern nach bestimmten Kritieren
        //2. null = selectionArgs
        //letzten drei = groupBy, having, orderBy

        //noch enthält cursor subset der Tabelle, deshalb ->

        StringBuilder buffer = new StringBuilder();
        int indexUID = cursor.getColumnIndex(DBghostScoreHelper.UID);
        int indexGameId = cursor.getColumnIndex(DBghostScoreHelper.GAME_ID);
        int indexThrowId = cursor.getColumnIndex(DBghostScoreHelper.THROW_ID_PER_GAME);
        int indexFirst = cursor.getColumnIndex(DBghostScoreHelper.FIRST_THROW);
        int indexSecond = cursor.getColumnIndex(DBghostScoreHelper.SECOND_THROW);
        int indexThird = cursor.getColumnIndex(DBghostScoreHelper.THIRD_THROW);

        while(cursor.moveToNext()){

            int cid = cursor.getInt(indexUID);
            int name = cursor.getInt(indexGameId);
            int hitpoints = cursor.getInt(indexThrowId);
            int classOne = cursor.getInt(indexFirst);
            int classTwo = cursor.getInt(indexSecond);
            int costs = cursor.getInt(indexThird);

            buffer.append(cid + " " + name + " " + hitpoints + " " + classOne + " " + classTwo + " " + costs + " " + "\n");
        }

        cursor.close();
        return buffer.toString();
    }

    public int getFirstThrow(int gameId, int throwId) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostScoreHelper.FIRST_THROW};
        String[] selectionArgs = {String.valueOf(gameId), String.valueOf(throwId)};
        Cursor cursor = db.query(DBghostScoreHelper.TABLE_NAME, columns, DBghostScoreHelper.GAME_ID + "=? AND " + DBghostScoreHelper.THROW_ID_PER_GAME + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBghostScoreHelper.FIRST_THROW));
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ firstThrow with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getSecondThrow(int gameId, int throwId) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostScoreHelper.SECOND_THROW};
        String[] selectionArgs = {String.valueOf(gameId), String.valueOf(throwId)};
        Cursor cursor = db.query(DBghostScoreHelper.TABLE_NAME, columns, DBghostScoreHelper.GAME_ID
                + " =? AND " + DBghostScoreHelper.THROW_ID_PER_GAME + " =? ", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBghostScoreHelper.SECOND_THROW));
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ firstThrow with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getThirdThrow(int gameId, int throwId) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBghostScoreHelper.THIRD_THROW};
        String[] selectionArgs = {String.valueOf(gameId), String.valueOf(throwId)};
        Cursor cursor = db.query(DBghostScoreHelper.TABLE_NAME, columns, DBghostScoreHelper.GAME_ID
                + "=? AND " + DBghostScoreHelper.THROW_ID_PER_GAME + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBghostScoreHelper.THIRD_THROW));
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ firstThrow with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    static class DBghostScoreHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "ghostscorehelper";
        private static final String TABLE_NAME = "GHOST_SCORE_TABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String GAME_ID = "GameId";
        private static final String THROW_ID_PER_GAME = "ThrowIdPerGame";
        private static final String FIRST_THROW = "FirstThrow";
        private static final String SECOND_THROW = "SecondThrow";
        private static final String THIRD_THROW = "ThirdThrow";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GAME_ID + " INT, "
                + THROW_ID_PER_GAME + " INT, "
                + FIRST_THROW + " INT, "
                + SECOND_THROW + " INT, "
                + THIRD_THROW + " INT);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBghostScoreHelper(Context context){
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
            Msg.msg(context, "DBghostScoreHelper onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Msg.msg(context, "DBghostScoreHelper onUpgrade called");
            Log.v("HEROES UPGRADE", "DBghostScoreHelper db upgraded");
        }
    }
}
