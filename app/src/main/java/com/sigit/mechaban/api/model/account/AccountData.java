package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

public class AccountData {
    @SerializedName("email")
    private final String email;

    @SerializedName("name")
    private final String name;

    @SerializedName("no_hp")
    private final String noHp;

    @SerializedName("password")
    private final String password;

    @SerializedName("role")
    private final String role;

    @SerializedName("photo")
    private final String photo;

    public AccountData(String email, String name, String noHp, String password, String role, String photo) {
        this.email = email;
        this.name = name;
        this.noHp = noHp;
        this.password = password;
        this.role = role;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPhoto() {
        return photo;
    }
}
