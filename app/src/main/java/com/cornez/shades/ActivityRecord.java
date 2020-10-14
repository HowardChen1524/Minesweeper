package com.cornez.shades;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

public class ActivityRecord extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);

        RecordDataBase helper = new RecordDataBase(this);

        if(helper.getCount() != 0) {
            TextView recordText[] = new TextView[5];
            for (int i = 0; i < helper.getCount(); i++)
                recordText[i] = findViewById(getResources().getIdentifier("record" + i, "id", getPackageName()));
            ArrayList<RecordTable> records = helper.getOrderRecords();
            for (int i = 0; i < helper.getCount(); i++)
                recordText[i].setText(records.get(i).getName() + " " + records.get(i).getTime());
        }

    }
}
