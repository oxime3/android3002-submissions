package com.codemetrictech.taskgetter;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class URLConfig {

    // TODO: 6/22/2019 Replace with the IP address of your server runs on.
    //  Localhost will not work if using emulator.
    String ip = "http://192.168.x.xxx";
    String port = ":5000";


    String HOME_URL = (ip + port).trim();
    String TASKS_URL = HOME_URL + "/todo/api/v1.0/tasks";

}
