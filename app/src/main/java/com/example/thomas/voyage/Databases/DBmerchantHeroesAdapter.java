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

public class DBmerchantHeroesAdapter {

    DBmerchantHeroesHelper helper;
    Context context1;

    public DBmerchantHeroesAdapter(Context context) {
        helper = new DBmerchantHeroesHelper(context);
        helper.getWritableDatabase();
        context1 = context;
    }

    public long insertData(String name, int hitpoints, String classOne, String classTwo, int costs, String imageResource, int evasion, int hpTotal, int bonusNumber) {

        Log.i("INSERT", "insert Data called");

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBmerchantHeroesHelper.NAME, name);
        contentValues.put(DBmerchantHeroesHelper.HITPOINTS, hitpoints);
        contentValues.put(DBmerchantHeroesHelper.CLASS_ONE, classOne);
        contentValues.put(DBmerchantHeroesHelper.CLASS_TWO, classTwo);
        contentValues.put(DBmerchantHeroesHelper.COSTS, costs);
        contentValues.put(DBmerchantHeroesHelper.IMAGE_RESOURCE, imageResource);
        contentValues.put(DBmerchantHeroesHelper.EVASION, evasion);
        contentValues.put(DBmerchantHeroesHelper.HP_TOTAL, hpTotal);
        contentValues.put(DBmerchantHeroesHelper.BONUS_NUMBER, bonusNumber);

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

        String[] columns = {DBmerchantHeroesHelper.UID,
                DBmerchantHeroesHelper.NAME,
                DBmerchantHeroesHelper.HITPOINTS,
                DBmerchantHeroesHelper.CLASS_ONE,
                DBmerchantHeroesHelper.CLASS_TWO,
                DBmerchantHeroesHelper.COSTS,
                DBmerchantHeroesHelper.IMAGE_RESOURCE,
                DBmerchantHeroesHelper.EVASION};

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
        int indexImage = cursor.getColumnIndex(DBmerchantHeroesHelper.IMAGE_RESOURCE);
        int indexEvasion = cursor.getColumnIndex(DBmerchantHeroesHelper.EVASION);

        while (cursor.moveToNext()) {

            int cid = cursor.getInt(indexUID);
            String name = cursor.getString(indexName);
            int hitpoints = cursor.getInt(indexHitpoints);
            String classOne = cursor.getString(indexClassOne);
            String classTwo = cursor.getString(indexClassTwo);
            int costs = cursor.getInt(indexCosts);
            String image = cursor.getString(indexImage);
            int evasion = cursor.getInt(indexEvasion);

            buffer.append(cid + " " + name + " " + hitpoints + " " + classOne + " " + classTwo + " " + costs + " " + image + " " + evasion + '\n');
        }

