package com.example.thomas.voyage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Capt'n on 17.08.2015.
 */
public class DBitems {

    DBitemsHelper helper;
    Context c;

    public DBitems(Context context) {
        helper = new DBitemsHelper(context);
        helper.getWritableDatabase();
        c = context;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBitemsHelper.TABLE_NAME);
    }

    public long insertData(String name, int skillsId, String desMain, String desAdd, int costs, String rarity) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBitemsHelper.NAME, name);
        contentValues.put(DBitemsHelper.SKILLS_ID, skillsId);
        contentValues.put(DBitemsHelper.DESCRIPTION_MAIN, desMain);
        contentValues.put(DBitemsHelper.DESCRIPTION_ADD, desAdd);
        contentValues.put(DBitemsHelper.COSTS, costs);
        contentValues.put(DBitemsHelper.RARITY, rarity);

        long id = db.insert(DBitemsHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;
    }

    public String getOneItemRow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {
                DBitemsHelper.UID,
                DBitemsHelper.NAME,
                DBitemsHelper.SKILLS_ID,
                DBitemsHelper.DESCRIPTION_MAIN,
                DBitemsHelper.DESCRIPTION_ADD,
                DBitemsHelper.COSTS,
                DBitemsHelper.RARITY};

        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBitemsHelper.TABLE_NAME, columns, DBitemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        StringBuilder buffer = new StringBuilder();

        try {
            int indexUID = cursor.getColumnIndex(DBitemsHelper.UID);
            int indexName = cursor.getColumnIndex(DBitemsHelper.NAME);
            int indexHitpoints = cursor.getColumnIndex(DBitemsHelper.SKILLS_ID);
            int indexClassOne = cursor.getColumnIndex(DBitemsHelper.DESCRIPTION_MAIN);
            int indexClassTwo = cursor.getColumnIndex(DBitemsHelper.DESCRIPTION_ADD);
            int indexCosts = cursor.getColumnIndex(DBitemsHelper.COSTS);
            int indexImage = cursor.getColumnIndex(DBitemsHelper.RARITY);

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            int hitpoints = cursor.getInt(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);
            int costs = cursor.getInt(indexCosts);
            String image = cursor.getString(indexImage);

            buffer.append(cid + " " + name + "\n" + hitpoints + "\n" + classOne + "\n" + classTwo + "\n" + costs + "\n" + image);

        } catch (SQLiteException e) {

            Message.message(c, "ERROR @ getOneItemRow with exception: " + e);

        } catch (NullPointerException n) {

            Message.message(c, "ERROR @ getOneItemRow with exception: " + n);
        }

        db.close();
        return buffer.toString();
    }

    public String getItemName(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBitemsHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBitemsHelper.TABLE_NAME, columns, DBitemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBitemsHelper.NAME));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemName with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getItemSkillsId(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBitemsHelper.SKILLS_ID};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBitemsHelper.TABLE_NAME, columns, DBitemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBitemsHelper.SKILLS_ID));
        } catch (java.lang.NullPointerException n) {
            Message.message(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getItemDescriptionMain(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBitemsHelper.DESCRIPTION_MAIN};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBitemsHelper.TABLE_NAME, columns, DBitemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBitemsHelper.DESCRIPTION_MAIN));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getitemDesMain with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getItemDescriptionAdditonal(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBitemsHelper.DESCRIPTION_ADD};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBitemsHelper.TABLE_NAME, columns, DBitemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBitemsHelper.DESCRIPTION_ADD));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemDesAdd with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getItemCosts(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBitemsHelper.COSTS};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBitemsHelper.TABLE_NAME, columns, DBitemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBitemsHelper.COSTS));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemCosts with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getItemRarity(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBitemsHelper.RARITY};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBitemsHelper.TABLE_NAME, columns, DBitemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBitemsHelper.RARITY));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int updateRowWithItemData(int UID, String name, int skillsId, String desMain, String desAdd, int costs, String rarity) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBitemsHelper.NAME, name);
        cv.put(DBitemsHelper.SKILLS_ID, skillsId);
        cv.put(DBitemsHelper.DESCRIPTION_MAIN, desMain);
        cv.put(DBitemsHelper.DESCRIPTION_ADD, desAdd);
        cv.put(DBitemsHelper.COSTS, costs);
        cv.put(DBitemsHelper.RARITY, rarity);

        String[] whereArgs = {UID + "", c.getString(R.string.indicator_unused_row)};

        int validation = db.update(DBitemsHelper.TABLE_NAME, cv, DBitemsHelper.UID + "=? AND " + DBitemsHelper.NAME + "=?", whereArgs);
        db.close();

        return validation;
    }

    public int markOneRowAsUnused(int id){

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBitemsHelper.NAME, c.getResources().getString(R.string.indicator_unused_row));
        cv.put(DBitemsHelper.SKILLS_ID, 0);
        cv.put(DBitemsHelper.DESCRIPTION_MAIN, "");
        cv.put(DBitemsHelper.DESCRIPTION_ADD, "");
        cv.put(DBitemsHelper.COSTS, 0);
        cv.put(DBitemsHelper.RARITY, c.getResources().getString(R.string.indicator_unused_row));

        String[] whereArgs = {Integer.toString(id)};

        int validation = db.update(DBitemsHelper.TABLE_NAME, cv, DBitemsHelper.UID + "=?", whereArgs);
        db.close();

        return validation;
    }

    static class DBitemsHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "itemsdatabase";
        private static final String TABLE_NAME = "ITEMSTABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String SKILLS_ID = "SkillsId";
        private static final String DESCRIPTION_MAIN = "Des1";
        private static final String DESCRIPTION_ADD = "Des2";
        private static final String COSTS = "Costs";
        private static final String RARITY = "ImageRessource";
        // hier Spalten deklarieren, die für Helden benötigt werden
        // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + SKILLS_ID + " INT, "
                + DESCRIPTION_MAIN + " VARCHAR(255), "
                + DESCRIPTION_ADD + " VARCHAR(255), "
                + COSTS + " INT, "
                + RARITY + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBitemsHelper (Context context){
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
            //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            Log.v("ITEM CREATE", "ItemdDatabase onCreate called");
            Message.message(context, "ItemsDatabase onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Message.message(context, "HerosDatabse onUpgrade called");
            Log.v("ITEM UPGRADE", "heroes db upgraded");
        }
    }
}
