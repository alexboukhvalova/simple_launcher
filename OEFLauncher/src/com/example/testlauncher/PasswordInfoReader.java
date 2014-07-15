package com.example.testlauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class PasswordInfoReader {
	public String extractPassword() throws IOException {
		
		String password = "";
		String fileName = "password.txt/";
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
			br = new BufferedReader(new FileReader(dir + File.separator + fileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				password += sCurrentLine;
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				Log.d("Launcher", ex.getMessage());
			}
		}
		
		return password;
	}
}
