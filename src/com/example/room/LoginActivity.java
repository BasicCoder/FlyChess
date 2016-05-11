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

        TextView tv1 = (TextView)findViewById(R.id.tv1);
        tv1.setOnClickListener(new tvClickListener());
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        tv2.setOnClickListener(new tvClickListener());

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
        	switch(v.getId()){
        	
        	case R.id.tv1:
        		Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
        		break;
        	case R.id.tv2:
        		Intent ReplayGameView = new Intent();
        		ReplayGameView.setClass(LoginActivity.this, GameView.class);
        		Bundle bundle = new Bundle();
				bundle.putString("roomid", "1");
				bundle.putString("roomstyle", "1");
				bundle.putString("userserial", "1");
				bundle.putBoolean("isreplay", Boolean.valueOf(true));
				ReplayGameView.putExtras(bundle);
                startActivity(ReplayGameView);
        		break;
        	}
            


        }

    }
}