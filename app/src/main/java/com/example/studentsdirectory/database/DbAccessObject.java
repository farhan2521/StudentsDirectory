package com.example.studentsdirectory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentsdirectory.database.DetailsContract.DetailsEntry;
import com.example.studentsdirectory.pojo.StudentDetails;

import java.util.ArrayList;

public class DbAccessObject {
    SQLiteDatabase studentdb;
    DbHelper dbHelper;

    public DbAccessObject(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void openDb() {
        studentdb = dbHelper.getWritableDatabase();
    }

    public void closeDb(){
        studentdb.close();
    }

    public void createRow(String name, String dob, String dept) {
        ContentValues values = new ContentValues();
        values.put(DetailsEntry.COLUMN_NAME_STUDENT,name);
        values.put(DetailsEntry.COLUMN_NAME_DOB,dob);
        values.put(DetailsEntry.COLUMN_NAME_DEPARTMENT,dept);
        studentdb.insert(DetailsEntry.TABLE_NAME,null,values);
    }

    public String readRow() {
        Cursor cursor = studentdb.query(DetailsEntry.TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndexOrThrow(DetailsEntry.COLUMN_NAME_STUDENT); //index = 1
        int dobIndex = cursor.getColumnIndexOrThrow(DetailsEntry.COLUMN_NAME_DOB); //index = 2
        int deptIndex = cursor.getColumnIndexOrThrow(DetailsEntry.COLUMN_NAME_DEPARTMENT); //index = 3
        return cursor.getString(nameIndex) + "\n"+cursor.getString(dobIndex) + "\n" +cursor.getString(deptIndex);
    }

    public void updateRow(String name, String dob, String dept, int id){
        ContentValues values = new ContentValues();
        if(!name.isEmpty())
            values.put(DetailsEntry.COLUMN_NAME_STUDENT,name);
        if(!dob.isEmpty())
            values.put(DetailsEntry.COLUMN_NAME_DOB,dob);
        if(!dept.isEmpty())
            values.put(DetailsEntry.COLUMN_NAME_DEPARTMENT,dept);
        studentdb.update(DetailsEntry.TABLE_NAME,values,DetailsEntry._ID + " = ?",new String[]{String.valueOf(id)});
    }

    public void deleteRow(int id){
        studentdb.delete(DetailsEntry.TABLE_NAME,DetailsEntry._ID + " = ?",new String[]{String.valueOf(id)});
    }

    public Cursor searchRow(String keyword){
        String sql = "select * from " + DetailsEntry.TABLE_NAME + " where " + DetailsEntry.COLUMN_NAME_STUDENT + " = " + DetailsEntry.COLUMN_NAME_STUDENT + " AND " + DetailsEntry.COLUMN_NAME_STUDENT + " LIKE '" + keyword + "%'";
        Cursor cursor = studentdb.rawQuery(sql, null);
        return cursor;
    }

    public Cursor getAllRows(){
        Cursor cursor = studentdb.query(DetailsEntry.TABLE_NAME,null,null,null,null,null,null);
        return  cursor;
    }

    public ArrayList<StudentDetails> getStudentDetails(){
        String sql = "select * from " + DetailsEntry.TABLE_NAME;
        ArrayList<StudentDetails> studentDetails = new ArrayList<>();
        Cursor cursor = studentdb.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String dob = cursor.getString(2);
                String department = cursor.getString(3);
                studentDetails.add(new StudentDetails(id,name,dob,department));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return studentDetails;
    }
}