package bumblebee.ogdhealthdirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MedicineAdapter extends ArrayAdapter<Medicine> {

    public MedicineAdapter(Context context, ArrayList<Medicine> venues) {
        super(context, 0, venues);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.medicine_list_item, parent, false);
        }

        Medicine medicine = getItem(position);
        TextView t = (TextView) listItemView.findViewById(R.id.medicine_name);
        t.setText(medicine.getName());

        TextView desc_eng = (TextView) listItemView.findViewById(R.id.medicine_description);
        desc_eng.setText(medicine.getDetailsHindi());

        return listItemView;
    }
}
