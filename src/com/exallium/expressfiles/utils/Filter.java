package com.exallium.expressfiles.utils;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.SearchView.OnQueryTextListener;

import com.exallium.expressfiles.adapters.FileAdapter;
import com.exallium.expressfiles.fragments.ListFragment;

public class Filter implements OnQueryTextListener {
	
	private static String TAG = Filter.class.getSimpleName();
	private Activity activity;
	
	public Filter(Activity activity) {
		this.activity = activity;
	}
	
	private void execute(String to_match) {
    	
		// Grab the details from the activity
		ListFragment list = (ListFragment) activity.getFragmentManager().findFragmentByTag("list_fragment");
		
		if (list == null) return;
		
		FileAdapter fileAdapter = list.getFileAdapter();
		List<File>	originalList = list.getOriginalList();
		List<File> workingListing = list.getWorkingList();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		boolean regex = prefs.getBoolean("preference_regex", false);
		
    	Log.d(TAG, "ATTEMPTING FILTER");
    	
    	if (!regex) {
    		to_match = Pattern.quote(to_match);
    		to_match = ".*" + to_match + ".*";
    	}
    	
    	if (to_match.length() == 0) {
    		to_match = ".*";
    		Log.d(TAG, to_match);
    	} else {
    		Log.d(TAG, to_match);
    	}
    	
    	fileAdapter.clear();
    	Log.d(TAG, "" + originalList.size());
    	
    	for (int i = 0; i < originalList.size(); i++) {
    		if (Pattern.matches(to_match, originalList.get(i).getName())) {
    			Log.d(TAG, originalList.get(i).getName() + " matches");
    			workingListing.add(originalList.get(i));
    		}
    	}
    	
    	try {
			Utilities.alphabeticSort(workingListing);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	fileAdapter.notifyDataSetChanged();
    }

	public boolean onQueryTextChange(String newText) {
		execute(newText);
		return true;
	}

	public boolean onQueryTextSubmit(String query) {
		// We do nasing....
		return false;
	}

}
