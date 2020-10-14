package com.cornez.shades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class inputRecord extends Activity {

    private EditText name;
    private Button send;
    private RecordDataBase helper;
    private int time;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_record);
        Intent intent = getIntent();
        time = intent.getIntExtra("time",-1);
        id = intent.getIntExtra("id",-1);
        name = findViewById(R.id.editext);
        send = findViewById(R.id.button);
        send.setOnClickListener(sendListener);
        helper = new RecordDataBase(this);
    }

    private View.OnClickListener sendListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!TextUtils.isEmpty(name.getText())) {
                if(helper.getCount() < 5)
                    helper.addRecord(new RecordTable(helper.getCount(), name.getText().toString(), time));
                else
                    helper.editRecord(new RecordTable(id, name.getText().toString(), time));
                finish();
            }
        }
    };
}
