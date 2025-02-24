package tests;

import io.restassured.RestAssured;
import models.lombok.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.LoginSpec.*;

@Tag("Api_tests")

public class ApiTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Успешное создание нового пользователя")
    void registrationLoginWithStepsSpecsTest() {
        LoginBodyLombokModel registrationData = new LoginBodyLombokModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        LoginResponseLombokModel response = step("Делаем запрос", () -> {
            return given(loginRequestSpec)
                    .body(registrationData)

                    .when()

                    .post("/register")

                    .then()
                    .spec(loginResponseStatusCod200Spec)
                    .extract().as(LoginResponseLombokModel.class);
        });
        step("Проверяем ответ", () -> {
            assertEquals("4", response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }


    @Test
    @DisplayName("Успешное создание пользователя")
    void successfulUserCreationTest() {
        UserDataModel createUserData = new UserDataModel();
        createUserData.setName("Vitaly");
        createUserData.setJob("QA Engineer");

        UserDataResponseModel response = step("Делаем запрос", () -> {
            return given(loginRequestSpec)

                    .body(createUserData)
                    .contentType(JSON)

                    .when()
                    .log().uri()
                    .post("/user")

                    .then()
                    .spec(loginResponseStatusCod201Spec)

                    .extract().as(UserDataResponseModel.class);
        });

        step("Проверяем ответ", () -> {
            assertEquals("Vitaly", response.getName());
            assertEquals("QA Engineer", response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void successfulDeleteUserTest() {

        step("Проверяем что пользователь не найден", () -> {

            given(loginRequestSpec)
                    .log().uri()

                    .when()
                    .delete("users/2")

                    .then()

                    .spec(loginResponseStatusCod204Spec)

                    .body(emptyOrNullString());
        });
    }

    @Test
    @DisplayName("Не пустой список пользователей")
    void listUsersNotNullTest() {

        step("Проверяем отображение не пустого списка", () -> {

            given(loginRequestSpec)
                    .log().uri()

                    .when()
                    .queryParam("page", "2")
                    .get("/users")

                    .then()
                    .spec(loginResponseStatusCod200Spec)

                    .body("data", notNullValue());
        });

    }

    @Test
    @DisplayName("Успешная проверка пользователя в данных")
    void successfulVerificationOfTheUserInTheDataTest() {

        UserResponseDataModel response = step("Делаем запрос", () -> {

            return given(loginRequestSpec)

                    .log().uri()

                    .when()
                    .get("/unknown/6")

                    .then()
                    .spec(loginResponseStatusCod200Spec)


                    .extract().as(UserResponseDataModel.class);
        });

        step("Проверяем ответ", () -> {

            assertEquals("6", response.getData().getId());
            assertEquals("blue turquoise", response.getData().getName());
            assertEquals("2005", response.getData().getYear());
            assertEquals("#53B0AE", response.getData().getColor());
            assertEquals("15-5217", response.getData().getPantoneValue());
        });
    }
}

