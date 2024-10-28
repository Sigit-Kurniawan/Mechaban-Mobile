package com.sigit.mechaban.object;

public class Account {
    private String email, name, no_hp, password;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Account(String email, String name, String no_hp, String password) {
        this.email = email;
        this.name = name;
        this.no_hp = no_hp;
        this.password = password;
    }
}
