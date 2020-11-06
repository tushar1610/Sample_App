package com.example.android.sample_app;

public class Details {

    String first_name, last_name, email_address, url_to_avatar;

//    public Details(){}

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getUrl_to_avatar() {
        return url_to_avatar;
    }

    public void setUrl_to_avatar(String url_to_avatar) {
        this.url_to_avatar = url_to_avatar;
    }

    public Details(String email_address, String first_name, String last_name, String url_to_avatar) {
        this.email_address = email_address;
        this.first_name = first_name;
        this.last_name = last_name;
        this.url_to_avatar = url_to_avatar;
    }
}
