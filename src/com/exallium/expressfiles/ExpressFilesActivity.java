package com.exallium.expressfiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.exallium.expressfiles.adapters.FileAdapter;
import com.exallium.expressfiles.listeners.SearchAnimationListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PatternMatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Filter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ExpressFilesActivity extends Activity {
	
	private static String TAG = "ExpressFilesActivity";
	private static int CHILD_LIST = 0;
	private static int CHILD_TEXT = 1;
	
	private ListView listResults;
	private ViewSwitcher viewSwitcher;
	private TextView noitems;
	private EditText searchBox;
	private TextWatcher textWatcher;
	private FileAdapter fileAdapter;
	private String workingPath;
	private String defaultPath;
	private File workingDirectory;
	private List<File> workingListing;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listResults = (ListView) findViewById(R.id.list_results);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.list_switch);
        noitems = (TextView) findViewById(R.id.list_noitems);
        searchBox = (EditText) findViewById(R.id.search_box);
        
        // Get the default path we want, and open it.
        SharedPreferences prefs = getSharedPreferences("expressfile", Context.MODE_PRIVATE);
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
				plainTextFilter();
			}
        	
        };
        searchBox.addTextChangedListener(textWatcher);
        
        
        // List stuff
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
					Uri uri = Uri.fromFile(item);
					
					Log.d(TAG, item.getName());
					
					String type = getFileType(item);
					
					try {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(uri, Constants.mimemap.get(type));
						startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(
								getApplicationContext(), "Could not open " + item.getName(), 
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
    }
    
    private void plainTextFilter() {
    	
    	Log.d(TAG, "ATTEMPTING FILTER");
    	String to_match = Pattern.quote(searchBox.getText().toString());
    	
    	
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
        
    }
    
    private void writeWorkingPath() {
    	SharedPreferences prefs = this.getSharedPreferences("expressfile", Context.MODE_PRIVATE);
    	SharedPreferences.Editor edit = prefs.edit();
    	edit.putString("current_path", workingPath);
    	edit.commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
    	case android.R.id.home:
    		
    		if (workingDirectory.getParentFile() != null) {
        		workingDirectory = workingDirectory.getParentFile();
    			workingPath = workingDirectory.getAbsolutePath();
    			displayList();
    		}
    		
    		return true;
    	case R.id.default_path:
    		
    		workingPath = defaultPath;
    		workingDirectory = new File(defaultPath);
    		displayList();
    		
    		return true;
    		
    	case R.id.search_dir:
    		
    		int anim = searchBox.isShown() ? android.R.anim.fade_out : android.R.anim.fade_in;
    		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), anim);
    		a.setAnimationListener(new SearchAnimationListener(searchBox));
    		searchBox.startAnimation(a);
    		
    		return true;
    	default:
    		return false;
    	}
    	
    }
    
    private String getFileType(File item) {
    	String [] item_split = item.getName().split("[.]");
		return item_split[item_split.length - 1];
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
        
        writeWorkingPath();
        
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
        
        if (workingDirectory.getParentFile() != null) {
        	this.getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
        	this.getActionBar().setDisplayHomeAsUpEnabled(false);
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