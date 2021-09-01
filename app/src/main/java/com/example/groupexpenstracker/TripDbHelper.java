package com.example.groupexpenstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.io.File;

public class TripDbHelper extends SQLiteOpenHelper {

    public TripDbHelper(Context context) {
        super(context, "Expenses.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Persons (PersonID INTEGER PRIMARY KEY AUTOINCREMENT, PersonName TEXT NOT NULL, TripName TEXT )");
        db.execSQL("create table Expense ( ExpenseID INTEGER PRIMARY KEY AUTOINCREMENT, ExpenseType TEXT NOT NULL , Value INTEGER NOT NULL , PersonID INTEGER NOT NULL, FOREIGN KEY(PersonID) REFERENCES Persons(PersonID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Persons");
        db.execSQL("DROP TABLE IF EXISTS Expense");
        onCreate(db);
    }

    public boolean insertDataPersons(String PersonName,String TripName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PersonName",PersonName);
        contentValues.put("TripName",TripName);
        long res = db.insert("Persons",null,contentValues);
        if(res==-1)
            return false;
        else
            return true;
    }

    public boolean insertDataExpense(String ExpenseType,String Value,String PersonID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ExpenseType",ExpenseType);
        contentValues.put("Value",Value);
        contentValues.put("PersonID",PersonID);
        long res = db.insert("Expense",null,contentValues);
        if(res==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllPerson() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Persons",null);
        return res;
    }

    public Cursor getAllExpense() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Expense", null);
        return res;
    }

    public Cursor getExpensebyId(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Expense"+" where PersonID = "+Id,null);
        return res;
    }
    public Cursor getExpenseType() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ExpenseType from Expense",null);
        return res;
    }
    public Cursor getExpenseTypeByName(String Type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Expense"+" where ExpenseType="+"'"+Type+"'",null);
        return res;
    }
    public Cursor getValueByExpenseNameAndId(String ExpName,String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Expense"+" where ExpenseType="+"'"+ExpName+"' and PersonID="+"'"+Id+"'",null);
        return res;
    }

    public boolean updateByIdAndExpense(String ExpName,String Id,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Value",value);
        long res = db.update("Expense",contentValues,"ExpenseType='"+ExpName+"' and PersonID='"+Id+"'",null);
        if(res==-1)
            return false;
        else
            return true;
    }

    public boolean dropDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        File file = new File(db.getPath());
        if(SQLiteDatabase.deleteDatabase(file))
            return true;
        else
            return false;
    }
}
