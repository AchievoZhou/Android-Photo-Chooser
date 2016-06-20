package com.example.photo_chooser_demo;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PhotoChooseView extends RelativeLayout {

	private Interpolator accdec = new AccelerateDecelerateInterpolator();
	private GridView pho_chs_gv;
	private LinearLayout pho_chs_ll, pho_chs_pview_ll, pho_chs_all_nav_ll;
	private GestureDetector mGestureDetector;
	private AlbumGridViewAdapter albumGridViewAdapter;

	private Handler handler;
	private int widthPixels, heightPixels;
	private int verticalMinDistance = 50;
	private int minVelocity = 800;
	private int topHold = 0;
	private boolean isTop = false;
	private boolean isMoving = false;

	public PhotoChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PhotoChooseView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		widthPixels = displayMetrics.widthPixels;
		heightPixels = displayMetrics.heightPixels;
		mGestureDetector = new GestureDetector(getContext(),
				new MyGestureListener());
	}
	
	public AlbumGridViewAdapter getAlbumGridViewAdapter() {
		return albumGridViewAdapter;
	}

	public void setAlbumGridViewAdapter(AlbumGridViewAdapter albumGridViewAdapter) {
		this.albumGridViewAdapter = albumGridViewAdapter;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initContentView();
	}

	private void initContentView() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			if (view.getId() == R.id.pho_chs_pview_ll) {
				pho_chs_pview_ll = (LinearLayout) view;
			}
			if (view.getId() == R.id.pho_chs_all_nav_ll) {
				pho_chs_all_nav_ll = (LinearLayout) view;
			}
			if (view.getId() == R.id.pho_chs_ll) {
				pho_chs_ll = (LinearLayout) view;
				pho_chs_gv = (GridView) pho_chs_ll.getChildAt(1);
			}
		}
		
		pho_chs_pview_ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isTop && !isMoving){
					isMoving = true;
					downAnimator(-1);
				}
			}
		});
		
		pho_chs_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){
					handler.sendEmptyMessage(0);
				}else{
					albumGridViewAdapter.setCurrPosition(position);
					albumGridViewAdapter.notifyDataSetChanged();
				}
				if(isTop && !isMoving){
					isMoving = true;
					downAnimator(position);
				}
			}
		});
		
	}
	
	private void upAnimator(int position){
		ValueAnimator valueAnimator2 = marginValueAnimator2(position);
		valueAnimator2.start();
	}
	
	private void downAnimator(int position){
		ValueAnimator valueAnimator2_ = marginValueAnimator2_(position);
		valueAnimator2_.start();
	}
	
    public ValueAnimator marginValueAnimator2(int position){
        ValueAnimator mAnimator = ValueAnimator.ofInt(heightPixels*3/5, topHold);
        final int top = ((RelativeLayout.LayoutParams)pho_chs_pview_ll.getLayoutParams()).topMargin;
        mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override  
            public void onAnimationUpdate(ValueAnimator animation) {  
                int animatorValue = (Integer)animation.getAnimatedValue();
                
                MarginLayoutParams marginLayoutParams1 = (MarginLayoutParams) pho_chs_pview_ll.getLayoutParams();  
                marginLayoutParams1.topMargin = top-(heightPixels*3/5-animatorValue);
                pho_chs_pview_ll.setLayoutParams(marginLayoutParams1);  
                
                MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) pho_chs_gv.getLayoutParams();
                marginLayoutParams2.topMargin = animatorValue;
                pho_chs_gv.setLayoutParams(marginLayoutParams2);  
            }  
        });
        mAnimator.addListener(new AnimatorListener (){
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				isTop = true;
				isMoving = false;
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}}
        );
        
        mAnimator.setTarget(pho_chs_gv);  
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(accdec);
        return mAnimator;
    }  
    
    public ValueAnimator marginValueAnimator2_(final int position){
        ValueAnimator mAnimator = ValueAnimator.ofInt(topHold, heightPixels*3/5);
        final int top = ((RelativeLayout.LayoutParams)pho_chs_pview_ll.getLayoutParams()).topMargin;
        mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override  
            public void onAnimationUpdate(ValueAnimator animation) {  
                int animatorValue = (Integer)animation.getAnimatedValue();
                
                MarginLayoutParams marginLayoutParams1 = (MarginLayoutParams) pho_chs_pview_ll.getLayoutParams();  
                marginLayoutParams1.topMargin = top + (animatorValue-topHold);
                pho_chs_pview_ll.setLayoutParams(marginLayoutParams1);  
                
                MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) pho_chs_gv.getLayoutParams();
                marginLayoutParams2.topMargin = animatorValue;
                pho_chs_gv.setLayoutParams(marginLayoutParams2);  
            }  
        });  
        mAnimator.addListener(new AnimatorListener (){
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				if(position > 0){
					pho_chs_gv.smoothScrollToPosition(position);
				}
				isTop = false;
				isMoving = false;
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}}
        );
        mAnimator.setTarget(pho_chs_gv);  
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(accdec);
        return mAnimator;
    }  

	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mGestureDetector.onTouchEvent(ev)) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	private class MyGestureListener extends SimpleOnGestureListener {
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(pho_chs_all_nav_ll.getVisibility() == View.VISIBLE || isMoving){
				return false;
			}
			
			if(pho_chs_gv.getAdapter().getCount() <= 6){
				return false;
			}
			
			float mNewX = e2.getX();
			float mNewY = e2.getY();
			float mOldX = e1.getX();
			float mOldY = e1.getY();
			float distanceY = mNewY - mOldY;

			if (Math.abs(mNewY - mOldY) > Math.abs(mNewX - mOldX)) {
				if (mNewY > mOldY) {
					if(isTop && pho_chs_gv.getFirstVisiblePosition() == 0){
						isMoving = true;
						downAnimator(-1);
					}
				} else {
					if (Math.abs(distanceY) > verticalMinDistance
							&& Math.abs(velocityY) > minVelocity && !isTop) {
						isMoving = true;
						upAnimator(-1);
					}
				}
			}
			return false;
		}
	}

}
