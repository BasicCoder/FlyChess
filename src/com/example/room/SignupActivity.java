package com.example.room;

import com.example.room.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends Activity {
	private String name;
    private String nick;
    private String psd;
    private String rpsd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button bn = (Button) findViewById(R.id.bt1);
        bn.setOnClickListener(new bnClickListener());
        TextView tv = (TextView)findViewById(R.id.tv1);
        tv.setOnClickListener(new tvClickListener());
    }
    class bnClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            EditText ed_name = (EditText) findViewById(R.id.ed_name1);
            EditText ed_nick = (EditText) findViewById(R.id.ed_nick);
            EditText ed_psd = (EditText) findViewById(R.id.ed_psd1);
            EditText ed_rpsd = (EditText) findViewById(R.id.ed_rpsd);
            name=new String(ed_name.getText().toString());
            nick=new String(ed_nick.getText().toString());
            psd=new String(ed_psd.getText().toString());
            rpsd=new String(ed_rpsd.getText().toString());

            if(psd.equals(rpsd))
            {
            	Intent intent = new Intent(SignupActivity.this, RoomActivity.class);
                startActivity(intent);
                finish();
            }


        }

    }
    
    class tvClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            Intent intent = new Intent();
            intent.setClass(SignupActivity.this, LoginActivity.class);
            startActivity(intent);


        }

    }
}
