package com.example.priyankasabhagani.assessment1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private boolean forceUpdate;
    private TextView tvResultMsg;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] arrMainActivityList = {"one", "two", "three"};
        ArrayAdapter adapterMainActivity = new ArrayAdapter(this, R.layout.main_listview, arrMainActivityList);

        //This line is changed from ListView listview = ....... to listview = ....
        //This is called converting a "local" variable to "field"
        //If listview was declared a local variable , we wont be able reference it outside this onCreate function
        //Since we will be using these views in other sections of this class, we convert them to field variables(declared on top of the class)
        listView = (ListView) findViewById(R.id.main_activity_list);

        //This is a text view on the centre of the screen which will be updated after our network call
        //the default state of this is set to invisible in out XML
        tvResultMsg = (TextView) findViewById(R.id.tv_result_main_activity);

        listView.setAdapter(adapterMainActivity);

        //This is an Async Task that we've created as an inner class below (not recommended, you will see later)
        //We create an object of this asyncTask and call the "execute" function on it to start it
        //Any parameters or info which we need to run this task should be sent inside the "execute(....)" function
        ForceUpdateTask forceUpdateTask = new ForceUpdateTask();
        // our asynctask needs a "String: current_version" as its first parameter (check the class below)
        forceUpdateTask.execute("1");

        //// TODO: ALERT!! If you dont get any result after this:
        //CHECK THE LOGCAT FOR EXCEPTION
        //AND CHECK THE ANDROID_MANIFEST.xml file


    }


    //an asyncTask is defined as AsyncTask<REQUEST_PARAMETERS, LOADING_SCREEN_PARAMETER, RESULT_PARAMETER>
    //this means: REQUEST PARAM can be many strings: example: version number,pincode,etc,
    //loading screen param means: while asynctask is running and not finshed, a loading progress can be disaplyed, what to u want to show in this progress,
    // eg: if %age completed, u will pass a String called percentage_completed and it will look like AsyncTask<req..,String,respoonse...>
    private class ForceUpdateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //Here we do all the work of the task
            //In this example: we check if the app version is old, if so , we show a Force update message


            //// TODO: ALERT!! You do not need to by heart the below code as it is readily available on stackoverflow
            ////TODO: BUT! You need to understand the steps taken to establish a connection,its life cycle, and what happends when u do a network call


            //Step 1:
            //Create the objects that u will need later for making a connection

            //httpurlconnection is an old class which handles HTTP call like GET request,POST request, etc

            HttpURLConnection urlConnection = null;
            //Buffered reader is a placeholder to save our api's response text so we can use it later and parse it
            BufferedReader reader = null;

            try {

                //Here we extract the parameter passed during "forceUpdateTask.execute("1")"
                String version = params[0];
                URL url = new URL("http://api2.aasaanjobs.com/api/v4/app_version_status/?user_type=CA&version_code=" + version);

//            //Step 2:
                //Make a url like above and open a connection to it like below
                urlConnection = (HttpURLConnection) url.openConnection();
                //Set the request method eg: GET,POST,PATCH etc
                urlConnection.setRequestMethod("GET");


                //this starts the connection and retrieves the result
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                //this formats the response into seperate lines (not needed but ok)
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }


                // TODO: IMP!! THIS IS WHERE WE FINISH THE BACKROUND TASK and return the resonse to postExectue, if all goes well
                return buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }
            //The finally block is run no matter if try is successful or fails, so that
            // we can delete the objects created in step 1 to free space
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Z", "Error closing stream", e);
                    }
                }
            }
        }


        //THIS FUNCTION IS THE ONE THAT WORKS WITH THE RESULT
        //The doInBackground cannot access any other code except its parameters ,eg: the version code
        //but after its completed, the onPost
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //if response is empty ? dont do json parsing on it
            if (s == null || s.length() <= 0) {
                Log.e("Z", "Empty response!");
                //code will come out of this function and anything written below will not execute after return;
                return;
            }


            //Ye hai json parsing. very easy.
            try {
                JSONObject response = new JSONObject(s);
                forceUpdate = response.getBoolean("force_update");

                Log.e("Z", "showResultOnUI: API call was made and JSON Parsed: force_update field was found to be: " + forceUpdate);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (forceUpdate) {

                //This is an example of how u can modify ui based on asynctask
                //we write code to update ui inside postExecute like this
                showResultOnUI();
            }


        }
    }

    private void showResultOnUI() {

        //Since we made textview a field class, it is globally available
        //And since our asynctask is also inside this class(inner class), we can easily access our textview
        tvResultMsg.setText("OMG! It works :P :P");
        tvResultMsg.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }
}
