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
	 * �Զ�������
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// ��ȡ��Ļ���
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;

		// ��dpת����px������Menu�Ҿ���50dp
		mMenuRightPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
						.getDisplayMetrics());

	}

	/**
	 * δ�Զ������Ե�ʱ��
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
	 * ��ȡ���ؼ�
	 */
	private void initView() {
		mWapper = (LinearLayout) this.getChildAt(0);
		mMenu = (ViewGroup) mWapper.getChildAt(0);
		mContent = (ViewGroup) mWapper.getChildAt(1);
	}

	private boolean once = true;

	/**
	 * ����������View���Լ��Ŀ�͸� ���ܱ���ε���
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
	 * ����λ�� ����ƫ������menu���� ���ܱ���ε���
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
			if (scrollX <= mMenuWidth / 2) {// ���ҳ��� open
				open();
			} else {// �����ƽ�ȥ hide
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
