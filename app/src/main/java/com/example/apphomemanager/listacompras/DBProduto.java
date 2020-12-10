package com.example.apphomemanager.listacompras;

import java.io.Serializable;

public class DBProduto implements Serializable {

    private int id;
    private int Categoria;
    private String Nome;
    private float Quantidade;
    private int Unidade;
    private int Status;

    public DBProduto(){

    }

    public DBProduto(int id, int categoria, String nome, float quantidade, int unidade, int status) {
        this.id = id;
        this.Categoria = categoria;
        this.Nome = nome;
        this.Quantidade = quantidade;
        this.Unidade = unidade;
        this.Status = status;
    }

    public int getId() {
        return id;
    }

    public int getCategoria() {
        return Categoria;
    }

    public String getNome() {
        return Nome;
    }

    public float getQuantidade() {
        return Quantidade;
    }

    public int getUnidade() {
        return Unidade;
    }

    public int getStatus() {
        return Status;
    }

    @Override
    public boolean equals(Object o){
        return this.id == ((DBProduto)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoria(int categoria) {
        Categoria = categoria;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public void setQuantidade(float quantidade) {
        Quantidade = quantidade;
    }

    public void setUnidade(int unidade) {
        Unidade = unidade;
    }

    public void setStatus(int status) {
        Status = status;
    }
}