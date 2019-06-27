package com.codemetrictech.taskgetter;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TaskFetcher {

    private final String TAG = "TaskFetcher";
    private final URLConfig config = new URLConfig();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        Log.d(TAG, Integer.toString(connection.getResponseCode()));
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        byte[] byteArray = getUrlBytes(urlSpec);
        System.out.println(byteArray.length);
        System.out.println("ARRAY: " + byteArray);
        return new String(byteArray);
    }

    public List<Task> fetchItems() {
        String url = config.TASKS_URL;
        List<Task> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonTaskArray = jsonObject.getJSONArray("tasks");

            for (int i = 0; i < jsonTaskArray.length(); i++) {
                Gson mGson = new Gson();
                Task task = mGson.fromJson(jsonTaskArray.get(i).toString(), Task.class);
                items.add(task);
                Log.i(TAG, "Added task: " + i + " " + task.getTitle());
            }

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }}
