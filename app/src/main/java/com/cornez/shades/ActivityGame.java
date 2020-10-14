package com.cornez.shades;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.graphics.Color;
import android.view.View;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.Button;



public class ActivityGame extends Activity {

    private int firstClick;
    private static int count = 0;
    private StepOnMine som;
    private Button arr[];
    private TextView winOrlose;
    private TextView txtView;
    private Handler handler;
    private static final int UPDATE_TEXTVIEW = 0;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private RecordDataBase helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game);


        som = new StepOnMine();
        arr = new Button[25];
        for (int i = 0; i < 25; i++) {
            arr[i] = (Button) findViewById(getResources().getIdentifier("button" + i, "id", getPackageName()));
            arr[i].setBackgroundColor(Color.GRAY);
        }
        winOrlose = (TextView) findViewById(R.id.textview);
        txtView = (TextView) findViewById(R.id.txtView);

        helper = new RecordDataBase(this);

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
    }

    public void sendMessage(int id){
        if (handler != null) {
            Message message = Message.obtain(handler, id);
            handler.sendMessage(message);
        }
    }

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

    public void buttonClick(View v){
        if (firstClick == 1){
            startTimer();
            firstClick = 0;
        }
        for(int i = 0, q = 0; i < 25; i++) {
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

    public void reset(View v){
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
            if(helper.getCount() < 5){
                Intent intent = new Intent(this, inputRecord.class);
                intent.putExtra("time", count);
                startActivity(intent);
            }
            else{
                RecordTable max = helper.getMaxTime();
                if(count < max.getTime()){
                    int id = max.getId();
                    Intent intent = new Intent(this, inputRecord.class);
                    intent.putExtra("time", count);
                    intent.putExtra("id", id);
                    startActivity(intent);
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

    @Override
    public void onBackPressed() {
        stopTimer();
        handler = null;
        finish();
    }
}
