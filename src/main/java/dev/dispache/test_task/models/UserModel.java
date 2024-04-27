package dev.dispache.test_task.models;

import java.time.LocalDate;

public class UserModel {

    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private LocalDate birth_date;
    private String address;
    private String phone_number;

    public UserModel() {}

    public UserModel(String email, String first_name, String last_name, String birth_date, String address, String phone_number) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        String[] dateItems = birth_date.split("-");
        this.birth_date = LocalDate.of(Integer.parseInt(dateItems[0]), Integer.parseInt(dateItems[1]), Integer.parseInt(dateItems[2]));
        this.address = address;
        this.phone_number = phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public void setBirth_date(String birth_date) {
        String[] birth_date_items = birth_date.split("-");
        this.birth_date = LocalDate.of(
                Integer.parseInt(birth_date_items[0]),
                Integer.parseInt(birth_date_items[1]),
                Integer.parseInt(birth_date_items[2])
        );
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String toString() {
        return String.format(
                "%s - %s - %s - %s - %s - %s - %s",
                this.id, this.email, this.first_name, this.last_name, this.birth_date, this.address, this.phone_number
        );
    }

    public boolean equals(Object comparable) {
        if (comparable.getClass() != this.getClass()) {
            return false;
        }
        return this.id == ((UserModel) comparable).getId()
                && this.email.equals(((UserModel) comparable).getEmail())
                && this.first_name.equals(((UserModel) comparable).getFirst_name())
                && this.last_name.equals(((UserModel) comparable).getLast_name())
                && this.birth_date.isEqual(((UserModel) comparable).getBirth_date())
                && this.address.equals(((UserModel) comparable).getAddress())
                && this.phone_number.equals(((UserModel) comparable).getPhone_number());
    }
}
