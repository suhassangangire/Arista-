package bumblebee.ogdhealthdirectory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class Ranking extends AppCompatActivity {
    RankAdapter rankAdapter;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

    new RankAsyncTask().execute();
    }

    public void updateUI(ArrayList<RankObj> arrayList){
        ListView l = (ListView) findViewById(R.id.ranking_listview);
        rankAdapter = new RankAdapter(this, arrayList);
        l.setAdapter(rankAdapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s1 = "Cleanliness rating: " + rankAdapter.getItem(position).getCleanliness() + "\n";
                String s2 = "Facilities rating: " + rankAdapter.getItem(position).getFacilities() + "\n";
                String s3 = "Doctors Behaviour rating: " + rankAdapter.getItem(position).getDoc_behaviour() + "\n";
                String s4 = "Quality of service rating: " + rankAdapter.getItem(position).getQuality_of_service() + "\n";

                String arr[] = {s1, s2, s3, s4};
                AlertDialog.Builder builder = new AlertDialog.Builder(Ranking.this);
                builder.setTitle(rankAdapter.getItem(position).getName())
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

    private class RankAsyncTask extends AsyncTask<URL,Void,ArrayList<RankObj>> {
        ArrayList<RankObj> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    Ranking.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<RankObj> doInBackground(URL... urls) {
            ArrayList<RankObj> list = new ArrayList<>();

            // Create URL object
            URL url = createUrl("https://b5952f18.ngrok.io/hospital");
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
        protected void onPostExecute(ArrayList<RankObj> rankObjArrayList) {
            pDialog.dismiss();
            updateUI(rankObjArrayList);
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

        private ArrayList<RankObj> extractFeatureFromJson(String govtJSON) {
            try {
                //JSONObject baseJsonResponse = new JSONObject(govtJSON);
                ArrayList<RankObj> list = new ArrayList<>();
                JSONArray itemArray = new JSONArray(govtJSON);
                // If there are results in the item array
                if (itemArray.length() > 0) {
                    for(int i=0;i<itemArray.length();i++) {
                        // Extract out the first service
                        JSONObject obj = itemArray.getJSONObject(i);

                        String title = obj.getString("name");
                        double avg = (Double.parseDouble(obj.getString("cleanliness")) + Double.parseDouble(obj.getString("facilities"))
                                + Double.parseDouble(obj.getString("doc-behaviour")) + Double.parseDouble(obj.getString("quality-of-service"))) / 4;
                        String description = avg + "";


                        RankObj govtSchemeObjectClass = new RankObj(title, description, obj.getString("cleanliness"), obj.getString("facilities"), obj.getString("doc-behaviour"), obj.getString("quality-of-service"));
                        list.add(govtSchemeObjectClass);
                    }
                }
                return list;

            } catch (JSONException e) {

                Toast.makeText(Ranking.this,"No Results Found",Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
