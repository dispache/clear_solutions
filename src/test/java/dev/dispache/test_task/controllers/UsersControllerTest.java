package dev.dispache.test_task.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dispache.test_task.dtos.CreateUserDto;
import dev.dispache.test_task.dtos.UpdateEntireUserDto;
import dev.dispache.test_task.models.UserModel;
import dev.dispache.test_task.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsersService usersService;

    final String base_uri = "/users";

    final ObjectMapper mapper = new ObjectMapper();

    private final String email = "created@gmail.com";
    private final String first_name = "Name";
    private final String last_name = "Surname";
    private final String birth_date = "1995-05-05";
    private final String address = "Portugal, Porto";
    private final String phone_number = "1234";

    private String mapToJson(Object obj) throws JsonProcessingException {
        return this.mapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> cls) throws JsonProcessingException {
        return this.mapper.readValue(json, cls);
    }

    @BeforeEach
    public void setup() {
        this.usersService.setAllowed_age(18);

        this.usersService.createUser(new CreateUserDto(
           "first@gmail.com", "First", "First", "1990-01-02", null, null
        ));
        this.usersService.createUser(new CreateUserDto(
           "second@gmail.com", "Second", "Second", "2000-04-20", "Australia, Sydney", "1234"
        ));

    }

    @Test
    public void shouldCreateNewUserAndReturnItWithHttpStatus201AndJSONContentType() throws Exception {

        final CreateUserDto createUserDto = new CreateUserDto(
                email, first_name, last_name, birth_date, address, phone_number
        );

        final String content = this.mapToJson(createUserDto);

        mvc.perform(post(base_uri + "/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(matcher -> {
            final MockHttpServletResponse response = matcher.getResponse();
            final int response_status = response.getStatus();
            final String response_content_type = response.getContentType();
            final String response_body = response.getContentAsString();
            final UserModel created_user = mapFromJson(response_body, UserModel.class);

            assertEquals(HttpStatus.CREATED.value(), response_status);
            assertEquals("application/json", response_content_type);
            assertEquals(first_name, created_user.getFirst_name());
            assertEquals(email, created_user.getEmail());
        });
    }

    @Test
    public void shouldThrowNotAllowedUserAgeExceptionWith400HttpStatusCode() throws Exception {
        final String birth_date = "2020-05-05";

        final CreateUserDto createUserDto = new CreateUserDto(
                email, first_name, last_name, birth_date, address, phone_number
        );

        final String content = this.mapToJson(createUserDto);

        mvc.perform(post(base_uri + "/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(matcher -> {
            final MockHttpServletResponse response = matcher.getResponse();
            final int response_status_code = response.getStatus();
            final String response_msg = response.getContentAsString();

            final String expected_msg = "The lowest allowed registration age is 18";

           assertEquals(HttpStatus.BAD_REQUEST.value(), response_status_code);
           assertEquals(expected_msg, response_msg);
        });
    }

    @Test
    public void shouldUpdateUserEmailAndReturnUserWithUpdatedEmailWith200HttpStatusCode() throws Exception {
        final int userId = 0;

        final String email = "updatedemail@gmail.com";

        final Map<String, String> body = Map.of("email", email);

        final String content = this.mapToJson(body);

        mvc.perform(put(base_uri + "/update-user/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(matcher -> {
            final MockHttpServletResponse response = matcher.getResponse();
            final int response_status_code = response.getStatus();
            final String response_body = response.getContentAsString();

            final UserModel updatedUser = this.mapFromJson(response_body, UserModel.class);

            assertEquals(HttpStatus.OK.value(), response_status_code);
            assertEquals(email, updatedUser.getEmail());
        });
    }

    @Test
    public void shouldUpdateEntireUserAndReturnItWith200HttpStatusCode() throws Exception {
        final int userId = 0;

        final UpdateEntireUserDto updateEntireUserDto = new UpdateEntireUserDto(
                email, first_name, last_name, birth_date, address, phone_number
        );

        final String content = this.mapToJson(updateEntireUserDto);

        mvc.perform(put(base_uri + "/update-entire-user/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(matcher -> {
            final MockHttpServletResponse response = matcher.getResponse();
            final int response_status_code = response.getStatus();
            final String response_content_type = response.getContentType();
            final String response_body = response.getContentAsString();

            final UserModel updatedUser = this.mapFromJson(response_body, UserModel.class);

            assertEquals(HttpStatus.OK.value(), response_status_code);
            assertEquals("application/json", response_content_type);
            assertEquals(userId, updatedUser.getId());
            assertEquals(email, updatedUser.getEmail());
            assertEquals(phone_number, updatedUser.getPhone_number());
        });
    }

    @Test
    public void shouldDeleteUserAndReturnItWith200HttpStatusCode() throws Exception {
        final int userId = 0;

        mvc.perform(delete(base_uri + "/delete-user/" + userId))
                .andExpect(matcher -> {
                    final MockHttpServletResponse response = matcher.getResponse();
                    final int response_status_code = response.getStatus();
                    final String response_content_type = response.getContentType();
                    final String response_body = response.getContentAsString();

                    final UserModel deletedUser = this.mapFromJson(response_body, UserModel.class);

                    assertEquals(HttpStatus.OK.value(), response_status_code);
                    assertEquals("application/json", response_content_type);
                    assertEquals(userId, deletedUser.getId());
                });
    }

    @Test
    public void shouldReturn400HttpStatusCodeBecauseToParamIsAbsent() throws Exception {
        final String query_string = "?from=1990-03-03";

        mvc.perform(get(base_uri + query_string)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(matcher -> {
            final MockHttpServletResponse response = matcher.getResponse();
            final int response_status_code = response.getStatus();
            final String response_msg = response.getContentAsString();

            final String expected_msg = "Required request parameter 'to' for method parameter type String is not present";

            assertEquals(HttpStatus.BAD_REQUEST.value(), response_status_code);
            assertEquals(expected_msg, response_msg);
        });
    }

}
