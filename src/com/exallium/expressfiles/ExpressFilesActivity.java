package com.exallium.expressfiles;

import com.exallium.expressfiles.fragments.ListFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ExpressFilesActivity extends Activity {
	
	private static String TAG = "ExpressFilesActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ListFragment listFrag = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        listFrag.setWorkingInformation();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	ListFragment listFrag = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
    	
    	switch (item.getItemId()) {
    	case android.R.id.home:
            listFrag.goBack();
    		return true;
    	case R.id.default_path:
            listFrag.goDefault();
    		return true;
    		
    	case R.id.search_dir:
    		
    		// Show search fragment
    		
    		return true;
    		
    	case R.id.settings:
    		
    		// Hotswap fragment
    		
    		return true;
    	default:
    		return false;
    	}
    	
    }
    
    
    

    
    
}