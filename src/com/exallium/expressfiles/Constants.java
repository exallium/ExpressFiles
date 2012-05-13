package com.exallium.expressfiles;

import java.util.HashMap;

public class Constants {
	public final static HashMap<String, String> mimemap = new HashMap<String, String>();
	static {
		
		// Images
		mimemap.put("jpg", "image/*");
		mimemap.put("jpeg", "image/*");
		mimemap.put("png", "image/*");
		mimemap.put("bmp", "image/*");
		mimemap.put("gif", "image/*");
		
		// Video
		
		// Music
		mimemap.put("mp3", "audio/*");
		mimemap.put("m4a", "audio/*");
	}
}
