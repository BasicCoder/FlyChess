package com.example.room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity{
	private String name;
    private String psd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button bn = (Button) findViewById(R.id.bt);
        bn.setOnClickListener(new bnClickListener());

        TextView tv = (TextView)findViewById(R.id.tv1);
        tv.setOnClickListener(new tvClickListener());

    }
    class bnClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            EditText ed_name = (EditText) findViewById(R.id.ed_name);
            EditText ed_psd = (EditText) findViewById(R.id.ed_psd);
            name=new String(ed_name.getText().toString());
            psd=new String(ed_psd.getText().toString());
            Intent intent = new Intent(LoginActivity.this, RoomActivity.class);
            
            Bundle bundle = new Bundle();
            bundle.putString("userid", name);
            intent.putExtras(bundle);
            
            startActivity(intent);
            finish();

        }

    }
    class tvClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, SignupActivity.class);
            startActivity(intent);


        }

    }
}