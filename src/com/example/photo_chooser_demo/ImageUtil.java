package com.example.photo_chooser_demo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {

	public static Bitmap reduce(String path, int outw, int outh) {
		if (path == null || outw <= 0 || outh <= 0) {
			return null;
		}
		try{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 1;
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opt);
			
			opt.inSampleSize = sampleSize(opt, outw, outh);
			opt.inJustDecodeBounds = false;
			Bitmap tBitmap = BitmapFactory.decodeFile(path, opt);
			return tBitmap;
		}catch(Exception e){
		}
		return null;
	}
	
	public static Bitmap reduce(Resources res, int rid, int outw, int outh) {
		if (rid <= 0 || outw <= 0 || outh <= 0) {
			return null;
		}
		try{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 1;
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(res, rid, opt);
			
			opt.inSampleSize = sampleSize(opt, outw, outh);
			opt.inJustDecodeBounds = false;
			Bitmap tBitmap = BitmapFactory.decodeResource(res, rid, opt);
			return tBitmap;
		}catch(Exception e){
		}
		return null;
	}

	private static int sampleSize(BitmapFactory.Options opts, int outw, int outh) {
		int hRatio = (int) Math.ceil(opts.outHeight / (float) outh);
		int wRatio = (int) Math.ceil(opts.outWidth / (float) outw);
		int sampleSize = 1;
		if (hRatio > 1 || wRatio > 1) {
			if (hRatio > wRatio) {
				sampleSize = hRatio;
			} else {
				sampleSize = wRatio;
			}
		}
		return sampleSize;
	}

}
