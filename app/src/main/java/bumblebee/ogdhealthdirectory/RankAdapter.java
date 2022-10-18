package bumblebee.ogdhealthdirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by suhassangangire on 18/10/22.
 */

public class RankAdapter extends ArrayAdapter<RankObj> {

    public RankAdapter(Context context, ArrayList<RankObj> venues) {
        super(context, 0, venues);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.rank_list_item, parent, false);
        }

        RankObj rankObj = getItem(position);
        TextView t = (TextView) listItemView.findViewById(R.id.hospi_name_rank_textview);
        t.setText(rankObj.getName());

        TextView desc_eng = (TextView) listItemView.findViewById(R.id.rank_textview);
        desc_eng.setText(rankObj.getStars());

        return listItemView;
    }
}