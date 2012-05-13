package com.exallium.expressfiles.fragments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exallium.expressfiles.Constants;
import com.exallium.expressfiles.R;
import com.exallium.expressfiles.adapters.FileAdapter;
import com.exallium.expressfiles.utils.Utilities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;

public class ListFragment extends Fragment {
	
	private static String TAG = "ListFragment";
	private static int CHILD_LIST = 0;
	private static int CHILD_TEXT = 1;
	
	private ListView listResults;
	private FileAdapter fileAdapter;
	private ViewSwitcher viewSwitcher;
	private TextView noitems;
	private File workingDirectory;
	private List<File> workingListing;
	private List<File> originalList;
	private String workingPath;
	private String defaultPath;
	private boolean def = false;
	
	public FileAdapter getFileAdapter() { return fileAdapter; }
	public List<File> getWorkingList() { return workingListing; }
	public List<File> getOriginalList() { return originalList; }
	
	public void setDef(boolean def) {
		this.def = def;
	}
	
	public void goBack() {
		if (workingDirectory.getParentFile() != null) {
    		workingDirectory = workingDirectory.getParentFile();
			workingPath = workingDirectory.getAbsolutePath();
			displayList();
		}
	}
	
	public void goDefault() {
		workingPath = defaultPath;
		workingDirectory = new File(defaultPath);
		displayList();
	}
	
	public void setWorkingInformation() {
		
		 // Get the default path we want, and open it.
		Log.d(TAG, "Gen working dir");
		
        SharedPreferences prefs = getActivity().getSharedPreferences("expressfile", Context.MODE_PRIVATE);
        workingPath = prefs.getString("current_path", null);
        defaultPath = prefs.getString("default_path", null);

		
		if (workingPath == null) {
        	workingDirectory = Environment.getExternalStorageDirectory();
        	workingPath = workingDirectory.getAbsolutePath();
        } else {
        	workingDirectory = new File(workingPath);
        }
        
        if (defaultPath == null) {
        	defaultPath = workingPath;
        	SharedPreferences.Editor edit = prefs.edit();
        	edit.putString("default_path", defaultPath);
        	edit.commit();
        }
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (def) {
			goDefault();
			def = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        setWorkingInformation();
        
		listResults = (ListView) getView().findViewById(R.id.list_results);
        viewSwitcher = (ViewSwitcher) getView();
        noitems = (TextView) getView().findViewById(R.id.list_noitems);
        
        listResults.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
				File item = workingListing.get(arg2);
				
				if(item.isDirectory()) {
					workingDirectory = item;
					workingPath = workingDirectory.getAbsolutePath();
					displayList();
				} else {
					Uri uri = Uri.fromFile(item);
					
					Log.d(TAG, item.getName());
					
					String type = Utilities.getFileType(item);
					
					try {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(uri, Constants.mimemap.get(type));
						startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(
								getActivity().getApplicationContext(), "Could not open " + item.getName(), 
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
        
        displayList();

	}
	
	private void displayList() {
    	// XXX: We are assuming the workingPath file exists.  This could lead to crashing.
        boolean filesExist = genWorkingListing();
        
        if (!filesExist || workingListing.size() == 0) {
        	int msg_id = !filesExist ? R.string.list_cannotaccess : R.string.list_noitems;
        	noitems.setText(msg_id);
        	viewSwitcher.setDisplayedChild(CHILD_TEXT);
        } else {
        	viewSwitcher.setDisplayedChild(CHILD_LIST);
        }
        
        Utilities.writeWorkingPath(getActivity().getApplicationContext(), workingPath);
        
        // We try to sort the list
        // XXX: We need to gracefully handle the error
        try {
			Utilities.alphabeticSort(workingListing);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        fileAdapter = new FileAdapter(
        		getActivity().getApplicationContext(), R.layout.fileitem, workingListing);
        listResults.setAdapter(fileAdapter);
        
        getActivity().getActionBar().setTitle(workingPath);
        
        if (workingDirectory.getParentFile() != null) {
        	getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
        	getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        }
        
        originalList = new ArrayList<File>();
        originalList.addAll(workingListing);
    }
	
    /**
     * Get a dynamic list of directories and files that we can sort through.
     * This should eventually cascade down into folders, but only when searching.
     * @return true if success, false if failure
     */
    private boolean genWorkingListing() {
    	
    	if (!workingDirectory.isDirectory()) { return false; }
    	
    	workingListing = new ArrayList<File>();
    	
    	File [] fileList = workingDirectory.listFiles();
    	if (fileList == null) {
    		return false;
    	}
    	
    	Log.d(TAG, "fileList " + fileList.length);
    	for (File f : fileList) {
    		// XXX: this is where our regex filter should be placed.
    		workingListing.add(f);
    	}
    	
    	return true;
    }
	
}
