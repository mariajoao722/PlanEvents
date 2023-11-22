package com.example.eventos;

public class Lugar {

    public String nomel, localizacao, descricao, tarefa, penalizacao, pontos, obrigatorio,image;

    public Lugar() {
    }

    public Lugar( String nomel,String localizacao, String descricao, String tarefa, String penalizacao, String pontos, String obrigatorio,String image) {

        this.image = image;
        this.nomel = nomel;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.tarefa = tarefa;
        this.penalizacao = penalizacao;
        this.pontos = pontos;
        this.obrigatorio = obrigatorio;
    }

    public String getNomel() {
        return nomel;
    }

    public void setNomel(String nomel) {
        this.nomel = nomel;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTarefa() {
        return tarefa;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    public String getPenalizacao() {
        return penalizacao;
    }

    public void setPenalizacao(String penalizacao) {
        this.penalizacao = penalizacao;
    }

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }

    public String getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(String obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
