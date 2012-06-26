package com.exallium.expressfiles;

import com.darvds.ribbonmenu.iRibbonMenuCallback;
import com.darvds.ribbonmenu.RibbonMenuView;
import com.exallium.expressfiles.fragments.ListFragment;
import com.exallium.expressfiles.fragments.SearchFragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ExpressFilesActivity extends Activity implements iRibbonMenuCallback {
	
	private static String TAG = "ExpressFilesActivity";
	private SearchFragment search;
	private ListFragment fileList;
	private RibbonMenuView ribbonMenu;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fileList = new ListFragment();
		search = new SearchFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.main_viewgroup, search);
		ft.hide(search);
		ft.add(R.id.main_viewgroup, fileList, "list_fragment");
		ft.commit();
		
		ribbonMenu = (RibbonMenuView) findViewById(R.id.rmv);
		ribbonMenu.setMenuClickCallback(this);
		ribbonMenu.setMenuItems(R.menu.ribbon);
		
		initFilter();
		
    }
    
    private void initFilter() {
    	SharedPreferences prefs = getSharedPreferences("expressfile", Context.MODE_PRIVATE);
    	boolean regex = prefs.getBoolean("preference_regex", false);
    	updateFilter(regex);
    }
    
    private void updateFilter(boolean regex) {
    	int text = regex ? R.string.preference_regex : R.string.preference_regex_off;
    	ListView rbmListView = (ListView) ribbonMenu.findViewById(R.id.rbm_listview);
    	// XXX: Need to do a little hacking, fork and manipulate.
    }
    
    private void toggleFilter() {
    	SharedPreferences prefs = getSharedPreferences("expressfile", Context.MODE_PRIVATE);
    	boolean regex = prefs.getBoolean("preference_regex", false);
    	updateFilter(!regex);
    	SharedPreferences.Editor edit = prefs.edit();
    	edit.putBoolean("preference_regex", !regex);
    	edit.commit();
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
    		fileList.goBack();
    		return true;
    		
    	case R.id.default_path:
            fileList.goDefault();
    		return true;
    		
    	case R.id.search_dir:
    		
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
    		ribbonMenu.toggleMenu();
    		return true;
    	default:
    		return false;
    	}
    	
    }

	public void RibbonMenuItemClick(int itemId) {
		switch(itemId) {
		case R.id.ribbon_filter:
			toggleFilter();
		}
		
	}

    
}