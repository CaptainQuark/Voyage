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


public class DBplayerItemsAdapter {

    DBplayerItemsHelper helper;
    Context c;

    public DBplayerItemsAdapter(Context context) {
        helper = new DBplayerItemsHelper(context);
        helper.getWritableDatabase();
        c = context;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBplayerItemsHelper.TABLE_NAME);
    }

    public long insertData(String name, int skillsId, String desMain, String desAdd, int buyCosts, int spellCosts, String rarity) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(DBplayerItemsHelper.NAME, name);
        cv.put(DBplayerItemsHelper.SKILLS_ID, skillsId);
        cv.put(DBplayerItemsHelper.DESCRIPTION_MAIN, desMain);
        cv.put(DBplayerItemsHelper.DESCRIPTION_ADD, desAdd);
        cv.put(DBplayerItemsHelper.COSTS_TO_BUY, buyCosts);
        cv.put(DBplayerItemsHelper.COSTS_TO_SPELL, spellCosts);
        cv.put(DBplayerItemsHelper.RARITY, rarity);

        long id = db.insert(DBplayerItemsHelper.TABLE_NAME, null, cv);
        db.close();

        return id;
    }

    public String getOneItemRow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {
                DBplayerItemsHelper.UID,
                DBplayerItemsHelper.NAME,
                DBplayerItemsHelper.SKILLS_ID,
                DBplayerItemsHelper.DESCRIPTION_MAIN,
                DBplayerItemsHelper.DESCRIPTION_ADD,
                DBplayerItemsHelper.COSTS_TO_BUY,
                DBplayerItemsHelper.COSTS_TO_SPELL,
                DBplayerItemsHelper.RARITY};

        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        StringBuilder buffer = new StringBuilder();

        try {
            int indexUID = cursor.getColumnIndex(DBplayerItemsHelper.UID);
            int indexName = cursor.getColumnIndex(DBplayerItemsHelper.NAME);
            int indexHitpoints = cursor.getColumnIndex(DBplayerItemsHelper.SKILLS_ID);
            int indexClassOne = cursor.getColumnIndex(DBplayerItemsHelper.DESCRIPTION_MAIN);
            int indexClassTwo = cursor.getColumnIndex(DBplayerItemsHelper.DESCRIPTION_ADD);
            int indexCostsBuy = cursor.getColumnIndex(DBplayerItemsHelper.COSTS_TO_BUY);
            int indexCostsSpell = cursor.getColumnIndex(DBplayerItemsHelper.COSTS_TO_SPELL);
            int indexImage = cursor.getColumnIndex(DBplayerItemsHelper.RARITY);

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            int hitpoints = cursor.getInt(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);
            int costsBuy = cursor.getInt(indexCostsBuy);
            int costsSpell = cursor.getInt(indexCostsSpell);
            String image = cursor.getString(indexImage);

            buffer.append(cid + " " + name + "\n" + hitpoints + "\n" + classOne + "\n" + classTwo + "\n" + costsBuy + "\n" + costsSpell + '\n' + image);

        } catch (SQLiteException e) {

            Msg.msg(c, "ERROR @ getOneItemRow with exception: " + e);

        }

        db.close();
        return buffer.toString();
    }

    public String getItemName(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBplayerItemsHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBplayerItemsHelper.NAME));
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getItemName with exception: " + n);
        }

        db.close();
        return value;
    }

    public int getItemSkillsId(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBplayerItemsHelper.SKILLS_ID};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBplayerItemsHelper.SKILLS_ID));

            cursor.close();
            db.close();
        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        db.close();
        return value;
    }

    public String getItemDescriptionMain(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBplayerItemsHelper.DESCRIPTION_MAIN};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBplayerItemsHelper.DESCRIPTION_MAIN));

            cursor.close();
            db.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getitemDesMain with exception: " + n);
        }

        db.close();
        return value;
    }

    public String getItemDescriptionAdditonal(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBplayerItemsHelper.DESCRIPTION_ADD};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBplayerItemsHelper.DESCRIPTION_ADD));

            cursor.close();
            db.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getItemDesAdd with exception: " + n);
        }

        db.close();
        return value;
    }

    public int getItemBuyCosts(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBplayerItemsHelper.COSTS_TO_BUY};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBplayerItemsHelper.COSTS_TO_BUY));
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getItemCosts with exception: " + n);
        }

        db.close();
        return value;
    }

    public int getItemSpellCosts(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBplayerItemsHelper.COSTS_TO_SPELL};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBplayerItemsHelper.COSTS_TO_SPELL));
            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        db.close();
        return value;
    }

    public String getItemRarity(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBplayerItemsHelper.RARITY};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBplayerItemsHelper.TABLE_NAME, columns, DBplayerItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBplayerItemsHelper.RARITY));

            cursor.close();

        } catch (NullPointerException n) {
            Msg.msg(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        db.close();
        return value;
    }

    public int updateRowWithItemData(int UID, String name, int skillsId, String desMain, String desAdd, int buyCosts, int spellCosts, String rarity) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBplayerItemsHelper.NAME, name);
        cv.put(DBplayerItemsHelper.SKILLS_ID, skillsId);
        cv.put(DBplayerItemsHelper.DESCRIPTION_MAIN, desMain);
        cv.put(DBplayerItemsHelper.DESCRIPTION_ADD, desAdd);
        cv.put(DBplayerItemsHelper.COSTS_TO_BUY, buyCosts);
        cv.put(DBplayerItemsHelper.COSTS_TO_SPELL, spellCosts);
        cv.put(DBplayerItemsHelper.RARITY, rarity);

        String[] whereArgs = {UID + "", c.getString(R.string.indicator_unused_row)};

        int validation = db.update(DBplayerItemsHelper.TABLE_NAME, cv, DBplayerItemsHelper.UID + "=? AND " + DBplayerItemsHelper.NAME + "=?", whereArgs);
        db.close();

        return validation;
    }

    public int markOneRowAsUnused(int id){

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBplayerItemsHelper.NAME, c.getResources().getString(R.string.indicator_unused_row));
        cv.put(DBplayerItemsHelper.SKILLS_ID, 0);
        cv.put(DBplayerItemsHelper.DESCRIPTION_MAIN, "");
        cv.put(DBplayerItemsHelper.DESCRIPTION_ADD, "");
        cv.put(DBplayerItemsHelper.COSTS_TO_BUY, 0);
        cv.put(DBplayerItemsHelper.COSTS_TO_SPELL, 0);
        cv.put(DBplayerItemsHelper.RARITY, c.getResources().getString(R.string.indicator_unused_row));

        String[] whereArgs = {Integer.toString(id)};

        int validation = db.update(DBplayerItemsHelper.TABLE_NAME, cv, DBplayerItemsHelper.UID + "=?", whereArgs);
        db.close();

        return validation;
    }

    static class DBplayerItemsHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "itemsdatabase";
        private static final String TABLE_NAME = "ITEMSTABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String SKILLS_ID = "SkillsId";
        private static final String DESCRIPTION_MAIN = "Des1";
        private static final String DESCRIPTION_ADD = "Des2";
        private static final String COSTS_TO_BUY = "BuyCosts";
        private static final String COSTS_TO_SPELL = "SpellCosts";
        private static final String RARITY = "Rarity";
        // hier Spalten deklarieren, die für ein Item benötigt werden
        // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + SKILLS_ID + " INT, "
                + DESCRIPTION_MAIN + " VARCHAR(255), "
                + DESCRIPTION_ADD + " VARCHAR(255), "
                + COSTS_TO_BUY + " INT, "
                + COSTS_TO_SPELL + " INT, "
                + RARITY + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBplayerItemsHelper(Context context){
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
            //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            Log.v("ITEM CREATE", "ItemdDatabase onCreate called");
            Msg.msg(context, "ItemsDatabase onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Msg.msg(context, "HerosDatabse onUpgrade called");
            Log.v("ITEM UPGRADE", "heroes db upgraded");
        }
    }
}
