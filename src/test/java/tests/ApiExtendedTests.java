package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyModel;
import models.pojo.LoginResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpec.loginRequestSpec;
import static specs.LoginSpec.loginResponseSpec;


public class ApiExtendedTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Успешное создание нового пользователя")
    void registrationPojoTest() {
        LoginBodyModel registrationData = new LoginBodyModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        LoginResponseModel response = given()
                .filter(withCustomTemplates())
                .body(registrationData)
                .contentType(JSON)

                .when()
                .log().uri()
                .log().body()
                .log().headers()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)

                .extract().as(LoginResponseModel.class);

        assertEquals("4", response.getId());
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());


    }

    @Test
    @DisplayName("Успешное создание нового пользователя")
    void registrationLombokTest() {
        LoginBodyLombokModel registrationData = new LoginBodyLombokModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        LoginResponseLombokModel response = given()
                .filter(withCustomTemplates())
                .body(registrationData)
                .contentType(JSON)

                .when()
                .log().uri()
                .log().body()
                .log().headers()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)

                .extract().as(LoginResponseLombokModel.class);

        assertEquals("4", response.getId());
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());

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
                    .spec(loginResponseSpec)
                    .extract().as(LoginResponseLombokModel.class);
        });
        step("Проверяем ответ", () -> {
            assertEquals("4", response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }
}









