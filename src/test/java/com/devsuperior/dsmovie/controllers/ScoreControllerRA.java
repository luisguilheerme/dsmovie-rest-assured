package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

public class ScoreControllerRA {
	
	private Long existingMovieId, nonExistingMovieId;
	private String clientUsername, clientPassword, clientToken;
	private Double negativeScore;
	
	private Map<String, Object> postScoreInstance;
	
	@BeforeEach
	void setUp() throws JSONException {
		baseURI = "http://localhost:8080";
		
		existingMovieId = 7L;
		nonExistingMovieId = 100L;
		negativeScore = -2.0;
		
		clientUsername = "alex@gmail.com"; 
		clientPassword = "123456";	
		
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		
		postScoreInstance = new HashMap<>();
		postScoreInstance.put("score", 4);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {	
		
		postScoreInstance.put("movieId", nonExistingMovieId);
		
		JSONObject newScore = new JSONObject(postScoreInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)				
		.when()
			.put("/scores")
		.then()
			.statusCode(404);			
		
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
				
		JSONObject newScore = new JSONObject(postScoreInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)				
		.when()
			.put("/scores")
		.then()
			.statusCode(422);	
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {			
		
		postScoreInstance.put("movieId", existingMovieId);
		postScoreInstance.put("score", negativeScore);
		
		JSONObject newScore = new JSONObject(postScoreInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)				
		.when()
			.put("/scores")
		.then()
			.statusCode(422);	
	}
}
