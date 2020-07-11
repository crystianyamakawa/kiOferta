package com.kioferta.model;

import com.orm.SugarRecord;

import java.util.Date;

public class Oferta extends SugarRecord {

    private String chave;
    private String fornecedor;
    private String setor;
    private String titulo;
    private String descricao;
    private String imagem;
    private Boolean ativo;
    //private Date dataInicio = new Date();
    //private Date dataTermino = new Date();


    public Oferta(String chave,  String fornecedor, String setor, String titulo, String descricao, String imagem) {
        this.chave = chave;
        this.fornecedor = fornecedor;
        this.setor = setor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;

    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public Oferta() {
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }


}
