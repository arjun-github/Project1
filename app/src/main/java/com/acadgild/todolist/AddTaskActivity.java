package com.acadgild.todolist;

import android.app.Activity;
import android.content.Intent;
import java.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddTaskActivity extends Activity implements OnClickListener {

    private Button addTodoBtn;
    private Button clearTodoBtn;
    private EditText subjectEditText;
    private EditText descEditText;
    private DatePicker dpResult;
    private String Date;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Loading main Layout Page
        setContentView(R.layout.activity_add_record);

        subjectEditText = (EditText) findViewById(R.id.subject_edittext);
        descEditText = (EditText) findViewById(R.id.description_edittext);
        dpResult = (DatePicker) findViewById(R.id.datePicker2);
        addTodoBtn = (Button) findViewById(R.id.add_record);
        clearTodoBtn = (Button) findViewById(R.id.clear);

        dbManager = new DBManager(this);
        dbManager.open();
        //Setting up on click listner for adding data in database and  to reset the page
        addTodoBtn.setOnClickListener(this);
        clearTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_record:
                // Checking that the editText is not empty and minimum characters are entered
                if( subjectEditText.getText().toString().length() == 0 )
                    subjectEditText.setError( "Title is required!" );
                else if( subjectEditText.getText().toString().length() <= 3 )
                    subjectEditText.setError( "Title is too short!" );
                else if( descEditText.getText().toString().length() == 0 )
                    descEditText.setError( "Description is required!" );
                else if( descEditText.getText().toString().length() <= 5 )
                    descEditText.setError( "Description is too short!" );
                else {
                // Getting the date from the date Picker
                    int day = dpResult.getDayOfMonth();
                    int month = dpResult.getMonth()+1;
                    int year =  dpResult.getYear();
                    String monthString ="" + month;
                    String dayString="" + day;

                    if(month < 10){
                        monthString = "0" + month;
                    }
                    if(day < 10){
                        dayString = "0" + day;
                    }
                    Date=year+"-"+monthString+"-"+dayString;
                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String status="0";
               //Inserting data in Database
                dbManager.insert(name, desc, Date, status);

                Intent main = new Intent(AddTaskActivity.this, TaskListActivity.class)
                       .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

               startActivity(main);
                }
                break;

            case R.id.clear:
                //Reset the editTexts and Date picker
                subjectEditText.setText("");
                descEditText.setText("");
                Calendar calendar = Calendar.getInstance();
                dpResult.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
    }
}