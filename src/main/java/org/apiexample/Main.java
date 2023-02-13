package org.apiexample;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Gson gson = new Gson();
        FileReader jsonFile = new FileReader("gmap_distance_matrix_response.json");
        Map<String, Object> jsonMap = new HashMap<>();


        Scanner sc = new Scanner(System.in);
        System.out.print("Dear user, please input your pickup location: ");
        String origin = sc.nextLine();
        System.out.print("Please enter your destination: ");
        String dropOff = sc.nextLine();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/distancematrix/json").newBuilder();
        urlBuilder.addQueryParameter("origins", origin);
        urlBuilder.addQueryParameter("destinations", dropOff);
        urlBuilder.addQueryParameter("units", "imperial");
        urlBuilder.addQueryParameter("key", "Nothing");

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            jsonMap = gson.fromJson(response.body().string(), jsonMap.getClass());
            System.out.println(jsonMap);

            ArrayList<Object> rows = (ArrayList<Object>) jsonMap.get("rows");
            Map<String, Object> rowsHashMap = (Map<String, Object>) rows.get(0);

            ArrayList<Object> elements = (ArrayList<Object>) rowsHashMap.get("elements");
            Map<String, Object> elementsHashMap = (Map<String, Object>) elements.get(0);
            Map<String, Object> duration = (Map<String, Object>) elementsHashMap.get("duration");

            System.out.println(duration.get("text"));
            System.out.println(response.body().string());
        } catch (IOException e) {
            System.out.println("There was an error in the request");
            throw new RuntimeException(e);
        }


        System.out.println("Exiting Application!");
    }
}