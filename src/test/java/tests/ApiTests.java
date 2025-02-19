package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;


public class ApiTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Успешное создание нового пользователя")
    void registrationTest() {
        String registrationData = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";

        given()
                .body(registrationData)
                .contentType(JSON)

                .when()
                .log().uri()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));


    }

    @Test
    @DisplayName("Успешное создание пользователя")
    void successfulUserCreationTest() {
        String createUserData = "{\"name\":\"Vitaly\",\"job\":\"QA Engineer\"}";
        given()
                .body(createUserData)
                .contentType(JSON)

                .when()
                .log().uri()
                .post("/user")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Vitaly"))
                .body("job", is("QA Engineer"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .extract().path("id");

    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void successfulDeleteUserTest() {
        given()
                .log().uri()

                .when()
                .delete("users/2")

                .then()
                .log().status()
                .log().body()
                .statusCode(204)
                .body(emptyOrNullString());
    }

    @Test
    @DisplayName("Не пустой список пользователей")
    void listUsersNotNullTest() {
        given()
                .log().uri()

                .when()
                .queryParam("page", "2")
                .get("/users")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data", notNullValue());
    }

    @Test
    @DisplayName("Успешная проверка пользователя в данных")
    void successfulVerificationOfTheUserInTheDataTest() {
        given()
                .log().uri()

                .when()
                .get("/unknown/6")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(6))
                .body("data.name", is("blue turquoise"))
                .body("data.year", is(2005))
                .body("data.color", is("#53B0AE"))
                .body("data.pantone_value", is("15-5217"));


    }
}

