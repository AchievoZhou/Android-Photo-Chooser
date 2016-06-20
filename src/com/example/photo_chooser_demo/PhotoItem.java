package com.example.photo_chooser_demo;

import java.io.Serializable;
import android.graphics.Bitmap;

public class PhotoItem implements Serializable, Comparable {
	private static final long serialVersionUID = 8682674788506891598L;
	private long photoID;
	private String path;
	private Bitmap bitmap;
	private boolean select = false;
	private long order;

	public PhotoItem(long id, String path, Bitmap bitmap) {
		this.photoID = id;
		this.path = path;
		this.bitmap = bitmap;
	}

	public PhotoItem(long id, boolean flag) {
		photoID = id;
		select = flag;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getPhotoID() {
		return photoID;
	}

	public void setPhotoID(long photoID) {
		this.photoID = photoID;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public boolean equals(Object o) {
		return ((PhotoItem)o).photoID == this.photoID;
	}

	public int hashCode() {
		return ((Long)photoID).hashCode();
	}

	@Override
	public int compareTo(Object arg0) {
		if(this.photoID == (((PhotoItem)arg0).photoID)){
			return 0;
		}else{
			if(this.order > ((PhotoItem)arg0).order){
				return 1;
			}else if(this.order < ((PhotoItem)arg0).order){
				return -1;
			}else{
				return 1;
			}
		}
	}

}
