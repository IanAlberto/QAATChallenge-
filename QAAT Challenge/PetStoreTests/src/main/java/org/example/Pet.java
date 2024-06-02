package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Pet {

    private String jsonBody;
    private JSONObject obj;
    private int id;

    @BeforeSuite
    public void GetData() throws IOException {
        // Define the Url
        RestAssured.baseURI = "http://localhost:8080/api/v3";

        // Define the data
        jsonBody = ConfigMethods.generateStringFromResource("src/main/java/org/example/Data/PetData.json");
        obj = new JSONObject(jsonBody);

        id = obj.getInt("id");

    }

    //Get the information of the pet inventaries by the status
    @Test(priority = 1)
    public void AddAnewPet() {
        given().header("Accept","application/xml").header("Content-Type","application/json").
                body(jsonBody).when().post("/pet").then().statusCode(200).log().body();
    }

    @Test(priority = 1)
    public void AddAnewPetWithoutBody() {
        given().header("Accept","application/xml").header("Content-Type","application/json").
                when().post("/pet").then().statusCode(400).log().body();
    }

    @Test(priority = 1)
    public void AddAnewPetWithInvalidBody() {
        String bodyrq = "{\n" +
                        "  \"id\": \"\",\n" +
                        "  \"name\": \"doggie\",\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"name\": \"Dogs\"\n" +
                        "  },\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"https://th.bing.com/th/id/R.6f5ff4a55984b199b086d382c24543df?rik=QTzOlhysHImPEQ&riu=http%3a%2f%2fwww.infoescola.com%2fwp-content%2fuploads%2f2010%2f08%2fdoberman_223996249.jpg&ehk=%2fDkftpHXIILaR8lEJ87ikNsWcPsVqngYYb1Ivb1CoF8%3d&risl=&pid=ImgRaw&r=0\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 0,\n" +
                        "      \"name\": \"Dobber\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"available\"\n" +
                        "}";
        System.out.println(bodyrq);
        given().header("Accept","application/xml").header("Content-Type","application/json").body(bodyrq).
                when().post("/pet").then().statusCode(500).log().body();
    }



    // Find pets by ID
    @Test(priority = 2)
    public void FindPetsByID() {
        given().header("Accept","application/xml").pathParams("id", id).
                when().get("/pet/{id}").then().statusCode(200).assertThat().header("Content-Type", equalTo("application/xml"));
    }

    @Test(priority = 2)
    public void FindPetsByInexistentPet() {
        given().pathParams("id", 22).
                when().get("/pet/{id}").then().statusCode(404).assertThat().header("Content-Type", equalTo("application/json"));
    }

    @Test(priority = 2)
    public void FindPetsBySpecialCharacter() {
        given().header("Accept","application/xml").pathParams("id", "-").
                when().get("/pet/{id}").then().statusCode(400).assertThat().header("Content-Type", equalTo("application/xml"));
    }


    // Update the pet information
    @Test(priority = 3)
    public void UpdateThePetInformationWithNoValidJson(){
        String bodyrq = "{\n" +
                "  \"id\": 11,\n" +
                "  \"name\": \"doggie\",\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Dogs\"\n" +
                "  },\n" +
                "  \"photoUrls\": [\n" +
                "    \"https://th.bing.com/th/id/R.6f5ff4a55984b199b086d382c24543df?rik=QTzOlhysHImPEQ&riu=http%3a%2f%2fwww.infoescola.com%2fwp-content%2fuploads%2f2010%2f08%2fdoberman_223996249.jpg&ehk=%2fDkftpHXIILaR8lEJ87ikNsWcPsVqngYYb1Ivb1CoF8%3d&risl=&pid=ImgRaw&r=0\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"Dobber\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";
        given().body(bodyrq).when().put("/pet").then().
                statusCode(415).log().body();
    }

    // Delete the pet information
    @Test(priority = 4)
    public void DeletePetById() {
        given().accept(ContentType.JSON).pathParams("petid", id).
                when().delete("pet/{petid}").then().statusCode(200).log().body();
    }

    // Negative test case
    @Test(priority = 4)
    public void DeleteOrderWithInvalidID() {
        given().accept(ContentType.JSON).
                when().delete("/pet/50000000001346451213546").then().statusCode(400);
    }
}
