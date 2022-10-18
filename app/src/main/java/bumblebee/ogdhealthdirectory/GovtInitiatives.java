package bumblebee.ogdhealthdirectory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class GovtInitiatives extends AppCompatActivity {
    GovtSchemesAdapter govtSchemesAdapter;
    ProgressDialog pDialog;
    //ArrayList<GovtSchemeObjectClass> govtSchemeObjectClassArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_initiatives);
        new GovtAsyncTask().execute();
    }

    public void updateUI(ArrayList<GovtSchemeObjectClass> arrayList){
        ListView l = (ListView) findViewById(R.id.govt_init_listview);
        govtSchemesAdapter = new GovtSchemesAdapter(this, arrayList);
        l.setAdapter(govtSchemesAdapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = govtSchemesAdapter.getItem(position).getDetails() + "\n";
                String arr[] = {s};
                AlertDialog.Builder builder = new AlertDialog.Builder(GovtInitiatives.this);
                builder.setTitle(govtSchemesAdapter.getItem(position).getName())
                        .setItems(arr, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }

                            ;
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private class GovtAsyncTask extends AsyncTask<URL,Void,ArrayList<GovtSchemeObjectClass>> {
        ArrayList<GovtSchemeObjectClass> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    GovtInitiatives.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<GovtSchemeObjectClass> doInBackground(URL... urls) {
            ArrayList<GovtSchemeObjectClass> list = new ArrayList<>();

            // Create URL object
            URL url = createUrl("https://b5952f18.ngrok.io/services");
            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Extract relevant fields from the JSON response and create an {@link Service} object
            list = extractFeatureFromJson(jsonResponse);

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<GovtSchemeObjectClass> govtSchemeObjectClassArrayList) {
            pDialog.dismiss();
            updateUI(govtSchemeObjectClassArrayList);
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                exception.printStackTrace();
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<GovtSchemeObjectClass> extractFeatureFromJson(String govtJSON) {
            try {
                JSONObject baseJsonResponse = new JSONObject(govtJSON);
                   ArrayList<GovtSchemeObjectClass> list = new ArrayList<>();
                    JSONArray itemArray = baseJsonResponse.getJSONArray("services");
                    // If there are results in the item array
                    if (itemArray.length() > 0) {
                        for(int i=0;i<itemArray.length();i++) {
                            // Extract out the first service
                            JSONObject obj = itemArray.getJSONObject(i);

                            String title = obj.getString("service-name");
                            String description = obj.getString("details");


                            GovtSchemeObjectClass govtSchemeObjectClass = new GovtSchemeObjectClass(title, description);
                            list.add(govtSchemeObjectClass);
                        }
                    }
                    return list;

            } catch (JSONException e) {

                Toast.makeText(GovtInitiatives.this,"No Results Found",Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
