package com.example.testlauncher;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HomeFragment extends FragmentActivity {

	private LinkedHashSet<String> selectedApps = null;
	private LinearLayout buttonDisplay = null;
	private AppsLoader appsLoader = null;
	private PkgNamesReaderUpdater allowedAppsUpdater = new PkgNamesReaderUpdater();
	private PasswordInfoReader passwordRetriever = new PasswordInfoReader();
	private String exitLauncherChoice = "Exit";
	private String addAppsChoice = "AddApps";
	private String exitPasscodeTitle = "Enter Passcode to Exit the Launcher";
	private String addAppsPasscodeTitle = "Enter Passcode to Modify Apps Displayed";
	private String addAppsDialogTitle = "Manage Allowed Applications";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);

		buttonDisplay = (LinearLayout)findViewById(R.id.app_buttons);
		appsLoader = new AppsLoader(this);

		selectedApps = allowedAppsUpdater.getPkgNames();
		updateAppDisplay();
	}


	@Override
	protected void onRestart() {
		super.onRestart();  // Always call the superclass method first
		// Activity being restarted from stopped state    
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_exit:
			passwordPrompt(exitLauncherChoice);
			return true;
		case R.id.action_add_apps:
			passwordPrompt(addAppsChoice);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void switchToDefaultLauncher() {
		getPackageManager().clearPackagePreferredActivities(getPackageName());
		final Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	public void displayAppList() {
		
		final ArrayList<String> fullPkgList =  appsLoader.getPkgList(); 
		final ArrayList<String> fullAppList = appsLoader.getAppList();
		CharSequence[] appList = fullAppList.toArray(new CharSequence[fullAppList.size()]);
		boolean[] itemsPreSelected = new boolean[appList.length];
		for (String appSelected : selectedApps) {
			int indexOfSelectedApp = fullPkgList.indexOf(appSelected);
			itemsPreSelected[indexOfSelectedApp] = true;
		}

		AlertDialog.Builder appDisplay = new AlertDialog.Builder(this);

		appDisplay.setTitle(addAppsDialogTitle);
		appDisplay.setMultiChoiceItems(appList, itemsPreSelected,
				new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int indexSelected,
					boolean isChecked) {
				String pkgName = fullPkgList.get(indexSelected);
				if (isChecked) {
					selectedApps.add(pkgName);
				} else {
					selectedApps.remove(pkgName);
				}
			}
			
		});
		
		// Set the action buttons
		appDisplay.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				allowedAppsUpdater.setPkgNames(selectedApps);
				selectedApps = allowedAppsUpdater.getPkgNames();
				
				buttonDisplay.removeAllViews();
				updateAppDisplay();
			}
		});
		appDisplay.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//When user clicks on cancel nothing is done. Just goes back to launcher screen
			}
		});
		appDisplay.show();
	}


	public void passwordPrompt(final String menuOptionChosen) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		if (menuOptionChosen.equals(exitLauncherChoice)) {
			alert.setTitle(exitPasscodeTitle);
		} else if (menuOptionChosen.equals(addAppsChoice)) {
			alert.setTitle(addAppsPasscodeTitle);
		}

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();

				try {
					if (value.equals(passwordRetriever.extractPassword())) {
						if (menuOptionChosen.equals(exitLauncherChoice)) {
							switchToDefaultLauncher();
						} else if (menuOptionChosen.equals(addAppsChoice)) {
							displayAppList();
						} 
					}
				} catch (Exception e) {
					Log.d("Launcher", e.getMessage());
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

	public void updateAppDisplay() {
		for (final String pkgName : selectedApps) {
			Button appButton = new Button(getBaseContext());
			try {
				appButton.setWidth(72);
				appButton.setHeight(72);
				appButton.setBackground(getPackageManager().getApplicationIcon(pkgName));
				appButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i;
						PackageManager manager = getPackageManager();
						try {
							i = manager.getLaunchIntentForPackage(pkgName);
							if (i == null)
								throw new PackageManager.NameNotFoundException();
							i.addCategory(Intent.CATEGORY_LAUNCHER);
							startActivity(i);
						} catch (PackageManager.NameNotFoundException e) {
							Log.d("Launcher", e.getMessage());
						}
					}
				});
			} catch (NameNotFoundException e) {
				Log.d("Launcher", e.getMessage());
				//i write a utility class that logs an exception
			}
			buttonDisplay.addView(appButton);
		}
	}
}