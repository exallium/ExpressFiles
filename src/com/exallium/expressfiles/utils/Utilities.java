package com.exallium.expressfiles.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

public class Utilities {
	/**
     * Sorts a list of files and directories alphabetically.
     * @throws IOException if one of the canonical paths dont exist
     */
    public static void alphabeticSort(List<File> workingListing) throws IOException {
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
    
    public static String getFileType(File item) {
    	String [] item_split = item.getName().split("[.]");
		return item_split[item_split.length - 1];
    }
    
    public static void writeWorkingPath(Context context, String workingPath) {
    	SharedPreferences prefs = context.getSharedPreferences("expressfile", Context.MODE_PRIVATE);
    	SharedPreferences.Editor edit = prefs.edit();
    	edit.putString("current_path", workingPath);
    	edit.commit();
    }
}
