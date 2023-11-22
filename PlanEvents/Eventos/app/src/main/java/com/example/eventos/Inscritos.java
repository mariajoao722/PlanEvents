package com.example.eventos;

public class Inscritos {

    public String nome,userid,currentLoc,resposta;
    public int comecou;

    public Inscritos() {
    }

    public Inscritos(String nome,String userid,String currentLoc,int comecou,String resposta) {
        this.userid = userid;
        this.nome = nome;
        this.currentLoc = currentLoc;
        this.comecou = comecou;
        this.resposta = resposta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getid() {
        return userid;
    }

    public void setid(String userid) {
        this.userid = userid;
    }

    public String getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(String currentLoc) {
        this.currentLoc = currentLoc;
    }

    public int getComecou() {
        return comecou;
    }

    public void setComecou(int comecou) {
        this.comecou = comecou;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
