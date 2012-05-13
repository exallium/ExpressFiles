package com.exallium.expressfiles.listeners;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class SearchAnimationListener implements AnimationListener {
	
	private View v;
	private int displayed;
	
	public SearchAnimationListener(View v) {
		this.v = v;
		displayed = v.getVisibility();
	}

	public void onAnimationEnd(Animation animation) {
		Log.d("ASDF", "Ending...");
		if (displayed == View.VISIBLE) {
			v.setVisibility(View.GONE);
		} else {
			v.setVisibility(View.VISIBLE);
		}
	}

	public void onAnimationRepeat(Animation animation) {
		
	}

	public void onAnimationStart(Animation animation) {
		Log.d("ASDF", "Starting...");
		if (displayed == View.GONE) {
			v.setVisibility(View.INVISIBLE);
		}
	}

}
