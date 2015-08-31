package com.example.thomas.voyage.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Message;

public class DBghostMetaDataAdapter {

    MetaHelper helper;
    Context c;

    public DBghostMetaDataAdapter(Context context) {
        helper = new MetaHelper(context);
        helper.getWritableDatabase();
        c = context;
    }

    public long insertData(int rowId, String name, int pointsPerLeg, int numLegs, int count, float avg, String date) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(MetaHelper.VAL, rowId);
        contentValues.put(MetaHelper.NAME, name);
        contentValues.put(MetaHelper.POINTS_PER_LEG, pointsPerLeg);
        contentValues.put(MetaHelper.NUM_LEGS, numLegs);
        contentValues.put(MetaHelper.THROW_COUNT, count);
        contentValues.put(MetaHelper.THROW_AVG, avg);
        contentValues.put(MetaHelper.DATE, date);

        long id = db.insert(MetaHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;
    }

    public long getTaskCount() {
        return DatabaseUtils.queryNumEntries(helper.getReadableDatabase(), MetaHelper.TABLE_NAME);
    }

    public String getOneRow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {MetaHelper.NAME, MetaHelper.POINTS_PER_LEG,MetaHelper.NUM_LEGS,MetaHelper.THROW_COUNT, MetaHelper.THROW_AVG, MetaHelper.DATE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(MetaHelper.TABLE_NAME, columns, MetaHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        StringBuilder buffer = new StringBuilder();

        try {
            int indexName = cursor.getColumnIndex(MetaHelper.NAME);
            int indexPointsPerLeg = cursor.getColumnIndex(MetaHelper.POINTS_PER_LEG);
            int indexNumLegs = cursor.getColumnIndex(MetaHelper.NUM_LEGS);
            int indexCount = cursor.getColumnIndex(MetaHelper.THROW_COUNT);
            int indexAvg = cursor.getColumnIndex(MetaHelper.THROW_AVG);
            int indexDate = cursor.getColumnIndex(MetaHelper.DATE);

            String name = cursor.getString(indexName);
            int pointsPerLeg = cursor.getInt(indexPointsPerLeg);
            int numLegs = cursor.getInt(indexNumLegs);
            int count = cursor.getInt(indexCount);
            float avg = cursor.getFloat(indexAvg);
            String date = cursor.getString(indexDate);

            buffer.append(name + ", mit  " + count + " Würfen (" + pointsPerLeg + " / " + numLegs + " Runden / Ø: " + avg + "), Datum: " + date);

        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getThrowCount with exception: " + n);
        }

        cursor.close();
        db.close();

        return buffer.toString();
    }

    public int getThrowCount(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {MetaHelper.THROW_COUNT};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(MetaHelper.TABLE_NAME, columns, MetaHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(MetaHelper.THROW_COUNT));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getThrowCount with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getPointsPerLeg(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {MetaHelper.POINTS_PER_LEG};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(MetaHelper.TABLE_NAME, columns, MetaHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(MetaHelper.POINTS_PER_LEG));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getThrowCount with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public int getNumLegs(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {MetaHelper.NUM_LEGS};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(MetaHelper.TABLE_NAME, columns, MetaHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int value = -1;

        try {
            value = cursor.getInt(cursor.getColumnIndex(MetaHelper.NUM_LEGS));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getThrowCount with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public float getPointsAveragePerThrow(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {MetaHelper.THROW_AVG};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(MetaHelper.TABLE_NAME, columns, MetaHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        float value = -1;

        try {
            value = cursor.getFloat(cursor.getColumnIndex(MetaHelper.THROW_AVG));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getPointsAveragePerThrow with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getDate(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {MetaHelper.DATE};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(MetaHelper.TABLE_NAME, columns, MetaHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "-1";

        try {
            value = cursor.getString(cursor.getColumnIndex(MetaHelper.DATE));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getDate with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    public String getName(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {MetaHelper.NAME};
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(MetaHelper.TABLE_NAME, columns, MetaHelper.VAL + "=?", selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String value = "-1";

        try {
            value = cursor.getString(cursor.getColumnIndex(MetaHelper.NAME));
        } catch (NullPointerException n) {
            Message.message(c, "ERROR @ getName with exception: " + n);
        }

        cursor.close();
        db.close();

        return value;
    }

    static class MetaHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "ghostmetadatabase";
        private static final String TABLE_NAME = "SCORE_MULTI_AMOUNT_TABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String VAL = "Val";
        private static final String NAME = "ThrowId";
        private static final String POINTS_PER_LEG = "PointsPerLeg";
        private static final String NUM_LEGS = "NumLegs";
        private static final String THROW_COUNT = "First";
        private static final String THROW_AVG = "Second";
        private static final String DATE = "Third";
            // hier Spalten deklarieren, die für Helden benötigt werden
            // -> diese dann in CREATE_TABLE unterhalb einfügen

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VAL + " INT, "
                + NAME + " VARCHAR(255), "
                + POINTS_PER_LEG + " INT, "
                + NUM_LEGS + " INT, "
                + THROW_COUNT + " INT, "
                + THROW_AVG + " REAL, "
                + DATE + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        MetaHelper(Context context){
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
            Message.message(context, "MetaHelper onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            db.execSQL(DROP_TABLE);
            onCreate(db);
            Message.message(context, "MetaHelper onUpgrade called");
            Log.v("HEROES UPGRADE", "heroes db upgraded");
        }
    }
}
