package com.exallium.expressfiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;

public class ExpressFilesActivity extends Activity {
	
	private ListView listResults;
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