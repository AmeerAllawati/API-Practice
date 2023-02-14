package org.apiexample;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GoogleAPI {
    /***
     *
     * @param args: the first part is the origin and the second part is the destination.
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws IOException {

        Maps googleMaps = new Maps();
        Gson gson = new Gson();
        FileReader jsonFile = new FileReader("gmap_distance_matrix_response.json");
        Map<String, Object> jsonMap = new HashMap<>();


        // CommandLine arguments example: "TRA, Muscat" "Al Khuwair North"
        if (args.length != 2) {
            System.out.println("You have to put two command line inputs");
            throw new IOException();
        }
        String origin = args[0];
        String dropOff = args[1];

        HttpUrl.Builder urlBuilder = HttpUrl.parse(googleMaps.distanceMatrixURL).newBuilder();
        urlBuilder.addQueryParameter("origins", origin);
        urlBuilder.addQueryParameter("destinations", dropOff);
        urlBuilder.addQueryParameter("units", "imperial");
        urlBuilder.addQueryParameter("key", googleMaps.apiKey);

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
            System.out.println("There was an error in the request, the response failed:" + e.toString());

            throw new RuntimeException(e);
        }


        System.out.println("Exiting Application!");
    }
}