package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Store {

    private String jsonBody;
    private JSONObject obj;
    
    private int id;
    private int petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;
    
    @BeforeSuite
    public void GetData() throws IOException {
        // Define the Url
        RestAssured.baseURI = "http://localhost:8080/api/v3";

        // Define the data
        jsonBody = ConfigMethods.generateStringFromResource("src/main/java/org/example/Data/StoreData.json");
        obj = new JSONObject(jsonBody);
        id = obj.getInt("id");
        petId = obj.getInt("petId");
        quantity = obj.getInt("quantity");
        shipDate = obj.getString("shipDate");
        status = obj.getString("status");
        complete = obj.getBoolean("complete");
    }

    //Get the information of the pet inventories by the status
    @Test(priority = 1)
    public void GetPetInventariesByStatus() {
        given().accept(ContentType.JSON).
                when().get("/store/inventory").then().statusCode(200).log().body();
    }

    // Place an order and validate if the information changes
    @Test(priority = 2)
    public void PlaceAnOrder() {
        given().accept(ContentType.JSON).
                contentType(ContentType.JSON).body(jsonBody).
                when().post("/store/order").then().statusCode(200).log().body();
    }

    @Test(priority = 2)
    public void ValidateResponse() {
        given().accept(ContentType.JSON).
                contentType(ContentType.JSON).body(jsonBody).
                when().post("/store/order").then().statusCode(200).log().body().assertThat().
                body("id",equalTo(id)).
                body("petId",equalTo(petId)).
                body("quantity",equalTo(quantity)).
                body("shipDate",equalTo(shipDate)).
                body("status",equalTo(status)).
                body("complete",equalTo(complete));
    }

    // Negative test case
    @Test(priority = 3)
    public void PlaceAnOrderWithoutBody() {
        given().body("").when().post("/store/order").then().statusCode(415).log().body();
    }


    @Test(priority = 4)
    public void FindOrderById() {
        given().accept(ContentType.JSON).pathParams("orderid", id).
                when().get("/store/order/{orderid}").then().statusCode(200).log().body();
    }

    // Negative test case
    @Test(priority = 5)
    public void FindOrderNonCreated() {
        String result = given().accept(ContentType.JSON).pathParams("orderid", 22).
                when().get("/store/order/{orderid}").then().statusCode(404).extract().asString();
        Assert.assertEquals(true, result.contains("Order not found"));
    }

    @Test(priority = 6)
    public void FindOrderWithInvalidID() {
        given().accept(ContentType.JSON).pathParams("orderid", 22.0).
                when().get("/store/order/{orderid}").then().statusCode(400);
    }

    @Test(priority = 7)
    public void DeleteOrderById() {
        given().accept(ContentType.JSON).pathParams("orderid", id).
                when().delete("/store/order/{orderid}").then().statusCode(200).log().body();
    }

    // Negative test case
    @Test(priority = 8)
    public void DeleteOrderWithInvalidID() {
        given().accept(ContentType.JSON).pathParams("orderid", 22.0).
                when().delete("/store/order/{orderid}").then().statusCode(400);
    }
}
