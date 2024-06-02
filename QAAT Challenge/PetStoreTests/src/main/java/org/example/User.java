package org.example;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import java.io.IOException;
import org.testng.Assert;   //used to validate response status

import org.json.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class User {

    private String jsonBody;
    private String jsonBodyNewInfo;

    private JSONObject obj;
    private String username;
    private String password;

    @BeforeSuite
    public void GetData() throws IOException {
        // Define the Url
        RestAssured.baseURI = "http://localhost:8080/api/v3";

        // Define the data
        jsonBody = ConfigMethods.generateStringFromResource("src/main/java/org/example/Data/UserData.json");
        obj = new JSONObject(jsonBody);
        username = obj.getString("username");
        password = obj.getString("password");
    }

    //Create user
    @Test(priority = 1)
    public void CreateUser() {
    // Prueba definida para la creacion del usuario
        given().accept(ContentType.JSON).
                contentType(ContentType.JSON).filter(new AllureRestAssured()).
                body(jsonBody).when().post("/user").then().statusCode(200).log().body();
    }

    @Test(priority = 2)
    public void ValidResultOfCreateUser() {
        // Prueba definida para la creacion del usuario
        given().accept(ContentType.JSON).
                        contentType(ContentType.JSON).filter(new AllureRestAssured()).
                        body(jsonBody).when().post("/user").then().
                        assertThat().body("username", equalTo(username),
                                     "password", equalTo(password));
    }

    //Negative test cases
    @Test(priority = 3)
    public void CreateUserWithoutBody() {
    // Prueba definida para la creacion del usuario
        given().accept(ContentType.JSON).
                contentType(ContentType.JSON).filter(new AllureRestAssured()).
                when().post("/user").then().statusCode(400).log().body();
    }

    @Test(priority = 4)
    public void CreateUserWithoutHeaders() {
        // Prueba definida para la creacion del usuario
        given().body(jsonBody).filter(new AllureRestAssured()).
                when().post("/user").then().statusCode(415).log().body();
    }

    // Login the user.
    //Note: This can't have negative test cases cause the result is always the same
    @Test(priority = 5)
    public void LoginWithValidCredentials() {
        // Prueba definida para la creacion del usuario
        given().filter(new AllureRestAssured()).pathParams("username", username).pathParams("password", password).
                when().get("/user/login?username={username}&password={password}").then().
                statusCode(200).log().body();
    }

    // Get the user by username
    @Test(priority = 6)
    public void GetInformationWithValidUser() {
        given().filter(new AllureRestAssured()).pathParams("username", username).when().get("http://localhost:8080/api/v3/user/{username}").then().
                statusCode(200).assertThat().body("username", equalTo(username),
                                               "password", equalTo(password));
    }

    @Test(priority = 7)
    public void GetInformationWithInvalidUser() {
        // This API has a bug here cause the result should be 400. But the status code is 200
        String result = given().filter(new AllureRestAssured()).pathParams("username", "a").when().get("http://localhost:8080/api/v3/user/{username}").then().
                statusCode(404).extract().asString();
        Assert.assertEquals(true, result.contains("User not found"));
    }

    // Update the User Information
    @Test(priority = 8)
    public void UpdateSpecificUserInformation() {
        String bodyrq = "{\"username\": \"IanL\"}";
        given().header("Accept","application/json").header("Content-Type","application/json").filter(new AllureRestAssured()).
                body(bodyrq).pathParams("username", username).when().put("http://localhost:8080/api/v3/user/{username}").then().log().body().
                statusCode(200).assertThat().body("username", equalTo(username));
    }

    @Test(priority = 9)
    public void UpdateTheUserInformation() throws IOException {

        //Define the data for the new information
        jsonBodyNewInfo = ConfigMethods.generateStringFromResource("src/main/java/org/example/Data/UserDataNew.json");
        obj = new JSONObject(jsonBodyNewInfo);
        String new_username = obj.getString("username");
        String new_firstname = obj.getString("firstName");
        String new_lastname = obj.getString("lastName");
        String new_email = obj.getString("email");
        String new_password = obj.getString("password");
        String new_phone = obj.getString("phone");

        given().header("Accept","application/json").header("Content-Type","application/json").filter(new AllureRestAssured()).
                body(jsonBodyNewInfo).pathParams("username", username).when().put("http://localhost:8080/api/v3/user/{username}").then().
                statusCode(200).assertThat().body("username" , equalTo(new_username)).
                body("firstName" , equalTo(new_firstname)).
                body("lastName" , equalTo(new_lastname)).
                body("email" , equalTo(new_email)).
                body("password" , equalTo(new_password)).
                body("phone" , equalTo(new_phone));
    }

    // Logout the user
    @Test(priority = 10)
    public void LogOutTheUser() {
        String result = given().filter(new AllureRestAssured()).when().get("/user/logout").then().
                statusCode(200).extract().asString();
        Assert.assertEquals(true, result.contains("User logged out"));
    }

    // Delete the user
    @Test(priority = 11)
    public void DeleteTheUser() {
        given().filter(new AllureRestAssured()).pathParams("username", username).when().delete("/user/{username}").then().
                statusCode(200);
    }

    //To validate that the user is deleted
    @Test(priority = 12)
    public void GetInformationWithAUnregisteredUser() {
        // This API has a bug here cause the result should be 400. But the status code is 200
        String result = given().filter(new AllureRestAssured()).pathParams("username", "a").when().get("http://localhost:8080/api/v3/user/{username}").
                then().statusCode(404).extract().asString();
        Assert.assertEquals(true, result.contains("User not found"));
    }
}
