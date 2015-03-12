package com.jianbin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

@SuppressLint("ClickableViewAccessibility")
public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;

	private int mScreenWidth;
	private int mMenuRightPadding;

	private int mMenuWidth;

	private boolean isOpen = false;

	/**
	 * 自定义属性
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// 获取屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;

		// 把dp转化成px，设置Menu右距离50dp
		mMenuRightPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
						.getDisplayMetrics());

	}

	/**
	 * 未自定义属性的时候
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context) {
		this(context, null);
	}

	/**
	 * 获取主控件
	 */
	private void initView() {
		mWapper = (LinearLayout) this.getChildAt(0);
		mMenu = (ViewGroup) mWapper.getChildAt(0);
		mContent = (ViewGroup) mWapper.getChildAt(1);
	}

	private boolean once = true;

	/**
	 * 测量设置子View和自己的宽和高 可能被多次调用
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (once) {
			initView();
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = false;

		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 决定位置 设置偏移量将menu隐藏 可能被多次调用
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		float evX = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_UP:
			if (isOpen && evX > mMenuWidth) {
				hide();
				return true;
			}
			int scrollX = getScrollX();
			if (scrollX <= mMenuWidth / 2) {// 向右出来 open
				open();
			} else {// 向左移进去 hide
				hide();
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public void open() {
		this.smoothScrollTo(0, 0);
		isOpen = true;

	}

	public void hide() {
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	public void toggle() {
		if (isOpen) {
			hide();
		} else {
			open();
		}
	}
}
