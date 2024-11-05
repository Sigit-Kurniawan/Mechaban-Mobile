package com.sigit.mechaban.object;

public class Account {
    private String action, email, emailUpdate, name, no_hp, password, photo, otp;
    private boolean updatePhoto;

    public void setAction(String action) {
        this.action = action;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailUpdate(String emailUpdate) {
        this.emailUpdate = emailUpdate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setUpdatePhoto(boolean updatePhoto) {
        this.updatePhoto = updatePhoto;
    }
}
