package com.example.testlauncher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;

import android.os.Environment;
import android.util.Log;

public class PkgNamesReaderUpdater {
	String fileName = "packageNames.txt/";
	String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	public LinkedHashSet<String> getPkgNames () {
		LinkedHashSet<String> listOfSelectedApps = new LinkedHashSet<String>();
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
			br = new BufferedReader(new FileReader(dir + File.separator + fileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				listOfSelectedApps.add(sCurrentLine);
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
		
		return listOfSelectedApps;
	}
	
	public void setPkgNames(LinkedHashSet<String> selectedAppsToDisplay) {
		Writer writer = null;
		String allPkgsToAddToFile = "";
		String lineBreak = "\n";

		try {
		    writer = new BufferedWriter(new FileWriter(dir + File.separator + fileName));
		    for (String pkgName : selectedAppsToDisplay) {
		    	allPkgsToAddToFile += (pkgName + lineBreak);
		    }
		    writer.write(allPkgsToAddToFile);
		} catch (IOException ex) {
			Log.d("Launcher", ex.getMessage());
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}
}
