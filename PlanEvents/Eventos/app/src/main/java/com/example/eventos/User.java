package com.example.eventos;

public class User {
    public String nome;
    public String idade;
    public String telefone;
    public String email;
    public int is_admin;
    public int is_monitor;

    public User() {
    }

    public User(String nome, String idade, String telefone, String email, int is_admin, int is_monitor) {
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
        this.email = email;
        this.is_admin = is_admin;
        this.is_monitor = is_monitor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public int getIs_monitor() {
        return is_monitor;
    }

    public void setIs_monitor(int is_monitor) {
        this.is_monitor = is_monitor;
    }
}
