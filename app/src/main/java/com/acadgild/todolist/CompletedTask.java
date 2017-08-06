package com.acadgild.todolist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
public class CompletedTask extends Activity {

    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    // Defining String and int array to get the data and link it with respective element in layout
    final String[] from = new String[] {DatabaseHelper.DATE, DatabaseHelper.SUBJECT, DatabaseHelper.DESC, DatabaseHelper.DATE, DatabaseHelper.STATUS, DatabaseHelper._ID};
    final int[] to = new int[] {R.id.dateTitle,  R.id.title, R.id.description, R.id.date, R.id.status , R.id.hidden_id};


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Loading Layout page
        setContentView(R.layout.task_list);

        dbManager = new DBManager(this);
        dbManager.open();
        //fetching completed task data and putting it in cursor
        Cursor cursor = dbManager.fetch_complete();

        listView = (ListView) findViewById(R.id.list_view);
        //listView.setEmptyView(findViewById(R.id.empty));

         TextView emptyView=(TextView)findViewById(R.id.textView2);

        //linking adapter with cursor ,from and to
        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        //if adapter is empty , make the emptyView textView visible
        if (adapter.isEmpty()){
                    emptyView.setText("No Completed Task");
                    emptyView.setVisibility(View.VISIBLE);
        }
        // Using setViewBinder method to Change the date in general format ie, day month year
        adapter.setViewBinder(new android.widget.SimpleCursorAdapter.ViewBinder() {


            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if (view.getId() == R.id.date) {
                    String d = cursor.getString(3);
                    String[] parts = d.split("-");
                    String year = parts[0];
                    String month = parts[1];
                    String day = parts[2];
                    String newDate=day+"/"+month+"/"+year;
                    TextView date = (TextView) view;
                    date.setText(newDate);
                    return true;
                }
                if (view.getId() == R.id.dateTitle) {
                    String d = cursor.getString(3);
                    String[] parts = d.split("-");
                    String year = parts[0];
                    String month = parts[1];
                    String day = parts[2];
                    String newDate=day+"/"+month+"/"+year;
                    TextView date1 = (TextView) view;
                    date1.setText(newDate);
                    return true;
                }

                return false;
            }});

        adapter.notifyDataSetChanged();
        //Setting the adapter
        listView.setAdapter(adapter);

        // OnCLickListiner For deletion of the task
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.hidden_id);
                String hidden_id = idTextView.getText().toString();
                // to delete the task
                dbManager.delete(hidden_id);

                Intent intent = getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);

            }


        });

    }
}
