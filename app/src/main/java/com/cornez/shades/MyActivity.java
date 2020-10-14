package com.cornez.shades;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//My import
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;

public class MyActivity extends Activity implements MyListFragment.OnItemSelectedListener,FragmentRecord.Data,FragmentGame.Data,FragmentInputRecord.Data{
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Intent intent;
    private RecordDataBase helper;
    private int id;
    private int time;
    private int stopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        helper = new RecordDataBase(this);
    }

    @Override
    public void getListChoose(int choose) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager = getFragmentManager();
            transaction = fragmentManager.beginTransaction();

            if (choose == 0) {
                FragmentGame fragmentGame = new FragmentGame();
                transaction.replace(R.id.fragment2, fragmentGame).commit();
            } else if (choose == 1) {
                FragmentRecord fragmentRecord = new FragmentRecord();
                transaction.replace(R.id.fragment2, fragmentRecord).commit();
            } else if (choose == 2) {
                FragmentHelp fragmentHelp = new FragmentHelp();
                transaction.replace(R.id.fragment2, fragmentHelp).commit();
            } else if (choose == 3) {
                FragmentAbout fragmentAbout = new FragmentAbout();
                transaction.replace(R.id.fragment2, fragmentAbout).commit();
            }
        }
        else {
            if (choose == 0) {
                intent = new Intent(getApplicationContext(), ActivityGame.class);
                startActivity(intent);
            }
            else if (choose == 1) {
                intent = new Intent(getApplicationContext(), ActivityRecord.class);
                startActivity(intent);
            }
            else if (choose == 2) {
                intent = new Intent(getApplicationContext(), ActivityHelp.class);
                startActivity(intent);
            }
            else if (choose == 3) {
                intent = new Intent(getApplicationContext(), ActivityAbout.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public RecordDataBase getHelper(){
        return helper;
    }

    @Override
    public void writeId(int i){
        id = i;
    }

    @Override
    public void writeTime(int t){
        time = t;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public int getTime(){
        return time;
    }

    @Override
    public void writeStopList(int s){
        stopList = s;
    }

    @Override
    public int getStopList(){
        return stopList;
    }
}
