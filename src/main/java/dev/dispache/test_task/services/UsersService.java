package dev.dispache.test_task.services;

import dev.dispache.test_task.dtos.CreateUserDto;
import dev.dispache.test_task.dtos.UpdateEntireUserDto;
import dev.dispache.test_task.dtos.UpdateOneSomeUserFieldsDto;
import dev.dispache.test_task.exceptions.FromDateGreaterThanToDateException;
import dev.dispache.test_task.exceptions.NotAllowedUserAgeException;
import dev.dispache.test_task.exceptions.UserNotFoundException;
import dev.dispache.test_task.models.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    // fake in-memory database instead of using real database
    private final List<UserModel> users = new ArrayList<>();

    @Value("${users.allowed_age}")
    private Integer allowed_age;

    public void setAllowed_age(Integer allowed_age) {
        this.allowed_age = allowed_age;
    }

    public UserModel getUserById(int userId) {
        for (UserModel value : users) {
            if (value.getId() == userId) {
                return value;
            }
        }
        return null;
    }

    public UserModel createUser(CreateUserDto createUserDto) throws NotAllowedUserAgeException {
        final String[] datesItems = createUserDto.getBirth_date().split("-"); // 2024-04-25
        final int year = Integer.parseInt(datesItems[0]);
        final int month = Integer.parseInt(datesItems[1]);
        final int day = Integer.parseInt(datesItems[2]);
        final LocalDate birth_date = LocalDate.of(year, month, day);

        final LocalDate current_date = LocalDate.now();
        final int user_age = Period.between(birth_date, current_date).getYears();

        if (user_age < allowed_age) {
            throw new NotAllowedUserAgeException();
        }

        final UserModel user = new UserModel(
                createUserDto.getEmail(),
                createUserDto.getFirst_name(),
                createUserDto.getLast_name(),
                createUserDto.getBirth_date(),
                createUserDto.getAddress(),
                createUserDto.getPhone_number()
        );
        user.setId(this.users.size());

        this.users.add(user);
        return user;
    }

    public UserModel updateOneSomeUserFields(
            int userId, UpdateOneSomeUserFieldsDto updateOneSomeUserFieldsDto
    ) throws UserNotFoundException {
        final UserModel user = this.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }
        final String email = updateOneSomeUserFieldsDto.getEmail();
        final String first_name = updateOneSomeUserFieldsDto.getFirst_name();
        final String last_name = updateOneSomeUserFieldsDto.getLast_name();
        LocalDate birth_date = null;
        if (updateOneSomeUserFieldsDto.getBirth_date() != null) {
            final String[] datesItems = updateOneSomeUserFieldsDto.getBirth_date().split("-");
            birth_date = LocalDate.of(
                    Integer.parseInt(datesItems[0]),
                    Integer.parseInt(datesItems[1]),
                    Integer.parseInt(datesItems[2])
            );
        }
        final String address = updateOneSomeUserFieldsDto.getAddress();
        final String phone_number = updateOneSomeUserFieldsDto.getPhone_number();

        if (email != null) user.setEmail(email);
        if (first_name != null) user.setFirst_name(first_name);
        if (last_name != null) user.setLast_name(last_name);
        if (birth_date != null) user.setBirth_date(birth_date);
        if (address != null) user.setAddress(address);
        if (phone_number != null) user.setPhone_number(phone_number);

        return user;
    }

    public UserModel updateEntireUser(int userId, UpdateEntireUserDto updateEntireUserDto) throws UserNotFoundException{
        final UserModel user = this.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }
        final String[] datesItems = updateEntireUserDto.getBirth_date().split("-");
        final LocalDate birth_date = LocalDate.of(
                Integer.parseInt(datesItems[0]),
                Integer.parseInt(datesItems[1]),
                Integer.parseInt(datesItems[2])
        );
        user.setEmail(updateEntireUserDto.getEmail());
        user.setFirst_name(updateEntireUserDto.getFirst_name());
        user.setLast_name(updateEntireUserDto.getLast_name());
        user.setBirth_date(birth_date);
        user.setAddress(updateEntireUserDto.getAddress());
        user.setPhone_number(updateEntireUserDto.getPhone_number());

        this.users.set(user.getId(), user);

        return user;
    }

    public UserModel deleteUser(int userId) throws UserNotFoundException {
        final UserModel user = this.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }
        this.users.remove(user);
        return user;
    }

    public List<UserModel> getUsersByBirthDateRange(String from, String to) throws FromDateGreaterThanToDateException {
        final String[] fromItems = from.split("-");
        final String[] toItems = to.split("-");
        final LocalDate fromDate = LocalDate.of(
                Integer.parseInt(fromItems[0]),
                Integer.parseInt(fromItems[1]),
                Integer.parseInt(fromItems[2])
        );
        final LocalDate toDate = LocalDate.of(
                Integer.parseInt(toItems[0]),
                Integer.parseInt(toItems[1]),
                Integer.parseInt(toItems[2])
        );

        if (fromDate.isAfter(toDate)) {
            throw new FromDateGreaterThanToDateException();
        }

        return users.stream().filter(user -> {
            LocalDate birth_date = user.getBirth_date();
            return fromDate.isBefore(birth_date) && toDate.isAfter(birth_date);
        }).collect(Collectors.toList());
    }

}
