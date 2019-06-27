package com.codemetrictech.taskgetter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Main";

    private ArrayList<Task> mTaskList = new ArrayList<>();
    private TaskDisplayFragment taskDisplayFragment = new TaskDisplayFragment(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, taskDisplayFragment, "TaskDisplayFragment")
                .addToBackStack(null)
                .commit();
        //begin fetch tasks.
        pullTasks(null);
    }

    public void pullTasks(String query){
        taskDisplayFragment.removeTaskCards();
        if (query != null && !query.equals("")){
            new TaskGetter(taskDisplayFragment, query).execute(query);
            return;
        }
        new TaskGetter(taskDisplayFragment).execute();
    }

    public static class TaskGetter extends AsyncTask<String, Void, Void>{
        private final String TAG = "fetchTasks";
        private TaskDisplayFragment taskDisplayFragment;
        private String query = "";

        TaskGetter(TaskDisplayFragment taskDisplayFragment) {
            this.taskDisplayFragment = taskDisplayFragment;
        }

        TaskGetter(TaskDisplayFragment taskDisplayFragment, String query) {
            this.taskDisplayFragment = taskDisplayFragment;
            this.query = query;
        }

        @Override
        protected Void doInBackground(String... strings) {
            TaskFetcher mTaskFetcher = new TaskFetcher(taskDisplayFragment);
            mTaskFetcher.fetchItems_volley(strings);
            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Fetching tasks...");
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "Completed fetching tasks.");
        }
    }
}
