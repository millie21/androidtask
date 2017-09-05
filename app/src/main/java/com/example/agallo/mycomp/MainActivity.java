package com.example.agallo.mycomp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText TaskName, TaskTime, TaskStatus;
    Button RegisterTask, ShowTasks;
    String TaskNameHolder, TaskTimeHolder, TaskStatusHolder;
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://seedorf.000webhostapp.com/mycollabo/mycreate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskName = (EditText)findViewById(R.id.editName);
        TaskTime = (EditText)findViewById(R.id.editPhoneNumber);
        TaskStatus = (EditText)findViewById(R.id.editClass);

        RegisterTask = (Button)findViewById(R.id.buttonSubmit);
        ShowTasks = (Button)findViewById(R.id.buttonShow);

        RegisterTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    TaskRegistration(TaskNameHolder,TaskTimeHolder, TaskStatusHolder);

                }
                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(MainActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });

        ShowTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),ShowAllStudentsActivity.class);

                startActivity(intent);

            }
        });
    }

    public void TaskRegistration(final String T_Name, final String T_Time, final String T_Status){

        class StudentRegistrationClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(MainActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("TaskName",params[0]);

                hashMap.put("TaskTime",params[1]);

                hashMap.put("TaskStatus",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StudentRegistrationClass studentRegistrationClass = new StudentRegistrationClass();

        studentRegistrationClass.execute(T_Name,T_Time,T_Status);
    }


    public void CheckEditTextIsEmptyOrNot(){

        TaskNameHolder = TaskName.getText().toString();
        TaskTimeHolder = TaskTime.getText().toString();
        TaskStatusHolder = TaskStatus.getText().toString();

        if(TextUtils.isEmpty(TaskNameHolder) || TextUtils.isEmpty(TaskTimeHolder) || TextUtils.isEmpty(TaskStatusHolder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }
}
