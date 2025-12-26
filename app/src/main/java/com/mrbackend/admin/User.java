package com.mrbackend.admin;

public class User {
    public int id;
    public String name;
    public String surname;
    public String email;
    public String score;
    public String created_at;

    public User(int id, String name, String surname, String email, String score, String created_at){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.score = score;
        this.created_at = created_at;
    }
}
