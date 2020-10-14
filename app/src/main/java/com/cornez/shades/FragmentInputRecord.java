package com.cornez.shades;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FragmentInputRecord extends Fragment {
    private EditText name;
    private Button send;
    private RecordDataBase helper;
    private int time;
    private int id;
    private Data listener;
    private FragmentManager fragmentManager;
    private int stopList = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_record, container, false);
        listener.writeStopList(stopList);
        id = listener.getId();
        time = listener.getTime();
        name = view.findViewById(R.id.editext);
        send = view.findViewById(R.id.button);
        send.setOnClickListener(sendListener);
        helper = listener.getHelper();
        return view;
    }

    private View.OnClickListener sendListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!TextUtils.isEmpty(name.getText())) {
                fragmentManager = getFragmentManager();
                if(helper.getCount() < 5)
                    helper.addRecord(new RecordTable(helper.getCount(), name.getText().toString(), time));
                else
                    helper.editRecord(new RecordTable(id, name.getText().toString(), time));
                stopList = 0;
                listener.writeStopList(stopList);
                fragmentManager.popBackStack();
            }
        }
    };

    public interface Data {
        public RecordDataBase getHelper();
        public int getId();
        public int getTime();
        public void writeStopList(int stopList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Data)
            listener = (Data) getActivity();
    }
}
