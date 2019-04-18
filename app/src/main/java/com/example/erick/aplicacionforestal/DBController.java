package com.example.erick.aplicacionforestal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBController extends SQLiteOpenHelper{
    public DBController(Context applicationcontext) {
        super(applicationcontext, "CubicacionDB", null, 19);
    }
    //Create Tables
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE Huber (idcalculo INTEGER PRIMARY KEY , diametro1 TEXT, diametro2 TEXT, longitud TEXT, volumen TEXT, unidad TEXT, usuario TEXT, fecha DATE, hora TEXT, condicion TEXT, udpateStatus TEXT)";
        database.execSQL(query);
        database.execSQL("CREATE TABLE 'Persona'('IDUser' INTEGER PRIMARY KEY, 'Nombre' TEXT, 'Numero' TEXT, 'Password' TEXT)");
        database.execSQL("CREATE TABLE Heyer (idcalculo INTEGER PRIMARY KEY , diametros TEXT, longitudes TEXT, volumen TEXT, unidad TEXT, usuario TEXT, fecha DATE, hora TEXT, condicion TEXT, udpateStatus TEXT)");
        database.execSQL("CREATE TABLE Newton (idcalculo INTEGER PRIMARY KEY , diametro1 TEXT, diametro2 TEXT, longitud TEXT, volumen TEXT, unidad TEXT, usuario TEXT, fecha DATE, hora TEXT, condicion TEXT,udpateStatus TEXT)");
        database.execSQL("CREATE TABLE Simpson (idcalculo INTEGER PRIMARY KEY , diametroSe TEXT, longitud TEXT, volumen TEXT, unidad TEXT, usuario TEXT, fecha DATE, hora TEXT, condicion TEXT, udpateStatus TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Huber";
        database.execSQL(query);
        database.execSQL("DROP TABLE IF EXISTS Persona");
        database.execSQL("DROP TABLE IF EXISTS Heyer");
        database.execSQL("DROP TABLE IF EXISTS Newton");
        database.execSQL("DROP TABLE IF EXISTS Simpson");
        onCreate(database);
    }
    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertHuber(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("diametro1", queryValues.get("diametro1"));
        values.put("diametro2", queryValues.get("diametro2"));
        values.put("longitud",queryValues.get("longitud"));
        values.put("volumen", queryValues.get("volumen"));
        values.put("unidad", queryValues.get("unidad"));
        values.put("usuario", queryValues.get("usuario"));
        values.put("fecha", queryValues.get("fecha"));
        values.put("hora", queryValues.get("hora"));
        values.put("condicion", "1");
        values.put("udpateStatus", "no");
        database.insert("Huber", null, values);
        database.close();
    }

    public void insertHeyer(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("diametros", queryValues.get("diametros"));
        values.put("longitudes", queryValues.get("longitudes"));
        values.put("volumen", queryValues.get("volumen"));
        values.put("unidad", queryValues.get("unidad"));
        values.put("usuario", queryValues.get("usuario"));
        values.put("fecha", queryValues.get("fecha"));
        values.put("hora", queryValues.get("hora"));
        values.put("condicion", "1");
        values.put("udpateStatus", "no");
        database.insert("Heyer", null, values);
        database.close();
    }

    public void insertNewton(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("diametro1", queryValues.get("diametro1"));
        values.put("diametro2", queryValues.get("diametro2"));
        values.put("longitud",queryValues.get("longitud"));
        values.put("volumen", queryValues.get("volumen"));
        values.put("unidad", queryValues.get("unidad"));
        values.put("usuario", queryValues.get("usuario"));
        values.put("fecha", queryValues.get("fecha"));
        values.put("hora", queryValues.get("hora"));
        values.put("condicion", "1");
        values.put("udpateStatus", "no");
        database.insert("Newton", null, values);
        database.close();
    }

    public void insertSimpson(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("diametroSe", queryValues.get("diametroSe"));
        values.put("longitud", queryValues.get("longitud"));
        values.put("volumen", queryValues.get("volumen"));
        values.put("unidad", queryValues.get("unidad"));
        values.put("usuario", queryValues.get("usuario"));
        values.put("fecha", queryValues.get("fecha"));
        values.put("hora", queryValues.get("hora"));
        values.put("condicion", "1");
        values.put("udpateStatus", "no");
        database.insert("Simpson", null, values);
        database.close();
    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Huber";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametro1", cursor.getString(1));
                map.put("diametro2", cursor.getString(2));
                map.put("longitud", cursor.getString(3));
                map.put("volumen", cursor.getString(4));
                map.put("unidad", cursor.getString(5));
                map.put("usuario", cursor.getString(6));
                map.put("fecha", cursor.getString(7));
                map.put("hora", cursor.getString(8));
                map.put("condicion", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllHeyer() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Heyer";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametros", cursor.getString(1));
                map.put("longitudes", cursor.getString(2));
                map.put("volumen", cursor.getString(3));
                map.put("unidad", cursor.getString(4));
                map.put("usuario", cursor.getString(5));
                map.put("fecha", cursor.getString(6));
                map.put("hora", cursor.getString(7));
                map.put("condicion", cursor.getString(8));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllNewton() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Newton";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametro1", cursor.getString(1));
                map.put("diametro2", cursor.getString(2));
                map.put("longitud", cursor.getString(3));
                map.put("volumen", cursor.getString(4));
                map.put("unidad", cursor.getString(5));
                map.put("usuario", cursor.getString(6));
                map.put("fecha", cursor.getString(7));
                map.put("hora", cursor.getString(8));
                map.put("condicion", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllSimpson() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Simpson";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametroSe", cursor.getString(1));
                map.put("longitud", cursor.getString(2));
                map.put("volumen", cursor.getString(3));
                map.put("unidad", cursor.getString(4));
                map.put("usuario", cursor.getString(5));
                map.put("fecha", cursor.getString(6));
                map.put("hora", cursor.getString(7));
                map.put("condicion", cursor.getString(8));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }

    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Huber WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametro1", cursor.getString(1));
                map.put("diametro2", cursor.getString(2));
                map.put("longitud", cursor.getString(3));
                map.put("volumen", cursor.getString(4));
                map.put("unidad", cursor.getString(5));
                map.put("usuario", cursor.getString(6));
                map.put("fecha", cursor.getString(7));
                map.put("hora", cursor.getString(8));
                map.put("condicion", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public String composeJSONfromSQLiteHe(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Heyer WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametros", cursor.getString(1));
                map.put("longitudes", cursor.getString(2));
                map.put("volumen", cursor.getString(3));
                map.put("unidad", cursor.getString(4));
                map.put("usuario", cursor.getString(5));
                map.put("fecha", cursor.getString(6));
                map.put("hora", cursor.getString(7));
                map.put("condicion", cursor.getString(8));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public String composeJSONfromSQLiteNe(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Newton WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametro1", cursor.getString(1));
                map.put("diametro2", cursor.getString(2));
                map.put("longitud", cursor.getString(3));
                map.put("volumen", cursor.getString(4));
                map.put("unidad", cursor.getString(5));
                map.put("usuario", cursor.getString(6));
                map.put("fecha", cursor.getString(7));
                map.put("hora", cursor.getString(8));
                map.put("condicion", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public String composeJSONfromSQLiteSi(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Simpson WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("idcalculo", cursor.getString(0));
                map.put("diametroSe", cursor.getString(1));
                map.put("longitud", cursor.getString(2));
                map.put("volumen", cursor.getString(3));
                map.put("unidad", cursor.getString(4));
                map.put("usuario", cursor.getString(5));
                map.put("fecha", cursor.getString(6));
                map.put("hora", cursor.getString(7));
                map.put("condicion", cursor.getString(8));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT * FROM Huber WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    public int dbSyncCountHe(){
        int count = 0;
        String selectQuery = "SELECT * FROM Heyer WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    public int dbSyncCountNe(){
        int count = 0;
        String selectQuery = "SELECT * FROM Newton WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    public int dbSyncCountSi(){
        int count = 0;
        String selectQuery = "SELECT * FROM Simpson WHERE udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void updateSyncStatus(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE Huber SET udpateStatus = '"+ status +"' WHERE idcalculo="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void updateSyncStatusHe(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE Heyer SET udpateStatus = '"+ status +"' WHERE idcalculo="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void updateSyncStatusNe(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE Newton SET udpateStatus = '"+ status +"' WHERE idcalculo="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void updateSyncStatusSi(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE Simpson SET udpateStatus = '"+ status +"' WHERE idcalculo="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public List<NewtonModel> mostrarRecordsNe(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Newton", null);
        List<NewtonModel> newtons = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                newtons.add(new NewtonModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        return newtons;
    }

    public List<HeyerModel> mostrarRecordsHe(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Heyer", null);
        List<HeyerModel> heyers = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                heyers.add(new HeyerModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
            }while (cursor.moveToNext());
        }
        return heyers;
    }

    public List<HuberModel> mostrarRecordsHu(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Huber", null);
        List<HuberModel> hubers = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                hubers.add(new HuberModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        return hubers;
    }

    public List<SimpsonModel> mostrarRecordsSimpson(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Simpson", null);
        List<SimpsonModel> simpsons = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                simpsons.add(new SimpsonModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
            }while (cursor.moveToNext());
        }
        return simpsons;
    }
}
