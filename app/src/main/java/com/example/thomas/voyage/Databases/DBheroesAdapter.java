package com.example.thomas.voyage.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.R;

import java.text.ParseException;
import java.util.Objects;

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
        contentValues.put(DBheroesHelper.HP_TOTAL, hitpoints);
        contentValues.put(DBheroesHelper.MED_SLOT_INDEX, -1);
        contentValues.put(DBheroesHelper.TIME_TO_LEAVE, "0");

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

            Msg.msg(context1, "ERROR @ getOneHeroRow with exception: " + e);

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
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroName with exception: " + n);
        }

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
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroHitpoints with exception: " + n);
        }

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
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroClassOne with exception: " + n);
        }

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
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroClassTwo with exception: " + n);
        }

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
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroHitpoints with exception: " + n);
        }

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

            cursor.close();
            db.close();

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroImgRes with exception: " + n);
        }

        return value;
    }

    public int getHeroHitpointsTotal(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.HP_TOTAL};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        int value = -1;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                value = cursor.getInt(cursor.getColumnIndex(DBheroesHelper.HP_TOTAL));
                cursor.close();
            }
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroHitpointsTotal with exception: " + n);
        }

        db.close();

        return value;
    }

    public int getMedSlotIndex(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.MED_SLOT_INDEX};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        int value = -1;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                value = cursor.getInt(cursor.getColumnIndex(DBheroesHelper.MED_SLOT_INDEX));
                cursor.close();
            }
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getMedSlotIndex with exception: " + n);
        }

        db.close();

        return value;
    }

    public long getTimeToLeave(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.TIME_TO_LEAVE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        long value = -1;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                String s = cursor.getString(cursor.getColumnIndex(DBheroesHelper.TIME_TO_LEAVE));
                try{
                    value = Long.parseLong(s);
                }catch (Exception p){
                    Msg.msg(context1, String.valueOf(p));
                }

                cursor.close();
            }
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getTimeToLeave with exception: " + n);
        }

        db.close();

        return value;
    }

    public int getEvasion(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBheroesHelper.EVASION};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBheroesHelper.TABLE_NAME, columns, DBheroesHelper.UID + "=?", selectionArgs, null, null, null);

        int value = -1;

        try {
            if (cursor != null) {
                try{
                    cursor.moveToFirst();
                    value = cursor.getInt(cursor.getColumnIndex(DBheroesHelper.EVASION));

                }catch (Exception p){
                    Msg.msg(context1, String.valueOf(p));
                }

                cursor.close();
            }
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getTimeToLeave with exception: " + n);
        }

        db.close();

        return value;
    }

    public int updateRowWithHeroData(int UID, String name, int hitpoints, String primaryClass, String secondaryClass, int costs, String image, int hpTotal, int medSlotIndex, long timeToLeave, int evasion) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.NAME, name);
        cv.put(DBheroesHelper.HITPOINTS, hitpoints);
        cv.put(DBheroesHelper.CLASS_ONE, primaryClass);
        cv.put(DBheroesHelper.CLASS_TWO, secondaryClass);
        cv.put(DBheroesHelper.COSTS, costs);
        cv.put(DBheroesHelper.IMAGE_RESOURCE, image);
        cv.put(DBheroesHelper.HP_TOTAL, hpTotal);
        cv.put(DBheroesHelper.MED_SLOT_INDEX, medSlotIndex);
        cv.put(DBheroesHelper.TIME_TO_LEAVE, Objects.toString(timeToLeave, "0"));
        cv.put(DBheroesHelper.EVASION, evasion);

        String[] whereArgs = {UID + "", context1.getString(R.string.indicator_unused_row)};

        int validation = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.UID + "=? AND " + DBheroesHelper.NAME + "=?", whereArgs);
        db.close();

        return validation;
    }

    public int updateHeroHitpoints(int UID, int hitpoints) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.HITPOINTS, hitpoints);

        String[] whereArgs = {UID + ""};

        int validation = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.UID + "=?", whereArgs);
        db.close();

        return validation;
    }

    public boolean updateMedSlotIndex(int UID, int slotIndex) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.MED_SLOT_INDEX, slotIndex);

        String[] whereArgs = {UID + ""};

        int validation = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.UID + "=?", whereArgs);
        db.close();

        return validation != -1;
    }

    public boolean updateTimeToLeave(int UID, long time) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBheroesHelper.TIME_TO_LEAVE, Objects.toString(time, "0"));

        String[] whereArgs = {UID + ""};

        int validation = db.update(DBheroesHelper.TABLE_NAME, cv, DBheroesHelper.UID + "=?", whereArgs);
        db.close();

        return validation != -1;
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
        cv.put(DBheroesHelper.HP_TOTAL, 0);
        cv.put(DBheroesHelper.MED_SLOT_INDEX, -1);
        cv.put(DBheroesHelper.TIME_TO_LEAVE, "0");
        cv.put(DBheroesHelper.EVASION, -1);

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
        private static final String HP_TOTAL = "HitpointsTotal";
        private static final String MED_SLOT_INDEX = "MedicationSlotIndex";
        private static final String TIME_TO_LEAVE = "TimeToLeave";
        private static final String EVASION = "Evasion";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + HITPOINTS + " INTEGER, "
                + CLASS_ONE + " VARCHAR(255), "
                + CLASS_TWO + " VARCHAR(255), "
                + COSTS + " INTEGER, "
                + IMAGE_RESOURCE + " VARCHAR(255), "
                + HP_TOTAL + " INTEGER, "
                + MED_SLOT_INDEX + " INTEGER, "
                + TIME_TO_LEAVE + " TEXT, "
                + EVASION + "INTEGER);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBheroesHelper (Context context){
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
                //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
            //com.example.thomas.voyage.ContainerClasses.Msg.msg(context, "HeroesDatabase constructor called");
            //com.example.thomas.voyage.ContainerClasses.Msg.msg(context, "HeroesDatabase constructor called");

        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            Msg.msg(context, "HerosDatabse onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Msg.msg(context, "HerosDatabse onUpgrade called");
            Log.v("HEROES UPGRADE", "heroes db upgraded");
        }
    }
}
