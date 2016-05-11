package com.example.room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;

public class ReadStr {
	//static String path = "/sdcard/foo.txt";
	static String content = ""; //�ļ������ַ���
	static InputStreamReader inputreader;
	static BufferedReader buffreader;
	static InputStream instream;
	static File file;
	
	static public void init(){
		file = Environment.getExternalStorageDirectory();
		//���path�Ǵ��ݹ����Ĳ�����������һ����Ŀ¼���ж�
		try {
			instream = new FileInputStream(file.getCanonicalPath() + "/fly.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (instream != null) {
			 inputreader = new InputStreamReader(instream);
			 buffreader = new BufferedReader(inputreader);
		 }
		 
	}
	
	
	public static String readALine(){
		String line;
		 //���ж�ȡ
		
		 try {
			if(( line = buffreader.readLine()) != null) {
					 return line;
				 }
			
		
			instream.close();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}
	
}
