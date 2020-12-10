package com.example.apphomemanager.listacompras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final String TABLE_PRODUTO = "Produto";
    private final String TABLE_UP_PRODUTO = "UpProduto";

    private ConstantsApp constants = new ConstantsApp();

    private DbGateway gw;

    public boolean updateFlagProduto(int id, int flag){
        ContentValues cv = new ContentValues();
        cv.put("upFlag", flag);
        if (id > 0)
            return gw.getDatabase().update(TABLE_UP_PRODUTO, cv, "ID=?", new String[]{id+""}) > 0;
        else
            return gw.getDatabase().insert(TABLE_UP_PRODUTO, null, cv) > 0;
    }

    public int getFlagProduto(int id){
        int upFlag = -1;

        try {
            Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM " + TABLE_UP_PRODUTO + " where ID = ?", new String[]{id+""}, null);
            while (cursor.moveToNext()) {
                upFlag = cursor.getInt(cursor.getColumnIndex("upFlag"));
            }
            cursor.close();
        }catch (Exception e){
            Log.println(Log.VERBOSE, "Teste: ", " Ex: " + e);
        }
        return upFlag;
    }

    public ProdutoDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public boolean saveList(ArrayList<DBProduto> list){

        try{
            for (DBProduto temp : list)
                saveItem(temp.getCategoria(), temp.getNome(), temp.getQuantidade(), temp.getUnidade(), temp.getStatus());

        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean saveItem(int categoria, String nome, float quantidade, int unidade, int status){
        ContentValues cv = new ContentValues();
        cv.put("Categoria", categoria);
        cv.put("Nome", nome);
        cv.put("Quantidade", quantidade);
        cv.put("Unidade", unidade);
        cv.put("Status", status);
        return gw.getDatabase().insert(TABLE_PRODUTO, null, cv) > 0;
    }

    public List<DBProduto> getListProduct(int mode, int value){
        List<DBProduto> produtos = new ArrayList<>();

        Cursor cursor = null;

        switch (mode){
            case -1:        //retorna dos os itens
                cursor = gw.getDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUTO, null);
                break;

            case 1:         //retorna itens por categoria
                cursor = gw.getDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUTO + " where Categoria = ?", new String[]{value+""}, null);
                break;

            case 2:         //retorna itens por status
                cursor = gw.getDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUTO + " where Status = ?", new String[]{value+""}, null);
                break;

            case 3:         //retorna itens por status - itens que estão fora da despensa
                cursor = gw.getDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUTO + " where Status = ? or Status = ?", new String[]{constants.getStatusOff()+"", constants.getStatusWait()+""}, null);
                break;
        }

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            int categoria = cursor.getInt(cursor.getColumnIndex("Categoria"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            float quantidade = cursor.getFloat(cursor.getColumnIndex("Quantidade"));
            int unidade = cursor.getInt(cursor.getColumnIndex("Unidade"));
            int status = cursor.getInt(cursor.getColumnIndex("Status"));
            produtos.add(new DBProduto(id, categoria, nome, quantidade, unidade, status));
        }
        cursor.close();
        return produtos;
    }

    public boolean updateProduto(DBProduto produto, int modo){
        ContentValues cv = new ContentValues();

        switch (modo){
            case 1:     //Atualiza o status do produto usando o ID
                cv.put("Status", produto.getStatus());
                return gw.getDatabase().update(TABLE_PRODUTO, cv, "ID=?", new String[]{produto.getId()+""}) > 0;

            case 2:     //Atualiza a quantidade, unidade e status usando o nome e a categoria
                cv.put("Quantidade", produto.getQuantidade());
                cv.put("Unidade", produto.getUnidade());
                cv.put("Status", produto.getStatus());
                return gw.getDatabase().update(TABLE_PRODUTO, cv, "Nome=? and Categoria=?", new String[]{produto.getNome()+"", produto.getCategoria()+""}) > 0;

            case 3:     //deixa todos os produtos disponíveis na despensa / fora da lista de compras
                cv.put("Status", constants.getStatusOn());
                return gw.getDatabase().update(TABLE_PRODUTO, cv, "", new String[]{}) > 0;
        }
        return false;
    }

    public boolean deleteProduto(int id){
        if (id > 0)
            return gw.getDatabase().delete(TABLE_PRODUTO, "ID=?", new String[]{id+""}) > 0;
        else
            return gw.getDatabase().delete(TABLE_PRODUTO, "", new String[]{}) > 0;
    }
}