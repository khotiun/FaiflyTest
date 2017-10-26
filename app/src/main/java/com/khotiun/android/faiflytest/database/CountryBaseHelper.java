package com.khotiun.android.faiflytest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CountryBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "CountryBase.db";

    public CountryBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CountryDbSchema.CountryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CountryDbSchema.CountryTable.Cols.COUNTRY + ", " +
                CountryDbSchema.CountryTable.Cols.CITY + ")"
        );
    }

    //upgrade db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + CountryDbSchema.CountryTable.NAME);
                onCreate(db);
    }
}
