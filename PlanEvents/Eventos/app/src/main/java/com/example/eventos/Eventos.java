package com.example.eventos;

public class Eventos {
    public String nome, localizacao, custo, hora_inicio, duracao, data,descricao,image,n_inscritos, n_maximo,id_criador;

    public Eventos() {
    }

    public Eventos(String nome, String localizacao, String custo, String hora_inicio, String duracao, String data, String descricao,String image, String n_inscritos, String n_maximo,String id_criador) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.custo = custo;
        this.hora_inicio = hora_inicio;
        this.duracao = duracao;
        this.data = data;
        this.descricao = descricao;
        this.image = image;
        this.n_inscritos = n_inscritos;
        this.n_maximo = n_maximo;
        this.id_criador = id_criador;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setCusto(String custo) {
        this.custo = custo;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public String getCusto() {
        return custo;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public String getDuracao() {
        return duracao;
    }

    public String getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getN_inscritos() {
        return n_inscritos;
    }

    public void setN_inscritos(String n_inscritos) {
        this.n_inscritos = n_inscritos;
    }

    public String getN_maximo() {
        return n_maximo;
    }

    public void setN_maximo(String n_maximo) {
        this.n_maximo = n_maximo;
    }

    public String getId_criador() {
        return id_criador;
    }

    public void setId_criador(String id_criador) {
        this.id_criador = id_criador;
    }
}
