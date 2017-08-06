package com.acadgild.todolist;



import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import android.support.v7.app.AppCompatActivity;

public class TaskListActivity extends AppCompatActivity {

    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    // Defining String and int array to get the data and link it with respective element in layout
    final String[] from = new String[]{DatabaseHelper.DATE, DatabaseHelper.SUBJECT, DatabaseHelper.DESC, DatabaseHelper.DATE, DatabaseHelper.STATUS, DatabaseHelper._ID,DatabaseHelper.STATUS};
    final int[] to = new int[]{R.id.dateTitle, R.id.title, R.id.description, R.id.date, R.id.status, R.id.hidden_id, R.id.textView};


    // Refreshing activity when returning from other activity
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        // Loading layout page
        setContentView(R.layout.task_list);

        //Creating a new instance of DBManager
        dbManager = new DBManager(this);
        dbManager.open();
        //Fetching the data and putting it in cursor
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        TextView emptyView=(TextView)findViewById(R.id.textView2);


        //linking adapter with cursor ,from and to
        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to);
        //if adapter is empty , make the emptyView textView visible
       if (adapter.isEmpty())
       {
           emptyView.setVisibility(View.VISIBLE);
       }

        // Using setViewBinder method to change the complete and incomplete icon based on the status from the Database
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {


            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if (view.getId() == R.id.textView) {
                    String status = cursor.getString(4);
                    if (status.equals("1")) {
                        TextView txt = (TextView) view;
                        txt.setBackgroundResource(R.drawable.complete);}
                    else if (status.equals("0")) {
                        TextView txt = (TextView) view;
                        txt.setBackgroundResource(R.drawable.incomplete);}
                    return true;
                }
        // Changing the date in general format ie, day month year
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


        // OnCLickListiner For List Items opens new dialog activity for update and delete of task
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {

                TextView idTextView = (TextView) view.findViewById(R.id.hidden_id);
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView descTextView = (TextView) view.findViewById(R.id.description);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyTaskActivity.class);
                //Using putExtra method to send and receive data and show it in next activity
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("id", id);
                startActivity(modify_intent);
            }
        });


    // OnItemLongCLickListiner For Changing Status of the task
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idTextView = (TextView) view.findViewById(R.id.hidden_id);
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView status = (TextView) view.findViewById(R.id.status);

                String hidden_id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String task_status = status.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Objects.equals(task_status, "0"))
                        task_status = "1";
                    else
                        task_status = "0";
                }

                Intent modify_intent = new Intent(getApplicationContext(), ChangeStatus.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                modify_intent.putExtra("status", task_status);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("id", hidden_id);
                finish();
                startActivity(modify_intent);
                return true;
            }
        });
    }

    //Inflating option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Opening Activity based on options selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {
            Intent add_mem = new Intent(this, AddTaskActivity.class);
            startActivity(add_mem);
        }
        if (id == R.id.completed) {
            Intent add_mem = new Intent(this, CompletedTask.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;

            startActivity(add_mem);


        }

        return super.onOptionsItemSelected(item);
    }

}