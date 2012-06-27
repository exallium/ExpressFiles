package com.exallium.expressfiles.adapters;

import java.io.File;
import java.util.List;

import com.exallium.expressfiles.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileAdapter extends ArrayAdapter<File> {
	
	private List<File> objects;
	private Context context;
	private Drawable folderImage, defaultImage;

	public FileAdapter(Context context, int textViewResourceId,
			List<File> objects) {
		super(context, textViewResourceId, objects);
		
		this.context = context;
		this.objects = objects;
		
		Resources res = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, res.getDisplayMetrics());
		int pixel = (int) px;
		
		folderImage = res.getDrawable(R.drawable.directory);
		folderImage.setBounds(0, 0, pixel, pixel);
		
		defaultImage = res.getDrawable(R.drawable.ic_launcher);
		defaultImage.setBounds(0, 0, pixel, pixel);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.fileitem, null);
		}
		
		File file = objects.get(position);
		
		TextView title = (TextView) v.findViewById(R.id.listitem_title);
		
		try {
			title.setText(file.getName());
			
			if (file.isDirectory()) {
				title.setCompoundDrawables(folderImage, null, null, null);
			} else {
				title.setCompoundDrawables(defaultImage, null, null, null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			title.setText("<ERROR, PLEASE REFER TO LOGCAT>");
		}
		
		return v;
	}

}
