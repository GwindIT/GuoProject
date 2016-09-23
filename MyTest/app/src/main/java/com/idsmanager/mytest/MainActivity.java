package com.idsmanager.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editText;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.et_my);
        button = (Button) findViewById(R.id.bt_my);
        textView = (TextView) findViewById(R.id.tv_my);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_my:
                NumberUtil.StringToByte("12:34");
                Time();
                break;
            default:
                break;
        }
    }


    private void Time() {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
//        Calendar calendar1 = Calendar.getInstance();

        calendar.set(2015, 0, 29);
        System.out.println(simpleFormatter.format(calendar.getTime()));
    }
}
