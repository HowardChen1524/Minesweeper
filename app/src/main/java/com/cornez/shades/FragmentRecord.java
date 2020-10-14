package com.cornez.shades;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


public class FragmentRecord extends Fragment {

    private Data listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        super.onCreate(savedInstanceState);

        RecordDataBase helper = listener.getHelper();

        if(helper.getCount() != 0) {
            TextView recordText[] = new TextView[5];
            for (int i = 0; i < helper.getCount(); i++)
                recordText[i] = view.findViewById(getResources().getIdentifier("record" + i, "id", getActivity().getPackageName()));
            ArrayList<RecordTable> records = helper.getOrderRecords();
            for (int i = 0; i < helper.getCount(); i++)
                recordText[i].setText(records.get(i).getName() + " " + records.get(i).getTime());
        }
        return view;

    }

    public interface Data {
        public RecordDataBase getHelper();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Data)
            listener = (Data) getActivity();
    }
}
