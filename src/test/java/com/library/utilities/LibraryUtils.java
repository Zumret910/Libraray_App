package com.library.utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
    public static Map<String,Object> createRandomUser(String userType){
        Map<String,Object> user = new LinkedHashMap<>();
        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Generate random dates within a specific range
        LocalDate startDate = LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 31));
        LocalDate endDate = LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 31));
        Faker faker=new Faker();
       user.put("full_name", faker.name().fullName());
       user.put("email", faker.internet().emailAddress());
        user.put("password", faker.internet().password(8, 16));
        user.put("status", "ACTIVE"); // Fixed value as per your request
        user.put("start_date", startDate.format(formatter)); // Today's date
        user.put("end_date", endDate.format(formatter)); // 30 days from now
        user.put("address", faker.address().fullAddress());
        switch (userType){
            case "admin":
                user.put("user_group_id",1);
                break;
            case "librarian":
                user.put("user_group_id",2);
                break;
            case "student":
                user.put("user_group_id",3);
                break;
            default:
                throw new RuntimeException("Invalid User Type Entry :\n>> " + userType + " <<");

        }        return user;

    }

}
