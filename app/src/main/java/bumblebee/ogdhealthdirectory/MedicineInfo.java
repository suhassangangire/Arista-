package bumblebee.ogdhealthdirectory;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MedicineInfo extends AppCompatActivity {

    private ArrayList<Medicine> medicineArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);

        AssetManager assetManager = getApplicationContext().getAssets();
        try {
            InputStream csvStream = assetManager.open("medfile.csv");
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader reader = new CSVReader(csvStreamReader);
            String[] nextLine;
            int count = 1;
            while ((nextLine = reader.readNext()) != null && count <= 31) {
                if (count == 1)
                {
                    //Toast.makeText(this, "Hello world!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Medicine medicine = new Medicine(nextLine);
                    medicineArrayList.add(medicine);

                }

                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        MedicineAdapter medicineAdapter = new MedicineAdapter(this, medicineArrayList);
        ListView listView = (ListView) findViewById(R.id.medicine_listview);
        listView.setAdapter(medicineAdapter);

    }
}
