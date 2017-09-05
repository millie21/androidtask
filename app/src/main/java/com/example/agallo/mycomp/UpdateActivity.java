package com.example.agallo.mycomp;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    String HttpURL = "https://seedorf.000webhostapp.com/mycollabo/myupdate.php";
    ProgressDialog progressDialog;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    EditText TaskName, TaskTime, TaskStatus;
    Button UpdateTask;
    String IdHolder, TaskNameHolder, TaskTimeHolder, TaskStatusHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        TaskName = (EditText)findViewById(R.id.editName);
        TaskTime = (EditText)findViewById(R.id.editPhoneNumber);
        TaskStatus = (EditText)findViewById(R.id.editClass);

        UpdateTask = (Button)findViewById(R.id.UpdateButton);

        // Receive Student ID, Name , Phone Number , Class Send by previous ShowSingleRecordActivity.
        IdHolder = getIntent().getStringExtra("task_id");
        TaskNameHolder = getIntent().getStringExtra("taskname");
        TaskTimeHolder = getIntent().getStringExtra("tasktime");
        TaskStatusHolder = getIntent().getStringExtra("taskstatus");

        // Setting Received Student Name, Phone Number, Class into EditText.
        TaskName.setText(TaskNameHolder);
        TaskTime.setText(TaskTimeHolder);
        TaskStatus.setText(TaskStatusHolder);

        // Adding click listener to update button .
        UpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();

                // Sending Student Name, Phone Number, Class to method to update on server.
                StudentRecordUpdate(IdHolder,TaskNameHolder, TaskTimeHolder, TaskStatusHolder);
            }
        });
    }
    // Method to get existing data from EditText.
    public void GetDataFromEditText(){

        TaskNameHolder = TaskName.getText().toString();
        TaskTimeHolder = TaskTime.getText().toString();
        TaskStatusHolder = TaskStatus.getText().toString();

    }

    // Method to Update Student Record.
    public void StudentRecordUpdate(final String task_id, final String T_Name, final String T_Time, final String T_Status){

        class StudentRecordUpdateClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("task_id",params[0]);

                hashMap.put("taskname",params[1]);

                hashMap.put("tasktime",params[2]);

                hashMap.put("taskstatus",params[3]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(task_id,T_Name,T_Time,T_Status);
    }
}
