package dev.dispache.test_task.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UpdateEntireUserDto {

    @NotEmpty(message = "Email field cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "first_name field cannot be empty")
    @Pattern(
            regexp = "[A-Za-zа-яА-Я]{1,50}",
            message = "first_name field should consists only letters. Length is from 1 to 50 symbols"
    )
    private String first_name;

    @NotEmpty(message = "last_name field cannot be empty")
    @Pattern(
            regexp = "[A-Za-zа-яА-Я]{1,50}",
            message = "last_name field should consists only letters. Length is from 1 to 50 symbols"
    )
    private String last_name;

    @NotEmpty(message = "birth_date field cannot be empty")
    @Pattern(
            regexp = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}",
            message = "Correct birth_date format is: year-month-day. Example: 2024-04-25"
    )
    private String birth_date;

    @NotEmpty(message = "address field cannot be empty")
    private String address;

    @NotEmpty(message = "phone_number field cannot be empty")
    @Pattern(
            regexp = "[0-9]{1,50}",
            message = "phone_number field should consists only numbers. Length is from 1 to 50 symbols"
    )
    private String phone_number;

    public UpdateEntireUserDto(String email, String first_name, String last_name, String birth_date, String address, String phone_number) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.address = address;
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
