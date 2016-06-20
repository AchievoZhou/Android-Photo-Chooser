package com.example.photo_chooser_demo;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class ImageCache {

	private static final Map<String, SoftReference<Bitmap>> cache = new LinkedHashMap<String, SoftReference<Bitmap>>();

	public static Bitmap getImage(String url) {
		synchronized (cache) {
			if(cache.containsKey(url)) {
				SoftReference<Bitmap> soft = cache.get(url);
				if(null != soft) {
					Bitmap bmp = soft.get();
					if(null != bmp && !bmp.isRecycled()) {
						return bmp;
					} else {
						cache.remove(url);
					}
				} else {
					cache.remove(url);
				}
			}
			return null;
		}
	}

	public static void putImage(String url, Bitmap bmp) {
		synchronized (cache) {
			if(cache.containsKey(url)) {
				return;
			}
			if(null != bmp && !bmp.isRecycled()) {
				cache.put(url, new SoftReference<Bitmap>(bmp));
			}
		}
	}
	
	public static void remove(String url) {
		synchronized (cache) {
			cache.remove(url);
		}
	}
	
	public void release(String preString){
		synchronized (cache) {
			boolean isAll = true;
			if(preString != null && !"".equals(preString.trim())){
				isAll = false;
			}
			for(String url : cache.keySet()){
				if(!isAll){
					if(!url.startsWith(preString)){
						continue;
					}
				}
				SoftReference<Bitmap> soft = cache.get(url);
				if(null != soft) {
					Bitmap bmp = soft.get();
					if(null != bmp && !bmp.isRecycled()) {
						bmp.recycle();
						cache.remove(url);
					}
				}
			}
		}
	}
	
}
