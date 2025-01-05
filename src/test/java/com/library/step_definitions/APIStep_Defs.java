package com.library.step_definitions;

import com.library.pages.BooksPage;
import com.library.pages.SignInPage;
import com.library.utilities.BrowserUtils;
import com.library.utilities.DB_Util;
import com.library.utilities.LibraryUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;

public class APIStep_Defs {
    RequestSpecification givenPart = RestAssured.given().log().all();
    Response response;
    JsonPath jp;
    ValidatableResponse thenPart;
    String pathParam;
    Map<String, Object> randomDataMap;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String role) {
        String token = LibraryUtils.generateTokenByRole(role);
        givenPart.header("x-library-token", token);
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);
    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer expectedStatusCode) {
        thenPart.statusCode(expectedStatusCode);
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String expectedContentType) {
        thenPart.contentType(expectedContentType);
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path, notNullValue());
    }

    @When("I send GET request to  {string} endpoint")
    public void iSendGETRequestToEndpoint(String endpoint) {
        response = givenPart.when().get(endpoint);
        jp = response.jsonPath();
        thenPart = response.then();

        response.prettyPrint();
    }

    @And("Path param {string} is {string}")
    public void pathParamIs(String pathParamKey, String pathParamValue) {
        pathParam = pathParamValue;
        givenPart.pathParams(pathParamKey, pathParamValue);
    }


    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String pathKey) {
        String actualValue = jp.getString(pathKey);
        Assert.assertEquals(pathParam, actualValue);
    }

    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String> fields) {
        for (String field : fields) {
            thenPart.body(field, notNullValue());
        }

    }

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentType) {
        givenPart.contentType(contentType);
    }

    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String dataType) {
        switch (dataType) {

            case "book":
                randomDataMap = LibraryUtils.createRandomBook();
                break;
            case "librarian":
            case "student":
            case "admin":
                randomDataMap = LibraryUtils.createRandomUser(dataType);
                break;
            default:
                throw new RuntimeException("Wrong data type is provide");
        }
        givenPart.formParams(randomDataMap);
        System.out.println("randomDataMap = " + randomDataMap);

    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endPoint) {
        response = givenPart.when().post(endPoint);
        jp = response.jsonPath();
        thenPart = response.then();

        response.prettyPrint();
    }

    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String path, String expectedValue) {
        String actual = jp.getString(path);
        Assert.assertEquals(expectedValue, actual);
    }

    SignInPage signInPage = new SignInPage();

    @And("I logged in Library UI as {string}")
    public void iLoggedInLibraryUIAs(String role) {
        switch (role) {
            case "librarian":
                signInPage.login();
                break;
            default:
                throw new RuntimeException("Wrong data type is provide");
        }

    }

    @And("I navigate to {string} page")
    public void iNavigateToPage(String moduleName) {
        signInPage.navigateByName(moduleName);

    }

    BooksPage booksPage = new BooksPage();

    @And("UI, Database and API created book information must match")
    public void uiDatabaseAndAPICreatedBookInformationMustMatch() {
        //compare API with database
        int expectedId = jp.getInt("book_id");
        System.out.println(expectedId);
        String quary = "select name,isbn,author from books where id=" + expectedId;
        DB_Util.runQuery(quary);
        Map<String, String> expectedBookData = DB_Util.getRowMap(1);
        System.out.println("expectedBookData = " + expectedBookData);
        for (Map.Entry<String, Object> entry : randomDataMap.entrySet()) {
            if (expectedBookData.containsKey(entry.getKey())) {
                Assert.assertEquals(entry.getValue(), expectedBookData.get(entry.getKey()));
            }
        }
        //compare Database with UI
//        BrowserUtils.waitForPageToLoad(5);
//        String expectedBookName = (String) randomDataMap.get("name");
//       booksPage.editBook(expectedBookName).click();
//        for (Map.Entry<String, String> entry : expectedBookData.entrySet()){
//            BrowserUtils.waitForPageToLoad(1);
//            Assert.assertEquals(entry.getValue(),booksPage.getBookDataByAttribute(entry.getKey()));
        //    }
// compare API with UI
        BrowserUtils.waitForPageToLoad(5);
        String expectedBookName = (String) randomDataMap.get("name");
        booksPage.editBook(expectedBookName).click();
        Assert.assertEquals(randomDataMap.get("name"), booksPage.getBookDataByAttribute("name"));
        Assert.assertEquals(randomDataMap.get("isbn"), booksPage.getBookDataByAttribute("isbn"));
        Assert.assertEquals(randomDataMap.get("author"), booksPage.getBookDataByAttribute("author"));
    }

    @And("created user information should match with Database")
    public void createdUserInformationShouldMatchWithDatabase() {
        //compare API with database
        int expectedId = jp.getInt("user_id");
        System.out.println(expectedId);
        String quary = "select full_name,email,status,address from users where id=" + expectedId;
        DB_Util.runQuery(quary);
        Map<String, String> expectedUserData = DB_Util.getRowMap(1);
        System.out.println("expectedBookData = " + expectedUserData);
        for (Map.Entry<String, Object> entry : randomDataMap.entrySet()) {
            if (expectedUserData.containsKey(entry.getKey())) {
                Assert.assertEquals(entry.getValue(), expectedUserData.get(entry.getKey()));
            }
        }

    }

    @And("created user should be able to login Library UI")
    public void createdUserShouldBeAbleToLoginLibraryUI() {
           String userName=(String)randomDataMap.get("email");
           String password=(String)randomDataMap.get("password");
           signInPage.login(userName,password);
    }

    @And("created user name should appear in Dashboard Page")
    public void createdUserNameShouldAppearInDashboardPage() {
        BrowserUtils.waitForPageToLoad(5);
       String userName=(String)randomDataMap.get("full_name");
       String actualName=signInPage.getUserNameText();
        System.out.println(actualName);
        Assert.assertEquals(userName,actualName);

    }
}
