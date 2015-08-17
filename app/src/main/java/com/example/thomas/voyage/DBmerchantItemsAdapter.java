package com.example.thomas.voyage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBmerchantItemsAdapter {

    DBmerchantItemsHelper helper;
    Context c;

    public DBmerchantItemsAdapter(Context context) {
        helper = new DBmerchantItemsHelper(context);
        helper.getWritableDatabase();
        c = context;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBmerchantItemsHelper.TABLE_NAME);
    }

    public long insertData(String name, int skillsId, String desMain, String desAdd, int buyCosts, int spellCosts, String rarity) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(DBmerchantItemsHelper.NAME, name);
        cv.put(DBmerchantItemsHelper.SKILLS_ID, skillsId);
        cv.put(DBmerchantItemsHelper.DESCRIPTION_MAIN, desMain);
        cv.put(DBmerchantItemsHelper.DESCRIPTION_ADD, desAdd);
        cv.put(DBmerchantItemsHelper.COSTS_TO_BUY, buyCosts);
        cv.put(DBmerchantItemsHelper.COSTS_TO_SPELL, spellCosts);
        cv.put(DBmerchantItemsHelper.RARITY, rarity);

        long id = db.insert(DBmerchantItemsHelper.TABLE_NAME, null, cv);
        db.close();

        return id;
    }

    public String getOneItemRow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {
                DBmerchantItemsHelper.UID,
                DBmerchantItemsHelper.NAME,
                DBmerchantItemsHelper.SKILLS_ID,
                DBmerchantItemsHelper.DESCRIPTION_MAIN,
                DBmerchantItemsHelper.DESCRIPTION_ADD,
                DBmerchantItemsHelper.COSTS_TO_BUY,
                DBmerchantItemsHelper.COSTS_TO_SPELL,
                DBmerchantItemsHelper.RARITY};

        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        StringBuilder buffer = new StringBuilder();

        try {
            int indexUID = cursor.getColumnIndex(DBmerchantItemsHelper.UID);
            int indexName = cursor.getColumnIndex(DBmerchantItemsHelper.NAME);
            int indexHitpoints = cursor.getColumnIndex(DBmerchantItemsHelper.SKILLS_ID);
            int indexClassOne = cursor.getColumnIndex(DBmerchantItemsHelper.DESCRIPTION_MAIN);
            int indexClassTwo = cursor.getColumnIndex(DBmerchantItemsHelper.DESCRIPTION_ADD);
            int indexCostsBuy = cursor.getColumnIndex(DBmerchantItemsHelper.COSTS_TO_BUY);
            int indexCostsSpell = cursor.getColumnIndex(DBmerchantItemsHelper.COSTS_TO_SPELL);
            int indexImage = cursor.getColumnIndex(DBmerchantItemsHelper.RARITY);

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

            Message.message(c, "ERROR @ getOneItemRow with exception: " + e);

        } catch (NullPointerException n) {

            Message.message(c, "ERROR @ getOneItemRow with exception: " + n);
        }

        db.close();
        return buffer.toString();
    }

    public String getItemName(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantItemsHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBmerchantItemsHelper.NAME));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemName with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getItemSkillsId(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantItemsHelper.SKILLS_ID};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBmerchantItemsHelper.SKILLS_ID));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getItemDescriptionMain(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantItemsHelper.DESCRIPTION_MAIN};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBmerchantItemsHelper.DESCRIPTION_MAIN));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getitemDesMain with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getItemDescriptionAdditonal(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantItemsHelper.DESCRIPTION_ADD};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBmerchantItemsHelper.DESCRIPTION_ADD));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemDesAdd with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getItemBuyCosts(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantItemsHelper.COSTS_TO_BUY};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(DBmerchantItemsHelper.COSTS_TO_BUY));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemCosts with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getItemSpellCosts(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantItemsHelper.COSTS_TO_SPELL};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBmerchantItemsHelper.COSTS_TO_SPELL));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getItemRarity(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantItemsHelper.RARITY};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantItemsHelper.TABLE_NAME, columns, DBmerchantItemsHelper.UID + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "";

        try {
            value = cursor.getString(cursor.getColumnIndex(DBmerchantItemsHelper.RARITY));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getItemSkillsId with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int updateRowWithItemData(int UID, String name, int skillsId, String desMain, String desAdd, int buyCosts, int spellCosts, String rarity) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBmerchantItemsHelper.NAME, name);
        cv.put(DBmerchantItemsHelper.SKILLS_ID, skillsId);
        cv.put(DBmerchantItemsHelper.DESCRIPTION_MAIN, desMain);
        cv.put(DBmerchantItemsHelper.DESCRIPTION_ADD, desAdd);
        cv.put(DBmerchantItemsHelper.COSTS_TO_BUY, buyCosts);
        cv.put(DBmerchantItemsHelper.COSTS_TO_SPELL, spellCosts);
        cv.put(DBmerchantItemsHelper.RARITY, rarity);

        String[] whereArgs = {UID + "", c.getString(R.string.indicator_unused_row)};

        int validation = db.update(DBmerchantItemsHelper.TABLE_NAME, cv, DBmerchantItemsHelper.UID + "=? AND " + DBmerchantItemsHelper.NAME + "=?", whereArgs);
        db.close();

        return validation;
    }

    public int markOneRowAsUnused(int id){

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBmerchantItemsHelper.NAME, c.getResources().getString(R.string.indicator_unused_row));
        cv.put(DBmerchantItemsHelper.SKILLS_ID, 0);
        cv.put(DBmerchantItemsHelper.DESCRIPTION_MAIN, "");
        cv.put(DBmerchantItemsHelper.DESCRIPTION_ADD, "");
        cv.put(DBmerchantItemsHelper.COSTS_TO_BUY, 0);
        cv.put(DBmerchantItemsHelper.COSTS_TO_SPELL, 0);
        cv.put(DBmerchantItemsHelper.RARITY, c.getResources().getString(R.string.indicator_unused_row));

        String[] whereArgs = {Integer.toString(id)};

        int validation = db.update(DBmerchantItemsHelper.TABLE_NAME, cv, DBmerchantItemsHelper.UID + "=?", whereArgs);
        db.close();

        return validation;
    }

    static class DBmerchantItemsHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "merchantitemsdatabase";
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

        DBmerchantItemsHelper(Context context){
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
