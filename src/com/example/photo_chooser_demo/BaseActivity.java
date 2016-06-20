package com.example.photo_chooser_demo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class BaseActivity extends FragmentActivity implements OnClickListener {
	
	protected int widthPixels, heightPixels;
	public LayoutInflater inflater = null;
	public ProgressDialog progressDialog;
	public DisplayImageOptions.Builder builder;
	public ImageLoader imageLoader;
	protected DisplayImageOptions options0,options1;

	@Override
	protected void onCreate(Bundle arg0) {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		widthPixels = metric.widthPixels;
		heightPixels = metric.heightPixels;
		initImageLoader();
		initView();
		super.onCreate(arg0);
	}
	
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void initView() {
	}
	
	private void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options0 = getDisplayImageOptions(0);
		options1 = getDisplayImageOptions(1);
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
			builder.displayer(new RoundedBitmapDisplayer(90));
			builder.showImageOnLoading(R.drawable.header_icon_default);
			builder.showImageOnFail(R.drawable.header_icon_default);
			builder.showImageForEmptyUri(R.drawable.header_icon_default);
		}
		return builder.build();
	}
	
	public void displayImage(String url,ImageView v,boolean isHeader) {
		if(isHeader) {
			imageLoader.displayImage(url, v, options1);
		} else {
			imageLoader.displayImage(url, v, options0);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
