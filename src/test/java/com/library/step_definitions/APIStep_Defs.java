package com.library.step_definitions;

import com.library.utilities.LibraryUtils;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

public class APIStep_Defs {
    RequestSpecification givenPart = RestAssured.given().log().all();
    Response response;
    JsonPath jp;
    ValidatableResponse thenPart;
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String role) {
        String token=LibraryUtils.generateTokenByRole(role);
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
        thenPart.body(path, everyItem(notNullValue()));
    }
    @When("I send GET request to  {string} endpoint")
    public void iSendGETRequestToEndpoint(String endpoint) {
        response = givenPart.when().get(endpoint);
        jp = response.jsonPath();
        thenPart = response.then();

        response.prettyPrint();
    }
}
