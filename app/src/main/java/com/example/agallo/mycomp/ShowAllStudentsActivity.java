package com.example.agallo.mycomp;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowAllStudentsActivity extends AppCompatActivity {

    ListView TaskListView;
    ProgressBar progressBar;
    String HttpUrl = "https://seedorf.000webhostapp.com/mycollabo/mystudents.php";
    List<String> IdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_all_students);

        TaskListView = (ListView)findViewById(R.id.listview1);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        new GetHttpResponse(ShowAllStudentsActivity.this).execute();

        //Adding ListView Item click Listener.
        TaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(ShowAllStudentsActivity.this,ShowSingleRecordActivity.class);

                // Sending ListView clicked value using intent.
                intent.putExtra("ListViewValue", IdList.get(position).toString());

                startActivity(intent);

                //Finishing current activity after open next activity.
                finish();

            }
        });
    }

    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String JSonResult;

        List<Task> taskList;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass(HttpUrl);
            try
            {
                httpServicesClass.ExecutePostRequest();

                if(httpServicesClass.getResponseCode() == 200)
                {
                    JSonResult = httpServicesClass.getResponse();

                    if(JSonResult != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            Task task;

                            taskList = new ArrayList<Task>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                task = new Task();

                                jsonObject = jsonArray.getJSONObject(i);

                                // Adding Student Id TO IdList Array.
                                IdList.add(jsonObject.getString("task_id").toString());

                                //Adding Student Name.
                                task.TaskName = jsonObject.getString("taskname").toString();

                                taskList.add(task);

                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBar.setVisibility(View.GONE);

            TaskListView.setVisibility(View.VISIBLE);

            ListAdapterClass adapter = new ListAdapterClass(taskList, context);

            TaskListView.setAdapter(adapter);

        }
    }
}
