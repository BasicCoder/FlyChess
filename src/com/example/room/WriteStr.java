package com.example.room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;

public class WriteStr {
	//static String path = "/sdcard/foo.txt";
	static String content = ""; //ÎÄ¼þÄÚÈÝ×Ö·û´®
	static File file;
	static OutputStream outstream;
	//static OutputStreamWriter out;
	
	static public void init(){
		file = Environment.getExternalStorageDirectory();
		
	}
	
	public static void writeALine(String str){
		 try {
			 Log.v("write", "1");
			 str += "\n";
			 outstream = new FileOutputStream(file.getCanonicalPath() + "/fly.txt",true);
			 outstream.write(str.getBytes());
			 outstream.close();
			 Log.v("write", "2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeALine(int game, int color,int seq, int len) {
		writeALine(""+game+";"+color+";"+seq+";"+len);
	}
	
	
}
