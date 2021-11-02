package com.rest.CRUD;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import java.io.FileNotFoundException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterClass;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

//Given - Prepare API REquest
// When - What type of Request you are making
// Then - Assertions

public class RestAPI_CRUD_Operations {

	Properties inputs;
	String strBaseURI, strRequestResource, strUserID,  strInputUserData;

	@BeforeClass
	public void setUP() throws FileNotFoundException, IOException {
		inputs = new Properties();
		inputs.load(new FileReader("src//main//resources//details.properties"));

		strBaseURI = inputs.getProperty("baseURI");
		strRequestResource = inputs.getProperty("RequestResource");
		strUserID = inputs.getProperty("userID");
		strInputUserData = inputs.getProperty("body");
	}

	// CREATE USER - POST REQUEST
	@Test(priority = 1)
	public void createUser() {

		RestAssured.baseURI = strBaseURI;

		System.out.println("Creating a user: ");

		given().log().all()
			.header("Content-Type", "application/json")
			.body(strInputUserData)
		.when()
			.post(strRequestResource)
		.then().log().all()
			.assertThat().statusCode(201)
			.body("id", is(notNullValue()));

	}

	// GET USERS ON A PAGE - GET REQUEST
	@Test(priority = 2)
	@Parameters("pageNumber")
	public void getUsersDataOnAPage(int PageNumber) {

		RestAssured.baseURI = strBaseURI;

		System.out.println("Get All users in a Page with Number:  " + PageNumber);

		given().log().all()
			.queryParam("page", PageNumber)
		.when()
			.get(strRequestResource)
		.then().log().all()
			.assertThat().statusCode(200)
			.body("page", equalTo(2));
	}

	// GET USER BY ID - GET REQUEST
	@Test(priority = 3)
	public void getUserByID() {

		System.out.println("Get User By ID: " + strUserID);

		RestAssured.baseURI = strBaseURI;

		given().log().all()
			.queryParam("id", strUserID)
		.when()
			.get(strRequestResource)
		.then().log().all()
			.assertThat().statusCode(200)
			.body("data.id", equalTo(Integer.parseInt(strUserID)));
	}

	// PARTIAL UPDATE USER BY ID - PATCH REQUEST
	@Test (priority = 4)
	public void partialUpdateUser() {
		
		System.out.println("Partial Update User By ID: " + strUserID);
		
		RestAssured.baseURI = strBaseURI;
		
		String strUserData="{"
				+ "\"first_name\":\"Ajith \",\"last_name\":\"Kumar\""
				+ "}";
		
		given().log().all()
			.queryParam("id", strUserID)
			.header("Content-Type", "application/json")
			.body(strUserData)
		.when()
			.patch(strRequestResource)
		.then().log().all()
			.assertThat().statusCode(200);	
	}

	
	//UPDATE USER BY ID - PUT REQUEST
	@Test(priority = 5)
	public void updateUserByID() {
		
		System.out.println("Updating User with ID: " + strUserID);
		
		RestAssured.baseURI = strBaseURI;
		
		given().log().all()
			.queryParam("id", strUserID)
			.header("Content-Type", "application/json")
			.body(strInputUserData)
		.when()
			.patch(strRequestResource)
	   .then().log().all()
	   		.assertThat().statusCode(200)
	   		.body("updatedAt", is(notNullValue()));
		
	}
	
	
	//DELETE USER - DELETE REQUEST
	@Test (priority = 6)
	public void deleteUserByID() {
		
		System.out.println("Deleting User with ID: " + strUserID);
		
		RestAssured.baseURI = strBaseURI;
		
		given().log().all()
			.queryParam("id", strUserID)
		.when()
			.delete(strRequestResource)
		.then().log().all()
			.assertThat().statusCode(204);
	}
	
	
	
	@AfterClass
	public void tearDown() {
		inputs.clear();
	}

}
