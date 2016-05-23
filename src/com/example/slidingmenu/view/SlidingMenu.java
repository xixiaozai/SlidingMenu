package com.example.slidingmenu.view;

import com.example.slidingmenu.R;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	// �lĻ����
	private int mScreenWidth;

	private int mMenuWidth;

	/*
	 * ƵĻ�ұ߾���
	 */
	// dp����ʼ��ȵ�λ��
	private int mMenuRightPadding = 50;

	private boolean once;

	private boolean isOpen;

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	/**
	 * ���Զ�������ʱ ����ô˹��췽��
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// ��ȡ���Ƕ��������
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		a.recycle();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;

		// /*
		// * ��50dpת��Ϊ����
		// */
		// mMenuRightPadding = (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
		// .getDisplayMetrics());
	}

	public SlidingMenu(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ������View�Ŀ�� �����Լ��Ŀ��
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {

			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * ͨ������ƫ�ƣ�����Menu
	 */

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();

		switch (action) {
		case MotionEvent.ACTION_UP:
			// ���Ӱ�ؿ��scrollX
			int scrollX = getScrollX();
			if (scrollX >= mMenuWidth / 2) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	private void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	private void closeMenu() {
		if (!isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	/*
	 * �л��˵�
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
			Toast.makeText(getContext(), "�ر�", Toast.LENGTH_SHORT).show();
		} else {
			openMenu();
			Toast.makeText(getContext(), "��", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * ��������ʱ (non-Javadoc)
	 * 
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;
		float leftScale = 1.0f - scale * 0.3f;
		float rightScale = 0.7f + 0.3f * scale;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);
		/**
		 * 
		 * ��QQ���� 1.��������1.0~.07 ����Ч�� 2.�˵�ƫ������Ҫ�޸� 3.͸���� ����0.7~1.0 1.0f-scale*0.3f
		 * ͸����0.6~1.0 0.6+0.4*��1-scale��
		 */
		// �������Զ���������TranslationX
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale*0.7f);
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
		// ����content��������
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
	}
}
