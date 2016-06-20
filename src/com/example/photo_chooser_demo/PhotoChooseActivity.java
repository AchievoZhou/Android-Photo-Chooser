package com.example.photo_chooser_demo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

public class PhotoChooseActivity extends BaseActivity {

	public static final String TEMPSHOOTFOLDER = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ "PhotoChooser/photo/";
	
	public static final String TEMPORARYFOLDER = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ "PhotoChooser/image/";
	
	private PhotoChooseView pho_chs_rl;
	private ImageView backBtn, settingBtn;
	private TextView pageName, nextTv;
	private Animation inFromBottom, outFromBottom;
	private RelativeLayout msee_title_rl; 
	private GridView pho_chs_gv;
	private LinearLayout pho_chs_ll, pho_chs_all_nav_ll, pho_chs_pview_ll;
	private ImageView pho_chs_shade_iv, pho_chs_pview;
	private Drawable drawable,drawable1;
	private ListView pho_chs_all_nav_list;
	private PhotoAibumAdapter photoAibumAdapter;
	private AlbumGridViewAdapter albumGridViewAdapter;
	
	private int screenWidth,screenHeight;
	private long afterTime = System.currentTimeMillis()/1000 - 30*24*60*60*6;
	//private long afterTime = -1;
	
	private static final String[] STORE_IMAGES = { MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.DATA, MediaStore.Images.Media.LONGITUDE,
		MediaStore.Images.Media._ID,
		MediaStore.Images.Media.BUCKET_ID,
		MediaStore.Images.Media.BUCKET_DISPLAY_NAME
	};
	
