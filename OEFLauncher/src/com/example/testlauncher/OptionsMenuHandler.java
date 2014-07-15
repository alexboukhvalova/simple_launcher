package com.example.testlauncher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

public class OptionsMenuHandler {
	
	HomeFragment homeFragment = new HomeFragment();
	
	public OptionsMenuHandler() {
		//do i need a constructor?
	}
	

	public void promptExitPassWord(String menuChoiceSelected) {
		AlertDialog.Builder alert = new AlertDialog.Builder(homeFragment);

		alert.setTitle("Enter Passcode to Exit the Launcher");

		// Set an EditText view to get user input 
		final EditText input = new EditText(homeFragment);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if (value.equals("alex")) {
					homeFragment.switchToDefaultLauncher();
				} else {
					//don't do anything if the password is incorrect
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}
	
	
	public void promptForAddAppsPassword() {
		AlertDialog.Builder alert = new AlertDialog.Builder(homeFragment);
		alert.setTitle("Enter Passcode to Modify Apps Displayed");
		
		final EditText input = new EditText(homeFragment);
		alert.setView(input);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if (value.equals("alex")) {
					homeFragment.displayAppList();
				} else {
					//don't do anything
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
		
	}
	
	
}
