package com.cornez.shades;

import android.view.View;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

//My imports
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.List;

public class MyListFragment extends Fragment {
    private OnItemSelectedListener listener;
    private String information;
    //My
    private ListView listView;
    private MyListAdapter listAdapter;
    private List<Hero> heroList = new ArrayList<Hero>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cover, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        heroList.add(new Hero("Start game"));
        heroList.add(new Hero("Record"));
        heroList.add(new Hero("Help"));
        heroList.add(new Hero("About"));
        listAdapter = new MyListAdapter(getActivity(), R.layout.custome_list, heroList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(onClickListView);

        return view;
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position)
            {
                case 0://start
                    if(listener.getStopList() == 0)
                        listener.getListChoose(0);
                    break;
                case 1://record
                    if(listener.getStopList() == 0)
                        listener.getListChoose(1);
                    break;
                case 2://help
                    if(listener.getStopList() == 0)
                        listener.getListChoose(2);
                    break;
                case 3://about
                    if(listener.getStopList() == 0)
                        listener.getListChoose(3);
                    break;
            }
        }
    };

    public interface OnItemSelectedListener {
        public void getListChoose(int choose);
        public int getStopList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener)
            listener = (OnItemSelectedListener) getActivity();
    }

}
