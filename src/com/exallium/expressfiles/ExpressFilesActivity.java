package com.exallium.expressfiles;

import com.exallium.expressfiles.fragments.ListFragment;
import com.exallium.expressfiles.utils.Filter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

public class ExpressFilesActivity extends Activity {
	
	private static String TAG = "ExpressFilesActivity";
	private ListFragment fileList;
	private SearchView searchView;
	
	private Filter filter;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fileList = new ListFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.main_viewgroup, fileList, "list_fragment");
		ft.commit();
		
		filter = new Filter(this);
		
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("Search this Folder");
        searchView.setOnQueryTextListener(filter);
        
        return true;
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	

		FragmentTransaction trans = getFragmentManager().beginTransaction();
    	
    	switch (item.getItemId()) {
    	case android.R.id.home:
    		fileList.goBack();
    		return true;
    		
    	case R.id.default_path:
            fileList.goDefault();
    		return true;
    		
    	case R.id.menu_search:
    		
    		
    		
    		return true;
    		
    	case R.id.settings:
    		
    		return true;
    	default:
    		return false;
    	}
    	
    }

    
}