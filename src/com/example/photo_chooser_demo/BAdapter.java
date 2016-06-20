package com.example.photo_chooser_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class BAdapter extends BaseAdapter {
	protected Context context;
	protected int widthPixels, heightPixels;
	protected LayoutInflater inflater;
	protected DisplayImageOptions displayImageOptions0,displayImageOptions1,displayImageOptions2;
	private boolean isNeedFadeIn = true;

	public BAdapter(Context context) {
		this.context = context;
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		widthPixels = displayMetrics.widthPixels;
		heightPixels = displayMetrics.heightPixels;
		inflater = LayoutInflater.from(context);
		displayImageOptions0 = getDisplayImageOptions(0);
		displayImageOptions1 = getDisplayImageOptions(1);
		displayImageOptions2 = getDisplayImageOptions(2);
	}
	
	private DisplayImageOptions getDisplayImageOptions(int tag){
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.considerExifParams(true);
		builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		if(tag == 0){
			builder.displayer(new SimpleBitmapDisplayer());
			builder.showImageOnLoading(R.drawable.list_image_default);
			builder.showImageOnFail(R.drawable.list_image_default);
			builder.showImageForEmptyUri(R.drawable.list_image_default);
		}else if(tag == 1){
			builder.displayer(new FadeInBitmapDisplayer(800));
			builder.showImageOnLoading(R.drawable.list_image_default);
			builder.showImageOnFail(R.drawable.list_image_default);
			builder.showImageForEmptyUri(R.drawable.list_image_default);
		}else if(tag == 2){
			
			builder.displayer(new RoundedBitmapDisplayer(90));
			builder.showImageOnLoading(R.drawable.header_icon_default);
			builder.showImageOnFail(R.drawable.header_icon_default);
			builder.showImageForEmptyUri(R.drawable.header_icon_default);
		}
		return builder.build();
	}
	
	protected void displayImage(String url, ImageView imageView, String tag){
		if(url == null || imageView == null){
			return;
		}
		if("image".equals(tag)){
			if(isNeedFadeIn){
				ImageLoader.getInstance().displayImage(url, imageView, displayImageOptions1);
			}else{
				ImageLoader.getInstance().displayImage(url, imageView, displayImageOptions0);
			}
		}else if("icon".equals(tag)){
			ImageLoader.getInstance().displayImage(url, imageView, displayImageOptions2);
		}
	}
	
	
	public boolean isNeedFadeIn() {
		return isNeedFadeIn;
	}

	public void setNeedFadeIn(boolean isNeedFadeIn) {
		this.isNeedFadeIn = isNeedFadeIn;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
	
}
