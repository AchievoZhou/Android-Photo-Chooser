package com.example.photo_chooser_demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AlbumGridViewAdapter extends BAdapter {

	private List<PhotoItem> dataList = new ArrayList<PhotoItem>();
	private ImageView pho_chs_pview;
	private int currPosition = -1;

	public AlbumGridViewAdapter(Context c) {
		super(c);
		setNeedFadeIn(false);
	}

	public List<PhotoItem> getDataList() {
		return dataList;
	}

	public void setDataList(List<PhotoItem> dataList) {
		this.dataList.clear();
		this.dataList.addAll(dataList);
		if(this.dataList.size() > 1){
			setCurrPosition(1);
		}
	}
	
	public void setShowView(ImageView pho_chs_pview){
		this.pho_chs_pview = pho_chs_pview;
	}
	
	public void setCurrPosition(int currPosition){
		this.currPosition = currPosition;
	}

	public int getCurrPosition() {
		return currPosition;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder {
		public ImageView album_item_thumbnail;
		public ImageView album_item_selected;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.photoselect_album_detail_item, parent, false);
			viewHolder.album_item_thumbnail = (ImageView) convertView
					.findViewById(R.id.album_item_thumbnail);
			viewHolder.album_item_selected = (ImageView) convertView
					.findViewById(R.id.album_item_selected);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final PhotoItem photoItem = dataList.get(position);
		viewHolder.album_item_thumbnail.getLayoutParams().height = widthPixels/3;
		viewHolder.album_item_selected.getLayoutParams().height = widthPixels/3;
		
		long photoID = photoItem.getPhotoID();
		
		if(photoID == Integer.MIN_VALUE){
			viewHolder.album_item_thumbnail.setImageBitmap(null);
			viewHolder.album_item_thumbnail.setBackgroundResource(R.drawable.phchs_icon_ps);
		}else{
			viewHolder.album_item_thumbnail.setBackgroundResource(R.drawable.list_image_default);
			viewHolder.album_item_thumbnail.setImageBitmap(photoItem.getBitmap());
		    if(position == currPosition){
		    	viewHolder.album_item_selected.setVisibility(View.VISIBLE);
		    	if(new File(photoItem.getPath()).exists()){
		    		String imageUrl = Scheme.FILE.wrap(photoItem.getPath());
			    	displayImage(imageUrl, pho_chs_pview, "image");
		    	}else{
		    		pho_chs_pview.setImageBitmap(null);
		    	}
		    }else{
		    	viewHolder.album_item_selected.setVisibility(View.GONE);
		    }
		}

		return convertView;
	}
	
}
