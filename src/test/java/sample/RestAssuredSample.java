package sample;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.containsString;

public class RestAssuredSample {

    private String url;

    public RestAssuredSample() {
        // Ajustes realizados para evitar repetições na utilização do baseURL
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                prop.load(input);
                url = prop.getProperty("base_url");
            } else {
                System.out.println("Erro: Não foi possível carregar o arquivo config.properties");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
@Test
public void postUserTest(){
    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "Jhonatan");
    requestParams.put("job", "QA");

    try {
        given().
            body(requestParams.toJSONString()).
        when().
            post(url).
        then().
            statusCode(201).
            body(containsString("createdAt"));
    } catch (Exception e) {
        System.err.println("Falha no teste postUserTest:");
        e.printStackTrace();
    }
}

@Test
public void getPageOneTest(){
    try {
        given().
            param("page", "1").
        when().
            get(url).
        then().
            statusCode(200).
            body("page", equalTo(1));
    } catch (Exception e) {
        System.err.println("Falha no teste getPageOneTest:");
        e.printStackTrace();
    }
}

@Test
public void getUserTest() {
    try {
        get(url + "/2").then().body("data.id", equalTo(2));
    } catch (Exception e) {
        System.err.println("Falha no teste getUserTest:");
        e.printStackTrace();
    }
}

@Test
public void putUserTest(){
    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "Jhonatan");
    requestParams.put("job", "QA");

    try {
        given().
            body(requestParams.toJSONString()).
        when().
            put(url + "/2").
        then().
            statusCode(200).
            body(containsString("updatedAt"));
    } catch (Exception e) {
        System.err.println("Falha no teste putUserTest:");
        e.printStackTrace();
    }
}

@Test
public void deleteUserTest(){
    try {
        when().
            delete(url + "/2").
        then().
            statusCode(204);
    } catch (Exception e) {
        System.err.println("Falha no teste deleteUserTest:");
        e.printStackTrace();
    }
}

/// cenários de erro

@Test
public void postUserInvalidDataTest() {
    JSONObject requestParams = new JSONObject();
    // Não fornecer dados obrigatórios, simulando um pedido inválido
    try {
        given().
            body(requestParams.toJSONString()).
        when().
            post(url).
        then().
            statusCode(400); // Código 400 para indicar requisição inválida
    } catch (Exception e) {
        System.err.println("Falha no teste postUserInvalidDataTest:");
        e.printStackTrace();
    }
}

@Test
public void getUserNonExistentTest() {
    // Tentar obter informações sobre um usuário que não existe
    try {
        get(url + "/9999").
        then().
            statusCode(404); // Código 404 para indicar recurso não encontrado
    } catch (Exception e) {
        System.err.println("Falha no teste getUserNonExistentTest:");
        e.printStackTrace();
    }
}

@Test
public void putUserInvalidDataTest() {
    JSONObject requestParams = new JSONObject();
    // Tentar atualizar um usuário com dados inválidos
    try {
        given().
            body(requestParams.toJSONString()).
        when().
            put(url + "/2").
        then().
            statusCode(400); // Código 400 para indicar requisição inválida
    } catch (Exception e) {
        System.err.println("Falha no teste putUserInvalidDataTest:");
        e.printStackTrace();
    }
}

@Test
public void deleteUserNonExistentTest() {
    // Tentar excluir um usuário que não existe
    try {
        when().
            delete(url + "/9999").
        then().
            statusCode(404); // Código 404 para indicar recurso não encontrado
    } catch (Exception e) {
        System.err.println("Falha no teste deleteUserNonExistentTest:");
        e.printStackTrace();
    }
}
}