        cursor.close();
        db.close();
        return buffer.toString();
    }

    public String getOneHeroRow(int id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.UID, DBmerchantHeroesHelper.NAME, DBmerchantHeroesHelper.HITPOINTS, DBmerchantHeroesHelper.CLASS_ONE, DBmerchantHeroesHelper.CLASS_TWO, DBmerchantHeroesHelper.COSTS};
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);



        StringBuilder buffer = new StringBuilder();

        try {
            if(cursor != null){
                cursor.moveToFirst();
                int indexUID = cursor.getColumnIndex(DBmerchantHeroesHelper.UID);
                int indexName = cursor.getColumnIndex(DBmerchantHeroesHelper.NAME);
                int indexHitpoints = cursor.getColumnIndex(DBmerchantHeroesHelper.HITPOINTS);
                int indexClassOne = cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_ONE);
                int indexClassTwo = cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_TWO);
                int indexCosts = cursor.getColumnIndex(DBmerchantHeroesHelper.COSTS);
                int indexImage = cursor.getColumnIndex(DBmerchantHeroesHelper.IMAGE_RESOURCE);
                int indexEvasion = cursor.getColumnIndex(DBmerchantHeroesHelper.EVASION);

                int cid = cursor.getInt(indexUID);
                String name = cursor.getString(indexName);
                int hitpoints = cursor.getInt(indexHitpoints);
                String classOne = cursor.getString(indexClassOne);
                String classTwo = cursor.getString(indexClassTwo);
                int costs = cursor.getInt(indexCosts);
                String image = cursor.getString(indexImage);
                int evasion = cursor.getInt(indexEvasion);

                buffer.append(cid + " " + name + "\n" + hitpoints + "\n" + classOne + "\n" + classTwo + "\n" + costs + "\n" + image + "\n" + evasion);
                cursor.close();
            }

        }catch (SQLiteException e){
            Msg.msg(context1, "ERROR @ getOneHeroRow with exception: " + e);
        }

        db.close();
        return buffer.toString();
    }

    public String getHeroName(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        String value = "";

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                value = cursor.getString(cursor.getColumnIndex(DBmerchantHeroesHelper.NAME));
                cursor.close();
            }

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroName with exception: " + n);
        }

        db.close();

        return value;
    }

    public int getHeroHitpoints(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.HITPOINTS};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        int value = -1;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                value = cursor.getInt(cursor.getColumnIndex(DBmerchantHeroesHelper.HITPOINTS));
                cursor.close();
            }

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroHitpoints with exception: " + n);
        }

        db.close();
        return value;
    }

    public String getHeroClassOne(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.CLASS_ONE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        String value = "";
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                value = cursor.getString(cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_ONE));
                cursor.close();
            }

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroClassOne with exception: " + n);
        }

        db.close();
        return value;
    }

    public String getHeroClassTwo(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.CLASS_TWO};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);


        String value = "";
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                value = cursor.getString(cursor.getColumnIndex(DBmerchantHeroesHelper.CLASS_TWO));
                cursor.close();
            }
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroClassTwo with exception: " + n);
        }

        db.close();

        return value;
    }

    public int getHeroCosts(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.COSTS};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        int costs = 0;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                costs = cursor.getInt(cursor.getColumnIndex(DBmerchantHeroesHelper.COSTS));
                cursor.close();
            }
        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroCosts with exception: " + n);
        }

        db.close();

        return costs;
    }

    public String getHeroImgRes(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.IMAGE_RESOURCE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        String value = "";

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                int indexImage = cursor.getColumnIndex(DBmerchantHeroesHelper.IMAGE_RESOURCE);
                value = cursor.getString(indexImage);
                cursor.close();
            }

        } catch (IndexOutOfBoundsException n) {

            Msg.msg(context1, "ERROR @ getHeroImgRes with exception: " + n);
        }

        db.close();
        return value;
    }

    public int getHeroEvasion(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.EVASION};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        int costs = 0;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                costs = cursor.getInt(cursor.getColumnIndex(DBmerchantHeroesHelper.EVASION));
                cursor.close();
            }

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroEvasion with exception: " + n);
        }

        db.close();

        return costs;
    }

    public int getHpTotal(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.HP_TOTAL};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        int costs = 0;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                costs = cursor.getInt(cursor.getColumnIndex(DBmerchantHeroesHelper.HP_TOTAL));
                cursor.close();
            }

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getHeroEvasion with exception: " + n);
        }

        db.close();

        return costs;
    }

    public int getBonusNumber(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DBmerchantHeroesHelper.BONUS_NUMBER};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBmerchantHeroesHelper.TABLE_NAME, columns, DBmerchantHeroesHelper.UID + "=?", selectionArgs, null, null, null);

        int number = 0;

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                number = cursor.getInt(cursor.getColumnIndex(DBmerchantHeroesHelper.BONUS_NUMBER));
                cursor.close();
            }

        } catch (NullPointerException n) {
            Msg.msg(context1, "ERROR @ getBonusNumber with exception: " + n);
        }

        db.close();
        return number;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), DBmerchantHeroesHelper.TABLE_NAME);
    }

    /*
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
    */
    public int updateRow(int id, String newName) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBmerchantHeroesHelper.NAME, newName);
        String[] whereArgs = {id + ""};

        int validation = db.update(DBmerchantHeroesHelper.TABLE_NAME, cv, DBmerchantHeroesHelper.UID + " =? ", whereArgs);
        db.close();

        return validation;
    }

    public int updateImageResource(int id, String image) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBmerchantHeroesHelper.IMAGE_RESOURCE, image);
        
        String[] whereArgs = {id + ""};

        int validation = db.update(DBmerchantHeroesHelper.TABLE_NAME, cv, DBmerchantHeroesHelper.UID + " =? ", whereArgs);
        db.close();

        return validation;
    }

    public int updateRowComplete(int id,String newName,int hitpoints,String primaryClass,String secondaryClass, int costs, String image, int evasion, int hpTotal, int bonusNumber) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBmerchantHeroesHelper.NAME, newName);
        cv.put(DBmerchantHeroesHelper.HITPOINTS, hitpoints);
        cv.put(DBmerchantHeroesHelper.CLASS_ONE, primaryClass);
        cv.put(DBmerchantHeroesHelper.CLASS_TWO, secondaryClass);
        cv.put(DBmerchantHeroesHelper.COSTS, costs);
        cv.put(DBmerchantHeroesHelper.IMAGE_RESOURCE, image);
        cv.put(DBmerchantHeroesHelper.EVASION, evasion);
        cv.put(DBmerchantHeroesHelper.HP_TOTAL, hpTotal);
        cv.put(DBmerchantHeroesHelper.BONUS_NUMBER, bonusNumber);

        String[] whereArgs = {id + ""};

        int validation = db.update(DBmerchantHeroesHelper.TABLE_NAME, cv, DBmerchantHeroesHelper.UID + " =? ", whereArgs);
        db.close();

        return validation;
    }

    static class DBmerchantHeroesHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "merchantherosdatabase";
        private static final String TABLE_NAME = "MERCHANTTABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String HITPOINTS = "Hitpoints";
        private static final String CLASS_ONE = "ClasstypeOne";
        private static final String CLASS_TWO = "ClasstypeSecondary";
        private static final String COSTS = "Costs";
        private static final String IMAGE_RESOURCE = "ImageResource";
        private static final String EVASION = "Evasion";
        private static final String HP_TOTAL = "HpTotal";
        private static final String BONUS_NUMBER = "BonusNumber";
        // hier Spalten deklarieren, die für Helden benötigt werden
        // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), "
                + HITPOINTS + " INT, "
                + CLASS_ONE + " VARCHAR(255), "
                + CLASS_TWO + " VARCHAR(255), "
                + COSTS + " INT, "
                + IMAGE_RESOURCE + " VARCHAR(255), "
                + EVASION + " INTEGER, "
                + HP_TOTAL + " INTEGER, "
                + BONUS_NUMBER + " INTEGER);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        DBmerchantHeroesHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //super( Context der mitgegeben wird, String, custom cursor, version nr.)
            this.context = context;
            //com.example.thomas.voyage.ContainerClasses.Msg.msg(context, "constructor called");
            //com.example.thomas.voyage.ContainerClasses.Msg.msg(context, "MerchantDatabase constructor called");

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //nur wenn DATABASE erzeugt wird

            db.execSQL(CREATE_TABLE);
            Msg.msg(context, "MerchantDatabase onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Msg.msg(context, "MerchantDatabse onUpgrade called");
            Log.v("MERCHANT_UPGRADE", "merchant db upgraded");
        }
    }
}
