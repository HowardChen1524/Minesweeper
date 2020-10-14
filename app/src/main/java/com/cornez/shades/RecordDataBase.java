package com.cornez.shades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class RecordDataBase extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Record";
    private static final String DATABASE_TABLE = "RecordTable";

    private static final String KEY_TASK_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIME = "time";


    public RecordDataBase (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_TASK_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_TIME + " INTEGER" + ")";
        db.execSQL (table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addRecord(RecordTable recordTable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //ADD KEY-VALUE PAIR INFORMATION FOR THE TASK DESCRIPTION
        values.put(KEY_TASK_ID, getCount()+1);

        //ADD KEY-VALUE PAIR INFORMATION FOR THE TASK DESCRIPTION
        values.put(KEY_NAME, recordTable.getName()); // task name

        //ADD KEY-VALUE PAIR INFORMATION FOR IS_DONE
        //  0- NOT DONE, 1 - IS DONE
        values.put(KEY_TIME, recordTable.getTime());

        // INSERT THE ROW IN THE TABLE
        db.insert(DATABASE_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public void editRecord(RecordTable recordTable){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, recordTable.getName());
        values.put(KEY_TIME, recordTable.getTime());

        db.update(DATABASE_TABLE, values, KEY_TASK_ID + " = ?",
                new String[]{
                        String.valueOf(recordTable.getId())
                });

        db.close();
    }

    public RecordTable getMaxTime(){
        ArrayList<RecordTable> records = getAllRecords();
        for(int j = 1; j < getCount(); j++)
            if(records.get(0).getTime() < records.get(j).getTime()) {
                RecordTable temp = new RecordTable(records.get(0));
                records.set(0, records.get(j));
                records.set(j, temp);
            }
        return records.get(0);
    }

    public RecordTable getMinTime(){
        ArrayList<RecordTable> records = getAllRecords();
        for(int j = 1; j < getCount(); j++)
            if(records.get(0).getTime() > records.get(j).getTime()) {
                RecordTable temp = new RecordTable(records.get(0));
                records.set(0, records.get(j));
                records.set(j, temp);
            }
        return records.get(0);
    }

    public ArrayList<RecordTable> getOrderRecords() {
        ArrayList<RecordTable> records = getAllRecords();
        for(int i = 0; i < getCount()-1; i++)
            for(int j = i; j < getCount(); j++)
                if(records.get(i).getTime() > records.get(j).getTime()) {
                    RecordTable temp = new RecordTable(records.get(i));
                    records.set(i,records.get(j));
                    records.set(j,temp);
                }

        return records;
    }

    public ArrayList<RecordTable> getAllRecords() {
        ArrayList<RecordTable> recordList = new ArrayList<>();
        String queryList = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(queryList, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                RecordTable record = new RecordTable();
                record.setId(cursor.getInt(0));
                record.setName(cursor.getString(1));
                record.setTime(cursor.getInt(2));

                //ADD TO THE QUERY LIST
                recordList.add(record);
            } while (cursor.moveToNext());
        }
        return recordList;
    }

    public int getCount(){
    String queryList = "SELECT * FROM " + DATABASE_TABLE;

    SQLiteDatabase database = this.getReadableDatabase();
    Cursor cursor = database.rawQuery(queryList, null);

    return cursor.getCount();
    }
}

