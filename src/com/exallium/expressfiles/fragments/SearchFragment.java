package com.exallium.expressfiles.fragments;

import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import com.exallium.expressfiles.R;
import com.exallium.expressfiles.adapters.FileAdapter;
import com.exallium.expressfiles.utils.Utilities;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SearchFragment extends Fragment {
	
	private static String TAG = "SearchFragment";
	private EditText searchBox;
	private TextWatcher textWatcher;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.search_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		searchBox = (EditText) this.getView();
		
		// Search Listener
        textWatcher = new TextWatcher() {

			public void afterTextChanged(Editable s) {
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				Log.d(TAG, "TEXT CHANGED");
				
				try {
					filter();
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
        	
        };
        searchBox.addTextChangedListener(textWatcher);
		
	}
	
	public void filter() {
    	
		// Grab the details from the activity
		ListFragment list = (ListFragment) getActivity().getFragmentManager().findFragmentByTag("list_fragment");
		
		if (list == null) return;
		
		FileAdapter fileAdapter = list.getFileAdapter();
		List<File>	originalList = list.getOriginalList();
		List<File> workingListing = list.getWorkingList();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		boolean regex = prefs.getBoolean("preference_regex", false);
		
    	Log.d(TAG, "ATTEMPTING FILTER");
    	
    	String to_match = searchBox.getText().toString();
    	
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
}
