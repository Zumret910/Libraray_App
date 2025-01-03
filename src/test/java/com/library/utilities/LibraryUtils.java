package com.library.utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LibraryUtils {
    public static String getToken(String email, String password) {

        JsonPath jp = RestAssured.given().accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .formParam("email", email).formParam("password", password)
                .when().post("/login")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().jsonPath();

        String accessToken = jp.getString("token");
        System.out.println(accessToken);

        return accessToken;

    }


    public static String generateTokenByRole(String role) {
        Map<String, String> roleCredentials = returnCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");
        return getToken(email, password);

    }
    public static Map<String, String> returnCredentials(String role) {
        String email = "";
        String password = "";

        switch (role) {
            case "librarian":
                // email = ConfigurationReader.getProperty("teacher_email") ;
                // password = ConfigurationReader.getProperty("teacher_password") ;
                email = System.getenv("EMAIL");
                password = System.getenv("PASSWORD");
                break;
            default:

                throw new RuntimeException("Invalid Role Entry :\n>> " + role + " <<");
        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return credentials;

    }
    public static Map<String,Object> createRandomBook(){
        Map<String,Object> book = new LinkedHashMap<>();
        Faker faker=new Faker();
        book.put("name",faker.book().title());
        book.put("isbn",faker.book().genre());
        book.put("year",faker.number().numberBetween(1900, 2022));
        book.put("author",faker.book().author());
        book.put("book_category_id",faker.number().numberBetween(1, 20));
        book.put("description",faker.book().publisher());
        return book;

    }
}
