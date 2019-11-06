package de.smartsquare;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/generate/9")
          .then()
             .statusCode(200)
             .body(is("[3, 3]"));
    }

}