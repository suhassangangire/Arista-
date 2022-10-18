package bumblebee.ogdhealthdirectory;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Complaint extends AppCompatActivity {
    ProgressDialog pDialog;
    String name="John", email="john@xyz.com", contact="45487589", detail="Bad services";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        EditText nameEditText = (EditText) findViewById(R.id.person_name);
        name = nameEditText.getText().toString();

        EditText emailEditText = (EditText) findViewById(R.id.person_email);
        email = emailEditText.getText().toString();

        EditText contactEditText = (EditText) findViewById(R.id.person_contact);
        contact = contactEditText.getText().toString();

        EditText detailEditText = (EditText) findViewById(R.id.complaint_description);
        detail = detailEditText.getText().toString();

        Button b = (Button) findViewById(R.id.complaint_submit_button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ComplaintAsyncTask().execute();
            }
        });

    }

    private class ComplaintAsyncTask extends AsyncTask<URL,Void,String> {
        ArrayList<GovtSchemeObjectClass> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    Complaint.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(URL... urls) {

            try {

                URL url = new URL("https://b5952f18.ngrok.io/complaints"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", "abc");
                postDataParams.put("email", "abc@gmail.com");
                postDataParams.put("contact","4574746767");
                postDataParams.put("description","bsdjkakj");
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(postDataParams.toString());

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String r) {
            pDialog.dismiss();
            //Toast.makeText(Complaint.this, "Details Submitted", Toast.LENGTH_SHORT).show();


        }


        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }
}
