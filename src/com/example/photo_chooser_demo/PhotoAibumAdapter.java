package com.example.photo_chooser_demo;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoAibumAdapter extends BaseAdapter {
	
	private List<PhotoAibum> aibumList = new ArrayList<PhotoAibum>();
	private Context context;
	private ViewHolder holder;
	private Bitmap defaultBitmap;

	public PhotoAibumAdapter(Context context) {
		this.context = context;
		defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.list_image_default);
	}

	public List<PhotoAibum> getAibumList() {
		return aibumList;
	}

	public void setAibumList(List<PhotoAibum> aibumList) {
		this.aibumList = aibumList;
	}

	@Override
	public int getCount() {
		return aibumList.size();
	}

	@Override
	public Object getItem(int position) {
		return aibumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (RelativeLayout)LayoutInflater.from(context).inflate(
					R.layout.photoselect_album_item, null);
			holder = new ViewHolder();
			holder.photoalbum_item_image = (ImageView) convertView.findViewById(R.id.photoalbum_item_image);
			holder.photoalbum_item_name = (TextView) convertView.findViewById(R.id.photoalbum_item_name);
			holder.photoalbum_item_num = (TextView) convertView.findViewById(R.id.photoalbum_item_num);
			holder.photoalbum_item_checked = (ImageView) convertView.findViewById(R.id.photoalbum_item_checked);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		PhotoAibum photoAibum = aibumList.get(position);
		holder.photoalbum_item_name.setText(photoAibum.getName());
		if(photoAibum.isChecked()){
			holder.photoalbum_item_checked.setVisibility(View.VISIBLE);
		}else{
			holder.photoalbum_item_checked.setVisibility(View.GONE);
		}
		
		holder.photoalbum_item_image.setImageBitmap(defaultBitmap);
		
		if(position == 0){
			if(aibumList.size() > 1){
				List<PhotoItem> photoItems = aibumList.get(position+1).getBitList();
				if(photoItems.size() > 0){
					holder.photoalbum_item_image.setImageBitmap(photoItems.get(0).getBitmap());
				}
			}
			holder.photoalbum_item_num.setText("");
		}else{
			List<PhotoItem> photoItems = photoAibum.getBitList();
			if(photoItems.size() > 0){
				holder.photoalbum_item_image.setImageBitmap(photoItems.get(0).getBitmap());
			}
			holder.photoalbum_item_num.setText("(" + photoAibum.getCount() + ")");
		}
		
		return convertView;
	}

	static class ViewHolder {
		ImageView photoalbum_item_image;
		TextView photoalbum_item_name;
		TextView photoalbum_item_num;
		ImageView photoalbum_item_checked;
	}

}
