package com.exallium.expressfiles;

import com.exallium.expressfiles.fragments.ExpressFilesPreferenceFragment;
import com.exallium.expressfiles.fragments.ListFragment;
import com.exallium.expressfiles.fragments.SearchFragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ExpressFilesActivity extends Activity {
	
	private static String TAG = "ExpressFilesActivity";
	private SearchFragment search;
	private ExpressFilesPreferenceFragment prefs;
	private ListFragment fileList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        prefs = new ExpressFilesPreferenceFragment();
        fileList = new ListFragment();
		search = new SearchFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		//ft.hide(search);
		ft.add(R.id.main_viewgroup, search);
		ft.add(R.id.main_viewgroup, fileList, "list_fragment");
		ft.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	

		FragmentTransaction trans = getFragmentManager().beginTransaction();
    	
    	switch (item.getItemId()) {
    	case android.R.id.home:
    		
    		if (!fileList.isAdded()) {
    			trans.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        		trans.attach(search);
        		trans.replace(R.id.main_viewgroup, fileList, "list_fragment");
        		trans.commit();
    		} else {
    			fileList.goBack();
    		}
    		
    		return true;
    		
    	case R.id.default_path:
    		
    		if (!fileList.isAdded()) {
        		Log.d(TAG, "HERE I AM1123411");
        		fileList.setDef(true);
        		trans.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        		trans.attach(search);
        		trans.replace(R.id.main_viewgroup, fileList, "list_fragment");
        		trans.commit();
    		} else {
                fileList.goDefault();
    		}
    		
    		return true;
    		
    	case R.id.search_dir:
    		
    		if (!prefs.isHidden() && prefs.isAdded()) return false;
    		
    		// Show search fragment
    		trans.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
    		
    		if (!search.isHidden()) {
    			trans.hide(search);
    		} else {
    			trans.show(search);
    		}
    		
    		trans.commit();
    		
    		return true;
    		
    	case R.id.settings:
    		
    		// Hotswap fragment
    		trans.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
    		trans.detach(search);
    		trans.replace(R.id.main_viewgroup, prefs);
    		trans.commit();
    		
    		return true;
    	default:
    		return false;
    	}
    	
    }
    
    
    

    
    
}