	private String cameraFilePath;
	private String selPath;
	private boolean isLoaded = false;
	public int from;
	
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_photo_choose);
		
		initDisplayImageOptions();
		from = getIntent().getIntExtra("from", 0);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		
		super.onCreate(arg0);
		
		albumGridViewAdapter = new AlbumGridViewAdapter(this);
		albumGridViewAdapter.setShowView(pho_chs_pview);
		pho_chs_gv.setAdapter(albumGridViewAdapter);
		pho_chs_rl.setAlbumGridViewAdapter(albumGridViewAdapter);
		pho_chs_rl.setHandler(handler);
		
		photoAibumAdapter = new PhotoAibumAdapter(this); 
		pho_chs_all_nav_list.setAdapter(photoAibumAdapter);
		pho_chs_all_nav_list.setOnItemClickListener(aibumClickListener);
		
		LoadTask loadTask = new LoadTask(null);
		loadTask.execute();
	}
	
	protected void onRestart() {
		super.onRestart();
	}

	public void initView() {
		super.initView();
		backBtn = (ImageView) this.findViewById(R.id.back_btn);
		settingBtn = (ImageView) this.findViewById(R.id.setting_btn);
		settingBtn.setVisibility(View.GONE);
		nextTv = (TextView) this.findViewById(R.id.next_tv);
		nextTv.setVisibility(View.VISIBLE);
		nextTv.setText("下一步");
		nextTv.setTextColor(this.getResources().getColor(R.color.text_color_mlivered));
		
		pageName = (TextView) this.findViewById(R.id.page_name);
		//pageName.setTextColor(this.getResources().getColor(R.color.white));
		pageName.setGravity(Gravity.CENTER_VERTICAL);
		pageName.setText("所有图片");
		drawable = getResources().getDrawable(R.drawable.phchs_bottom_icon);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		drawable1 = getResources().getDrawable(R.drawable.phchs_bottom_icon1);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
		pageName.setCompoundDrawablePadding(dip2px(3));
		pageName.setCompoundDrawables(null, null, drawable1, null);
		backBtn.setOnClickListener(this);
		nextTv.setOnClickListener(this);
		pageName.setOnClickListener(this);
		
		inFromBottom = AnimationUtils.loadAnimation(
				this.getApplicationContext(), R.anim.from_top_in);
		outFromBottom = AnimationUtils.loadAnimation(
				this.getApplicationContext(), R.anim.from_top_out);
		
		pho_chs_rl = (PhotoChooseView) this
				.findViewById(R.id.pho_chs_rl);
		pho_chs_ll = (LinearLayout) this
				.findViewById(R.id.pho_chs_ll);
		pho_chs_pview_ll = (LinearLayout) this
				.findViewById(R.id.pho_chs_pview_ll);
		pho_chs_gv = (GridView) this
				.findViewById(R.id.pho_chs_gv);
		pho_chs_all_nav_list = (ListView) this
				.findViewById(R.id.pho_chs_all_nav_list);
		pho_chs_all_nav_ll = (LinearLayout) this
				.findViewById(R.id.pho_chs_all_nav_ll);
		pho_chs_shade_iv = (ImageView) this
				.findViewById(R.id.pho_chs_shade_iv);
		pho_chs_pview = (ImageView) this
				.findViewById(R.id.pho_chs_pview);
		
		msee_title_rl = (RelativeLayout) this.findViewById(R.id.msee_title_rl);
		//msee_title_rl.setBackgroundResource(R.drawable.phchs_title_bg);
		msee_title_rl.measure(0, 0);
		((LinearLayout.LayoutParams)pho_chs_gv.getLayoutParams()).topMargin = screenHeight*3/5;
		pho_chs_pview_ll.getLayoutParams().height = screenHeight*3/5;
		((RelativeLayout.LayoutParams)pho_chs_pview_ll.getLayoutParams()).topMargin = msee_title_rl.getMeasuredHeight();
		((RelativeLayout.LayoutParams)pho_chs_shade_iv.getLayoutParams()).topMargin = msee_title_rl.getMeasuredHeight();
		((RelativeLayout.LayoutParams)pho_chs_all_nav_ll.getLayoutParams()).topMargin = msee_title_rl.getMeasuredHeight();
		
		inFromBottom.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				pho_chs_shade_iv.setVisibility(View.VISIBLE);
				pageName.setCompoundDrawables(null, null, drawable, null);
			}
		});
		outFromBottom.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				pho_chs_shade_iv.setVisibility(View.GONE);
				pageName.setCompoundDrawables(null, null, drawable1, null);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {}
		});

		pho_chs_shade_iv.setOnClickListener(this);
		pageName.setOnClickListener(this);
	}
	
	private class LoadTask extends AsyncTask<Void, List<PhotoAibum> , List<PhotoAibum> >{
		private String dirId;
		
		LoadTask(String dirId){
			this.dirId = dirId;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			if(dirId == null && !isLoaded){
			}
		}
		
		protected List<PhotoAibum> doInBackground(Void... params) {
			List<PhotoAibum> aibumList = getPhotoAlbum(dirId);
			return aibumList;
		}
		
		protected void onPostExecute(List<PhotoAibum> result) {
			PhotoAibum currPhotoAibum = null;
			
			PhotoAibum photoAibumAll = new PhotoAibum();
			photoAibumAll.setName("所有图片");
			photoAibumAll.setChecked(true);
			List<PhotoItem> photoItems = new ArrayList<PhotoItem>();
			PhotoItem photoItem = new PhotoItem(Integer.MIN_VALUE, null, null);
			photoItems.add(0, photoItem);
			photoAibumAll.setBitList(photoItems);
			
			if(result.size() > 0){
				if(dirId == null){
					for(PhotoAibum pa : result){
						photoItems.addAll(pa.getBitList());
					}
					currPhotoAibum = photoAibumAll;
				}else{
					PhotoAibum photoAibum = result.get(0);
					photoAibum.getBitList().add(0, photoItem);
					currPhotoAibum = photoAibum;
				}
			}
			
			if(!isLoaded){
				result.add(0, photoAibumAll);
				photoAibumAdapter.setAibumList(result);
				photoAibumAdapter.notifyDataSetChanged();
				isLoaded = true;
			}
			
			if(currPhotoAibum != null){
				albumGridViewAdapter.setDataList(currPhotoAibum.getBitList());
			}else{
				albumGridViewAdapter.setDataList(photoItems);
			}
			albumGridViewAdapter.notifyDataSetChanged();
			
			return;
		};
	}
	
	public void setActionBarRight() {
		int position = albumGridViewAdapter.getCurrPosition();
		if(position == -1){
			Toast.makeText(this, "请选择一张图片。", Toast.LENGTH_SHORT).show();
			return;
		}
		PhotoItem photoItem = (PhotoItem)albumGridViewAdapter.getItem(position);
		if(position == 0){
			selPath = cameraFilePath;
		}else{
			selPath = photoItem.getPath();
		}
		if(selPath == null || !new File(selPath).exists()){
			Toast.makeText(this, "无效图片。", Toast.LENGTH_SHORT).show();
			return;
		}
		if(from == 1){
		}else if(from == 0 || from == 2){
		}
		Toast.makeText(this, "你的下一步。", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onClick(View v) {
		if(!isLoaded && v.getId() != R.id.back_btn){
			return;
		}
		switch (v.getId()) {
		case R.id.page_name:
			if (pho_chs_all_nav_ll.getVisibility() == View.GONE) {
				pho_chs_all_nav_ll.setVisibility(View.VISIBLE);
				pho_chs_all_nav_ll.startAnimation(inFromBottom);
			} else {
				pho_chs_all_nav_ll.startAnimation(outFromBottom);
				pho_chs_all_nav_ll.setVisibility(View.GONE);
			}
			break;
		case R.id.pho_chs_shade_iv:
			if (pho_chs_all_nav_ll.getVisibility() == View.VISIBLE) {
				pho_chs_all_nav_ll.startAnimation(outFromBottom);
				pho_chs_all_nav_ll.setVisibility(View.GONE);
			}
			break;
		case R.id.back_btn:
			if (pho_chs_all_nav_ll.getVisibility() == View.VISIBLE) {
				pho_chs_all_nav_ll.startAnimation(outFromBottom);
				pho_chs_all_nav_ll.setVisibility(View.GONE);
			}else{
				finish();
			}
			return;
		case R.id.next_tv:
			setActionBarRight();
			return;	
		default:
			break;
		}
		super.onClick(v);
	}
	
	private List<PhotoAibum> getPhotoAlbum(String dirId) {
		List<PhotoAibum> aibumList = new ArrayList<PhotoAibum>();
		Cursor cursor;
		if(dirId == null || "".equals(dirId.trim())){
			cursor = MediaStore.Images.Media.query(getContentResolver(),
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
					MediaStore.Images.Media.DATE_ADDED + ">" + afterTime,
					MediaStore.Images.Media._ID + " DESC");
		}else{
			cursor = MediaStore.Images.Media.query(getContentResolver(),
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, 
					MediaStore.Images.Media.DATE_ADDED + ">" + afterTime + " AND " + 
					MediaStore.Images.Media.BUCKET_ID + "=" + dirId, 
					MediaStore.Images.Media._ID + " DESC");
		}
		
		Map<String, PhotoAibum> countMap = new HashMap<String, PhotoAibum>();
		PhotoAibum pa = null;
		while (cursor.moveToNext()) {
			String path = cursor.getString(1);
			String id = cursor.getString(3);
			String dir_id = cursor.getString(4);
			String dir = cursor.getString(5);
			
			Log.e("info", "id===" + id + "==dir_id==" + dir_id + "==dir==" + dir + "==path=" + path);
			
			if(!new File(path).exists()){
				continue;
			}
			
			Bitmap thumbnail = ImageCache.getImage("thumbnail:"+path);
			if(thumbnail == null){
				thumbnail = MediaStore.Images.Thumbnails.getThumbnail(this
						.getContentResolver(), Long.parseLong(id), Thumbnails.MICRO_KIND, null);
				
				if(thumbnail == null){
					thumbnail = ImageUtil.reduce(path, 240, 240);
				}
				
				if(thumbnail == null){
					thumbnail = BitmapFactory.decodeResource(getResources(), R.drawable.list_image_default);
				}
				
				ImageCache.putImage("thumbnail:"+path, thumbnail);
			}
			
			if (!countMap.containsKey(dir_id)) {
				pa = new PhotoAibum();
				pa.setDirId(dir_id);
				pa.setName(dir);
				pa.setId(Long.parseLong(id));
				pa.setCount("1");
				countMap.put(dir_id, pa);
			} else {
				pa = countMap.get(dir_id);
				pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
			}
			
			PhotoItem photoItem = new PhotoItem(Long.parseLong(id), path, thumbnail);
			photoItem.setOrder(System.currentTimeMillis());
			pa.getBitList().add(photoItem);
			
		}
		cursor.close();
		Iterable<String> it = countMap.keySet();
		for (String key : it) {
			aibumList.add(countMap.get(key));
		}
		
		return aibumList;
	}

	private OnItemClickListener aibumClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			PhotoAibum photoAibum = (PhotoAibum)photoAibumAdapter.getItem(position);
			if(photoAibum.isChecked() || photoAibum.getBitList().size() < 1){
				return;
			}else{
				for(PhotoAibum pa : photoAibumAdapter.getAibumList()){
					if(pa.isChecked()){
						pa.setChecked(false);
					}
				}
				photoAibum.setChecked(true);
				photoAibumAdapter.notifyDataSetChanged();
				
				pageName.setText(photoAibum.getName());
				
				LoadTask loadTask = new LoadTask(photoAibum.getDirId());
				loadTask.execute();
				
				if (pho_chs_all_nav_ll.getVisibility() == View.VISIBLE) {
					pho_chs_all_nav_ll.startAnimation(outFromBottom);
					pho_chs_all_nav_ll.setVisibility(View.GONE);
				}
			}
		}
	};
	
	public String genShootPath(){
		this.cameraFilePath =  TEMPSHOOTFOLDER + System.currentTimeMillis() + ".jpg";
		return cameraFilePath;
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        if (intent.resolveActivity(getPackageManager()) != null) {
		            ContentValues contentValues = new ContentValues(2);
		            contentValues.put(MediaStore.Images.Media.DATA, genShootPath());
		            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		            Uri mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
		            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
		            startActivityForResult(intent, 0);
		        }
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 30001) {
			return;
		}
		if(resultCode != RESULT_OK){
			return;
		}
		if(requestCode == 0){
			albumGridViewAdapter.setCurrPosition(0);
			albumGridViewAdapter.notifyDataSetChanged();
			String imageUrl = Scheme.FILE.wrap(cameraFilePath);
			displayImage(imageUrl, pho_chs_pview, false);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pho_chs_all_nav_ll.getVisibility() == View.VISIBLE) {
				pho_chs_all_nav_ll.startAnimation(outFromBottom);
				pho_chs_all_nav_ll.setVisibility(View.GONE);
			}else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	
	public int dip2px(float dipValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public int px2dip(float pxValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	private void initDisplayImageOptions() {
		File shootFile = new File(TEMPSHOOTFOLDER);
		
		if (!shootFile.mkdirs()) {
			Log.e("Account", shootFile.getName() + " mkdir fail");
		}
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				this);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.memoryCache(new WeakMemoryCache());
		config.threadPoolSize(20);
		config.diskCacheFileNameGenerator(new FileNameGenerator() {
			@Override
			public String generate(String arg0) {
				return null;
			}
		});
		config.denyCacheImageMultipleSizesInMemory();
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.diskCache(new UnlimitedDiskCache(
				new File(TEMPORARYFOLDER)));// 自定义缓存路径
		ImageLoader.getInstance().init(config.build());
	}

}
