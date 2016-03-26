package com.hw.common.ui;

import java.util.List;

import com.hw.common.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 在原来基础上增加一些属性：矩形指示器、颜色、可见性等
 * http://blog.csdn.net/lmj623565791/article/details/42160391
 * 
 * @930324291
 */
public class MiuiViewPagerIndicator extends LinearLayout {
	/**
	 * 绘制三角形的画笔
	 */
	private Paint mPaint;
	/**
	 * path构成一个三角形
	 */
	private Path mPath;
	/**
	 * 三角形的宽度
	 */
	private int mTriangleWidth;
	/**
	 * 三角形的高度
	 */
	private int mTriangleHeight;

	/**
	 * 三角形的宽度为单个Tab的1/6
	 */
	private static final float RADIO_TRIANGEL = 1.0f / 6;
	/**
	 * 三角形的最大宽度
	 */
	private final int DIMENSION_TRIANGEL_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGEL);

	/**
	 * 矩形
	 */
	private int mTop;
	private int mWidth; // 指示符的width
	private int mHeight = 5; // 指示符的高度，固定了
	private int mColor; // 指示符的颜色

	// 设置默认指示器形状
	private Shape mIndicatorShape = Shape.Rectangle;

	private IndicatorVisibility mVisibility = IndicatorVisibility.Visible;
	/**
	 * 初始时，三角形指示器的偏移量
	 */
	private int mInitTranslationX;
	/**
	 * 手指滑动时的偏移量
	 */
	private float mTranslationX;

	/**
	 * 默认的Tab数量
	 */
	private static final int COUNT_DEFAULT_TAB = 4;
	/**
	 * tab数量
	 */
	private int mTabVisibleCount = COUNT_DEFAULT_TAB;

	/**
	 * tab上的内容
	 */
	private List<String> mTabTitles;
	/**
	 * 与之绑定的ViewPager
	 */
	public ViewPager mViewPager;

	/**
	 * 标题正常时的颜色
	 */
	private int COLOR_TEXT_NORMAL;
	/**
	 * 标题选中时的颜色
	 */
	private int COLOR_TEXT_HIGHLIGHTCOLOR;
	
	/**
	 * 是否生成带背景的textview
	 */
	private Boolean isSetTvBackground = true;

	public Boolean getIsSetTvBackground() {
		return isSetTvBackground;
	}

	public void setIsSetTvBackground(Boolean isSetTvBackground) {
		this.isSetTvBackground = isSetTvBackground;
	}

	public enum Shape {
		Rectangle, Triangle
	}

	public enum IndicatorVisibility {
		Visible, Invisible;
	};

	public MiuiViewPagerIndicator(Context context) {
		this(context, null);
	}

	public MiuiViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		 setWillNotDraw(false);
		// 获得自定义属性，tab的数量
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MiuiViewPagerIndicator);
		mTabVisibleCount = a.getInt(R.styleable.MiuiViewPagerIndicator_item_count, COUNT_DEFAULT_TAB);
		if (mTabVisibleCount < 0)
			mTabVisibleCount = COUNT_DEFAULT_TAB;

		// 获取指示器颜色
		mColor = a.getColor(R.styleable.MiuiViewPagerIndicator_indicator_color, Color.parseColor("#63a1f7"));

		// 获取文本颜色
		COLOR_TEXT_NORMAL = a.getColor(R.styleable.MiuiViewPagerIndicator_unselect_title_color, Color.parseColor("#0087e0"));
		COLOR_TEXT_HIGHLIGHTCOLOR = a.getColor(R.styleable.MiuiViewPagerIndicator_select_title_color, Color.parseColor("#ffffff"));

		// 获取指示器形状
		int shape = a.getInt(R.styleable.MiuiViewPagerIndicator_miui_shape, Shape.Rectangle.ordinal());
		for (Shape s : Shape.values()) {
			if (s.ordinal() == shape) {
				mIndicatorShape = s;
				break;
			}
		}

		// 获取指示器是否显示
		int visibility = a.getInt(R.styleable.MiuiViewPagerIndicator_miui_visibility, IndicatorVisibility.Visible.ordinal());
		for (IndicatorVisibility v : IndicatorVisibility.values()) {
			if (v.ordinal() == visibility) {
				mVisibility = v;
				break;
			}
		}

		a.recycle();

		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mColor);
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	/**
	 * 初始化
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGEL);// 1/6 of width
		
		mTriangleWidth = Math.min(DIMENSION_TRIANGEL_WIDTH, mTriangleWidth);

		// 初始时的偏移量
		mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth / 2;
	}

	/**
	 * 设置可见的tab的数量
	 * 
	 * @param count
	 */
	public void setVisibleTabCount(int count) {
		this.mTabVisibleCount = count;
	}

	/**
	 * 设置tab的标题内容 可选，可以自己在布局文件中写死
	 * 
	 * @param datas
	 */
	public void setTabItemTitles(List<String> datas) {
		// 如果传入的list有值，则移除布局文件中设置的view
		if (datas != null && datas.size() > 0) {
			this.removeAllViews();
			this.mTabTitles = datas;
//			for (String title : mTabTitles) {
			for (int i=0;i<mTabTitles.size();i++) {
				// 添加view
				if(isSetTvBackground){
					addView(generateTextViewWithBackground(mTabTitles.get(i),i));
				}else{
					addView(generateTextView(mTabTitles.get(i)));
				}
			}
			// 设置item的click事件
			setItemClickEvent();
		}

	}

	/**
	 * 对外的ViewPager的回调接口
	 * 
	 * @author zhy
	 * 
	 */
	public interface PageChangeListener {
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		public void onPageSelected(int position);

		public void onPageScrollStateChanged(int state);
	}

	// 对外的ViewPager的回调接口
	private PageChangeListener onPageChangeListener;

	// 对外的ViewPager的回调接口的设置
	public void setOnPageChangeListener(PageChangeListener pageChangeListener) {
		this.onPageChangeListener = pageChangeListener;
	}

	// 设置关联的ViewPager
	public void setViewPager(ViewPager mViewPager, int pos) {
		this.mViewPager = mViewPager;

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// 设置字体颜色高亮
				resetTextViewColor();
				highLightTextView(position);

				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 滚动
				scroll(position, positionOffset);

				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageScrollStateChanged(state);
				}

			}
		});
		// 设置当前页
		mViewPager.setCurrentItem(pos);
		// 高亮
		highLightTextView(pos);
	}
	
	
	// 设置关联的ViewPager
		public void setViewPager1(ViewPager mViewPager) {
			this.mViewPager = mViewPager;
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					// 设置字体颜色高亮
					resetTextViewColor();
					highLightTextView(position);

					// 回调
					if (onPageChangeListener != null) {
						onPageChangeListener.onPageSelected(position);
					}
				}

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					// 滚动
					scroll(position, positionOffset);

					// 回调
					if (onPageChangeListener != null) {
						onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
					}

				}

				@Override
				public void onPageScrollStateChanged(int state) {
					// 回调
					if (onPageChangeListener != null) {
						onPageChangeListener.onPageScrollStateChanged(state);
					}

				}
			});
		}

	/**
	 * 高亮文本
	 * 
	 * @param position
	 */
	protected void highLightTextView(int position) {
		View view = getChildAt(position);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
			((TextView) view).setSelected(true);
		}

	}

	/**
	 * 重置文本颜色
	 */
	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
				((TextView) view).setSelected(false);
			}
		}
	}

	/**
	 * 设置点击事件
	 */
	public void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

	/**
	 * 根据标题生成我们的TextView
	 * 
	 * @param text
	 * @return
	 */
	private TextView generateTextView(String text) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tv.setLayoutParams(lp);
		return tv;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private TextView generateTextViewWithBackground(String text,int position) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		tv.setGravity(Gravity.CENTER);
		
		if(position == 0){
			setTextBackground(tv,getResources().getDrawable(R.drawable.tab_tv_bg_left));
//			
		}else if(position == mTabTitles.size() - 1){
			setTextBackground(tv,getResources().getDrawable(R.drawable.tab_tv_bg_right));
//			tv.setBackground(getResources().getDrawable(R.drawable.tab_tv_bg_right));
		}else{
			setTextBackground(tv,getResources().getDrawable(R.drawable.tab_tv_bg_center));
//			tv.setBackground(getResources().getDrawable(R.drawable.tab_tv_bg_center));
		}
		
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setText(text);
		tv.setClickable(true);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tv.setLayoutParams(lp);
		return tv;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	private void setTextBackground(View view,Drawable background){
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			 view.setBackground(background);
		    } else {
		    view.setBackgroundDrawable(background);
		    }
	}

	protected void onDraw(Canvas canvas) {
		if (mVisibility == IndicatorVisibility.Visible) {
			if (mIndicatorShape == Shape.Rectangle) {
				canvas.drawRect((int) mTranslationX, mTop, (int) mTranslationX + mWidth, mTop + mHeight, mPaint); // 绘制该矩形
			} else {
				mPath = new Path();
				mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
				mPath.moveTo(0, 0);
				mPath.lineTo(mTriangleWidth, 0);
				mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
				mPath.close();
				canvas.save();
				canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
				canvas.drawPath(mPath, mPaint);
				canvas.restore();
			}
		}
		super.onDraw(canvas);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mTop = getMeasuredHeight(); // 测量的高度即指示符的顶部位置
		int width = getMeasuredWidth(); // 获取测量的总宽度
		int height = mTop + mHeight; // 重新定义一下测量的高度
		mWidth = width / mTabVisibleCount; // 指示符的宽度为总宽度/item的个数

		setMeasuredDimension(width, height);
	}

	/**
	 * 指示器跟随手指滚动，以及容器滚动
	 * 
	 * @param position
	 * @param offset
	 */
	public void scroll(int position, float offset) {
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */
		// 不断改变偏移量，invalidate
		mTranslationX = getWidth() / mTabVisibleCount * (position + offset);

		int tabWidth = getScreenWidth() / mTabVisibleCount;
		// 容器滚动，当移动到倒数最后一个的时候，开始滚动
		if (offset > 0 && position >= (mTabVisibleCount - 2) && getChildCount() > mTabVisibleCount) {
			if (mTabVisibleCount != 1) {
				this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * offset), 0);
			} else
			// 为count为1时 的特殊处理
			{
				this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
			}
		}

		this.invalidate();
	}

	/**
	 * 设置布局中view的一些必要属性；如果设置了setTabTitles，布局中view则无效
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		int cCount = getChildCount();

		if (cCount == 0)
			return;

		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		// 设置点击事件
		setItemClickEvent();

	}

	/**
	 * 获得屏幕的宽度
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
//		return getMeasuredWidth();
	}

}
