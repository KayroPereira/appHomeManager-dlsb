package com.example.apphomemanager.listacompras;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbListaCompras.db";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_PRODUTO =   "CREATE TABLE Produto (ID INTEGER PRIMARY KEY AUTOINCREMENT, Categoria INTEGER NOT NULL, " +
                                            "Nome TEXT NOT NULL, Quantidade REAL NOT NULL, Unidade INTEGER NOT NULL, Status INTEGER NOT NULL);";
    private final String CREATE_UP_PRODUTO =  "CREATE TABLE UpProduto (ID INTEGER PRIMARY KEY AUTOINCREMENT, upFlag INTEGER NOT NULL);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUTO);
        db.execSQL(CREATE_UP_PRODUTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}