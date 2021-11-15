package com.homework.auto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;


public class ipLocation {

    private String ip;
    private String bodyResponse;
    private String country;


    @Test()
    public void ipLocator() throws Exception {

        getHttpResponse();
        isIsrael(bodyResponse);
    }

    private String getHttpResponse() throws IOException, InterruptedException {
        String ipLoc = getIp();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iplocation.net?ip=" + ipLoc + ""))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        bodyResponse = response.body();
        return bodyResponse;

    }

    private String getIp() {
        System.setProperty("webdriver.chrome.driver", "C:\\TempHW\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://fakercloud.com/api#Internet");
        ip = driver.findElement(By.xpath("//*[@data-example=\"IP Address\"]")).getAttribute("value");
        System.out.println("Your Ip is: " + ip);
        System.out.println("Continue to the validation step!");
        driver.quit();
        return ip;

    }

    private Object toJson(String jsonString) {
        JsonParser parser = new JsonParser();
        try {
            JsonElement json;
            if (parser.parse(jsonString).isJsonArray()) {
                json = parser.parse(jsonString).getAsJsonArray();
            } else {
                json = parser.parse(jsonString).getAsJsonObject();
            }
            return json;
        } catch (Exception e) {
            return jsonString;
        }

    }

    private void isIsrael(String countryResponse) {
        JsonObject jObjectResponse = (JsonObject) toJson(bodyResponse);
        country = jObjectResponse.getAsJsonPrimitive("country_name").getAsString();
        System.out.println(country);
        if (country.equals("Israel")) {
            System.out.println("Your country is Israel");
        } else {
            System.out.println("Your country is not Israel, try again");
        }
    }

}
