package dev.dispache.test_task.contollers;

import dev.dispache.test_task.dtos.CreateUserDto;
import dev.dispache.test_task.dtos.UpdateEntireUserDto;
import dev.dispache.test_task.dtos.UpdateOneSomeUserFieldsDto;
import dev.dispache.test_task.exceptions.FromDateGreaterThanToDateException;
import dev.dispache.test_task.exceptions.NotAllowedUserAgeException;
import dev.dispache.test_task.exceptions.UserNotFoundException;
import dev.dispache.test_task.models.UserModel;
import dev.dispache.test_task.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@Validated
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserModel> createUser(@Valid @RequestBody CreateUserDto createUserDto) throws NotAllowedUserAgeException {
        final UserModel createdUser = this.usersService.createUser(createUserDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserModel> updateOneSomeUserFields(
            @PathVariable("id") int userId,
            @Valid @RequestBody UpdateOneSomeUserFieldsDto updateOneSomeUserFieldsDto
    ) throws UserNotFoundException {
        final UserModel updatedUser = this.usersService.updateOneSomeUserFields(userId, updateOneSomeUserFieldsDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/update-entire-user/{id}")
    public ResponseEntity<UserModel> updateEntireUser(
            @PathVariable("id") int userId,
            @Valid @RequestBody UpdateEntireUserDto updateEntireUserDto
    ) throws UserNotFoundException {
        final UserModel updatedUser = this.usersService.updateEntireUser(userId, updateEntireUserDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<UserModel> deleteUser(@PathVariable("id") int userId) throws UserNotFoundException {
        final UserModel deletedUser = this.usersService.deleteUser(userId);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Iterable<UserModel>> getUsersByBirthDateRange(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to
    ) throws FromDateGreaterThanToDateException {
        final Iterable<UserModel> usersByBirthDateRange = this.usersService.getUsersByBirthDateRange(from, to);
        return new ResponseEntity<>(usersByBirthDateRange, HttpStatus.OK);
    }

}
