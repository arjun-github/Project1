package com.acadgild.todolist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ChangeStatus extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBManager dbManager;
        dbManager = new DBManager(this);
        dbManager.open();
        // Getting intent from the TaskListActivity OnLongClickListner method
        Intent intent = getIntent();
        String task_status = intent.getStringExtra("status");
        String hidden_id = intent.getStringExtra("id");
        //Changing the status of the task
        dbManager.status(task_status, hidden_id);
        this.returnHome();
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), TaskListActivity.class);
        finish();
        startActivity(home_intent);

    }
}
