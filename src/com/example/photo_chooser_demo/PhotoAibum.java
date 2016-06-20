package com.example.photo_chooser_demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoAibum implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String dirId;
	private String name; // 相册名字
	private String count; // 数量
	private long id; // 相册第一张图片
	private boolean isChecked = false; //是否选中
	private List<PhotoItem> bitList = new ArrayList<PhotoItem>();
	
	public PhotoAibum(){
	}
	
	public PhotoAibum(String dirId, String name, String count, long id) {
		super();
		this.dirId = dirId;
		this.name = name;
		this.count = count;
		this.id = id;
	}

	public String getDirId() {
		return dirId;
	}

	public void setDirId(String dirId) {
		this.dirId = dirId;
	}

	public List<PhotoItem> getBitList() {
		return bitList;
	}

	public void setBitList(List<PhotoItem> bitList) {
		this.bitList = bitList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "PhotoAibum [name=" + name + ", count=" + count + ", id="
				+ id + ", bitList=" + bitList + "]";
	}
}
