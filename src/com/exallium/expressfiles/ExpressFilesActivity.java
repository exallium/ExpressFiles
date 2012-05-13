package com.exallium.expressfiles;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.exallium.expressfiles.adapters.FileAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ExpressFilesActivity extends Activity {
	
	private static String TAG = "ExpressFilesActivity";
	
	private ListView listResults;
	private FileAdapter fileAdapter;
	private String workingPath;
	private File workingDirectory;
	private List<File> workingListing;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listResults = (ListView) findViewById(R.id.list_results);
        
        // Get the default path we want, and open it.
        SharedPreferences prefs = getSharedPreferences("expressfile", Context.MODE_PRIVATE);
        workingPath = prefs.getString("default_path", null);
        if (workingPath == null) {
        	workingDirectory = Environment.getExternalStorageDirectory();
        	workingPath = workingDirectory.getAbsolutePath();
        } else {
        	workingDirectory = new File(workingPath);
        }
        
        displayList();
        
        listResults.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				File item = workingListing.get(arg2);
				
				if(item.isDirectory()) {
					workingDirectory = item;
					workingPath = workingDirectory.getAbsolutePath();
					displayList();
				} else {
					// XXX: This needs to be able to map MIME to file type
					Uri uri = Uri.fromFile(item);
					
					Log.d(TAG, item.getName());
					
					String [] item_split = item.getName().split("[.]");
					String type = item_split[item_split.length - 1];
					
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, Constants.mimemap.get(type));
					startActivity(intent);
				}
			}
		});
    }
    
    private void displayList() {
    	// XXX: We are assuming the workingPath file exists.  This could lead to crashing.
        genWorkingListing();
        
        // We try to sort the list
        // XXX: We need to gracefully handle the error
        try {
			alphabeticSort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        fileAdapter = new FileAdapter(
        		this.getApplicationContext(), R.layout.fileitem, workingListing);
        listResults.setAdapter(fileAdapter);
        
        this.getActionBar().setTitle(workingPath);
    }
    
    /**
     * Get a dynamic list of directories and files that we can sort through.
     * This should eventually cascade down into folders, but only when searching.
     * @return true if success, false if failure
     */
    private boolean genWorkingListing() {
    	
    	if (!workingDirectory.isDirectory()) { return false; }
    	
    	workingListing = new ArrayList<File>();
    	for (File f : workingDirectory.listFiles()) {
    		// XXX: this is where our regex filter should be placed.
    		workingListing.add(f);
    	}
    	
    	return true;
    }
    
    /**
     * Sorts a list of files and directories alphabetically.
     * @throws IOException if one of the canonical paths dont exist
     */
    private void alphabeticSort() throws IOException {
    	boolean flag = true;
    	File temp;
    	
    	while (flag) {
    		flag = false;
    		for (int i = 0; i < workingListing.size() - 1; i++) {
    			String file1 = workingListing.get(i).getCanonicalPath();
    			String file2 = workingListing.get(i + 1).getCanonicalPath();
    			
    			if (file1.compareToIgnoreCase(file2) > 0) {
    				temp = workingListing.get(i);
    				workingListing.set(i, workingListing.get(i + 1));
    				workingListing.set(i + 1, temp);
    				flag = true;
    			}
    		}
    	}
    }
}