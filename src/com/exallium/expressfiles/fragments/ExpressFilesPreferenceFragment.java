package com.exallium.expressfiles.fragments;

import com.exallium.expressfiles.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ExpressFilesPreferenceFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.preferences);
	}
}
