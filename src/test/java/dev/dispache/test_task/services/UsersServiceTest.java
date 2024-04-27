package dev.dispache.test_task.services;

import dev.dispache.test_task.dtos.CreateUserDto;
import dev.dispache.test_task.dtos.UpdateEntireUserDto;
import dev.dispache.test_task.dtos.UpdateOneSomeUserFieldsDto;
import dev.dispache.test_task.exceptions.FromDateGreaterThanToDateException;
import dev.dispache.test_task.exceptions.NotAllowedUserAgeException;
import dev.dispache.test_task.exceptions.UserNotFoundException;
import dev.dispache.test_task.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersServiceTest {

    private final UsersService usersService = new UsersService();

    final String email = "created@gmail.com";
    final String first_name = "Name";
    final String last_name = "Surname";
    final String birth_date = "1991-12-12";
    final String address = "Spain, Barcelona";
    final String phone_number = "1111";

    @BeforeEach()
    public void setup() {
        this.usersService.setAllowed_age(18);
        fullfillDatabase();
    }

    private void fullfillDatabase() {
        this.usersService.createUser(new CreateUserDto(
           "test1@gmail.com", "First", "First", "1995-05-05", "UK, London", "1234"
        ));
        this.usersService.createUser(new CreateUserDto(
           "test2@gmail.com", "Second", "Second", "2000-06-06", "Ukraine, Kyiv", "5678"
        ));
        this.usersService.createUser(new CreateUserDto(
            "test3@gmail.com", "Third", "Third", "1999-07-07", "United States, Chicago", "1010"
        ));
    }

    @Test
    public void shouldCreateNewUserAndReturnIt() {
        final CreateUserDto createUserDto = new CreateUserDto(
                email, first_name, last_name, birth_date, address, phone_number
        );
        final UserModel result = this.usersService.createUser(createUserDto);

        final UserModel expected = new UserModel(
                email, first_name, last_name, birth_date, address, phone_number
        );
        expected.setId(3);

        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldThrowAnExceptionBecauseOfDeniedRegistrationAge() {
        final String birth_date = "2020-06-06";

        final CreateUserDto createUserDto = new CreateUserDto(email, first_name, last_name, birth_date, address, phone_number);

        NotAllowedUserAgeException exception = assertThrows(NotAllowedUserAgeException.class, () -> {
            this.usersService.createUser(createUserDto);
        });

        final String expected_exception_msg = "The lowest allowed registration age is 18";

        assertEquals(expected_exception_msg, exception.getMessage());
    }

    @Test
    public void shouldUpdateOneUserFieldAndReturnUpdatedUser() {

        final int userId = 0;
        final String first_name = "Updated first_name";

        final UpdateOneSomeUserFieldsDto updateOneSomeUserFieldsDto = new UpdateOneSomeUserFieldsDto();
        updateOneSomeUserFieldsDto.setFirst_name(first_name);

        final UserModel result = this.usersService.updateOneSomeUserFields(userId, updateOneSomeUserFieldsDto);

        assertEquals(first_name, result.getFirst_name());

    }

    @Test
    public void shouldUpdateAllUserFields() {
        final int userId = 0;

        final UserModel user = this.usersService.getUserById(userId);

        final String old_user_email = user.getEmail();
        final String old_user_first_name = user.getFirst_name();
        final String old_user_last_name = user.getLast_name();
        final LocalDate old_user_birth_date = user.getBirth_date();
        final String old_user_address = user.getAddress();
        final String old_user_phone_number = user.getPhone_number();

        UpdateEntireUserDto updateEntireUserDto = new UpdateEntireUserDto(
                "updated@gmail.com", "haha", "hoho", "1111-11-11", "Canada, Toronto", "999"
        );
        final UserModel result = this.usersService.updateEntireUser(userId, updateEntireUserDto);

        assertEquals(user.getId(), result.getId());
        assertNotEquals(old_user_email, result.getEmail());
        assertNotEquals(old_user_first_name, result.getFirst_name());
        assertNotEquals(old_user_last_name, result.getLast_name());
        assertNotEquals(old_user_birth_date, result.getBirth_date());
        assertNotEquals(old_user_address, result.getAddress());
        assertNotEquals(old_user_phone_number, result.getPhone_number());
    }

    @Test
    public void shouldDeleteUserByIdAndReturnIt() {
        final int userId = 0;
        final UserModel deleted = this.usersService.deleteUser(userId);

        final UserModel expected = new UserModel("test1@gmail.com", "First", "First", "1995-05-05", "UK, London", "1234");

        assertThat(deleted).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    public void shouldThrowUserNotFoundException() {
        final int userId = 1111;

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            final UserModel user = this.usersService.deleteUser(userId);
        });

        final String expected_msg = "User with id=1111 was not found";

        assertEquals(expected_msg, exception.getMessage());
    }

    @Test
    public void shouldReturnOneUserWhereBirthDateIsBetweenFromAndToRange() {
        final String from = "1997-03-03";
        final String to = "2000-05-05";

        final String expected_first_name = "Third";

        final List<UserModel> result = this.usersService.getUsersByBirthDateRange(from, to);

        assertThat(result.size()).isEqualTo(1);
        assertEquals(expected_first_name, result.get(0).getFirst_name());
    }

    @Test
    public void shouldThrowFromDateGreaterThanToDateException() {
        final String from = "2000-05-05";
        final String to = "1980-06-06";

        FromDateGreaterThanToDateException exception = assertThrows(FromDateGreaterThanToDateException.class, () -> {
            this.usersService.getUsersByBirthDateRange(from, to);
        });

        final String expected_msg = "'from' request parameter is greater than 'to' request parameter";

        assertThat(expected_msg).isEqualTo(exception.getMessage());
    }
}
