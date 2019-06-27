package com.codemetrictech.taskgetter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import volley.AppController;
import volley.Config_URL;

public class TaskFetcher {

    private final String TAG = "TaskFetcher";
    private TaskDisplayFragment hostFragment;
    private List<Task> items = new ArrayList<>();

    public TaskFetcher(TaskDisplayFragment hostFragment) {
        this.hostFragment = hostFragment;
    }

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

    public void addTasksFromJson(String jsonString, String query) throws JSONException {
        Log.i(TAG, "Received JSON: " + jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonTaskArray = jsonObject.getJSONArray("tasks");

        for (int i = 0; i < jsonTaskArray.length(); i++) {
            Log.i(TAG, "QUERY: " + query);

            if(!query.equals("")){
                String taskString = jsonTaskArray.optJSONObject(i).get("title").toString();

                //Check if query exists in task i using pattern.
                System.out.println(taskString + ":" + query);
                if(taskString.toLowerCase().contains(query.toLowerCase())){
                    Log.d(TAG, "Pattern match");
                    gsonAddTaskCard(jsonTaskArray.get(i));
                }
            } else {
                gsonAddTaskCard(jsonTaskArray.get(i));
            }
        }
    }

    private void gsonAddTaskCard(Object jsonObj){
        Gson mGson = new Gson();
        Task task = mGson.fromJson(jsonObj.toString(), Task.class);
        items.add(task);
        hostFragment.addTaskCard(task);
        Log.i(TAG, "Added task: " + jsonObj + " " + task.getTitle());
    }


    void fetchItems_volley(final String[] queries){
    // Tag used to cancel the request
        String tag_string_req = "req_tasks";
        String qu = "";
        if(queries.length > 0){
            qu = queries[0];
        } else {
             qu = "";
        }
        final String query = qu;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Config_URL.URL_TASKS,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Fetch tasks Response: " + response);
                try {
                    addTasksFromJson(response, query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getMessage() != null){
                    Log.e(TAG, error.getMessage());
                } else {
                    Log.e(TAG, "Error, check if server is running!");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(hostFragment.getContext());
                builder.setMessage(hostFragment.getString(R.string.error_CONNECTION));
                builder.setNegativeButton("Exit Application", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hostFragment.getHostActivity().finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Snackbar.make(hostFragment.getView(), "Connection error.", Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> mparams = new HashMap<String, String>();
                mparams.put("query", queries[0]);
                return mparams;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


}
