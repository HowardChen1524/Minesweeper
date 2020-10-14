package com.cornez.shades;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//My imports
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.graphics.Color;
import android.app.Activity;

public class FragmentGame extends Fragment {
    private Data listener;
    private int firstClick;
    private static int count = 0;
    private StepOnMine som;
    private Button arr[];
    private Button reset;
    private TextView winOrlose;
    private TextView txtView;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private Handler handler;
    private static final int UPDATE_TEXTVIEW = 0;
    private RecordDataBase helper;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        som = new StepOnMine();
        arr = new Button[25];
        for (int i = 0; i < 25; i++) {
            arr[i] = (Button) view.findViewById(getResources().getIdentifier("button" + i, "id", getActivity().getPackageName()));
            arr[i].setBackgroundColor(Color.GRAY);
            arr[i].setOnClickListener(btnListener);
        }
        reset = (Button) view.findViewById(R.id.button25);
        reset.setOnClickListener(resetListener);
        winOrlose = (TextView) view.findViewById(R.id.textview);
        txtView = (TextView) view.findViewById(R.id.txtView);

        helper = listener.getHelper();
        firstClick = 1;

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXTVIEW:
                        txtView.setText(count + " Sec");
                        break;
                    default:
                        break;
                }
            }
        };

        return view;
    }

    public void sendMessage(int id){
        if (handler != null) {
            Message message = Message.obtain(handler, id);
            handler.sendMessage(message);
        }
    }

    private View.OnClickListener btnListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (firstClick == 1){
                startTimer();
                firstClick = 0;
            }
            for(int i = 0, q; i < 25; i++) {
                if (v.getId() == arr[i].getId())
                    if(som.getState(i) == 1) {
                        q = i/5;
                        if(i-5-1 >= (q-1)*5 && q > 0)
                            display(i-5-1);
                        if(q > 0)
                            display(i-5);
                        if(i-5+1 < q*5 && q > 0)
                            display(i-5+1);
                        if(i-1 >= q*5)
                            display(i-1);
                        if(i+1 < (q+1)*5)
                            display(i+1);
                        if(i+5-1 >= (q+1)*5 && q < 4)
                            display(i+5-1);
                        if(q < 4)
                            display(i+5);
                        if(i+5+1 < (q+2)*5 && q < 4)
                            display(i+5+1);
                        display(i);
                    }
                    else if(som.getState(i) == 2) {
                        display(i);
                    }
                    else if(som.getState(i) == 3){
                        display(i);
                        gameover(true);
                    }
            }
        }
    };

    private View.OnClickListener resetListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            som = null;
            som = new StepOnMine();
            count = 0;
            firstClick = 1;
            txtView.setText("0 Sec");
            stopTimer();
            for(int i = 0; i < 25; i++) {
                arr[i].setBackgroundColor(Color.GRAY);
                arr[i].setText(R.string.empty);
                winOrlose.setText(R.string.empty);
            }
        }
    };

    public void startTimer(){
        if(timer == null)
            timer = new Timer();
        if(timerTask == null) {
            timerTask = new TimerTask () {
                @Override
                public void run () {
                    sendMessage(UPDATE_TEXTVIEW);
                    count++;
                }
            };
        }
        if(timer != null && timerTask != null)
            timer.schedule(timerTask, 1000, 1000);
    }

    public void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void display(int pos){
        arr[pos].setText(som.getSign(pos));
        arr[pos].setBackgroundColor(Color.WHITE);
        if(som.getState(pos) != 3)
            som.open(pos);
        isWin();
    }

    public void gameover(boolean boom)
    {
        stopTimer();
        if(boom == true)
            winOrlose.setText(R.string.lose);
        else {
            winOrlose.setText(R.string.win);
            fragmentManager = getFragmentManager();
            transaction = fragmentManager.beginTransaction();
            if(helper.getCount() < 5){
                FragmentInputRecord inputRecord = new FragmentInputRecord();
                listener.writeTime(count);
                if (!inputRecord.isAdded())
                    transaction.add(R.id.fragment2,inputRecord).hide(this).addToBackStack(null).commit();
            }
            else{
                RecordTable max = helper.getMaxTime();
                if(count < max.getTime()){
                    FragmentInputRecord inputRecord = new FragmentInputRecord();
                    int id = max.getId();
                    listener.writeTime(count);
                    listener.writeId(id);
                    if (!inputRecord.isAdded())
                        transaction.add(R.id.fragment2,inputRecord).hide(this).addToBackStack(null).commit();
                }
            }
        }

        if(helper.getCount() == 0)
            txtView.setText("Top: No Record  You: " + count + " Sec");
        else {
            RecordTable min = helper.getMinTime();
            txtView.setText("Top: " + min.getTime() + " Sec  You: " + count + " Sec");
        }
        som.stop();
    }

    public void isWin(){
        for(int i = 0, j = 0; i < 25; i++) {
            if (som.getState(i) == 0)
                j++;
            if (j == 23)
                gameover(false);
        }
    }

    public interface Data {
        public RecordDataBase getHelper();
        public void writeId(int id);
        public void writeTime(int time);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Data)
            listener = (Data) getActivity();
    }

}
