package com.hw.common.ui.marquee.library;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import com.hw.common.R;
import com.hw.common.ui.marquee.library.Animations.BaseAnimationInterface;
import com.hw.common.ui.marquee.library.Indicators.PagerIndicator;
import com.hw.common.ui.marquee.library.SliderTypes.BaseSliderView;
import com.hw.common.ui.marquee.library.Transformers.AccordionTransformer;
import com.hw.common.ui.marquee.library.Transformers.BackgroundToForegroundTransformer;
import com.hw.common.ui.marquee.library.Transformers.BaseTransformer;
import com.hw.common.ui.marquee.library.Transformers.CubeInTransformer;
import com.hw.common.ui.marquee.library.Transformers.DefaultTransformer;
import com.hw.common.ui.marquee.library.Transformers.DepthPageTransformer;
import com.hw.common.ui.marquee.library.Transformers.FadeTransformer;
import com.hw.common.ui.marquee.library.Transformers.FlipHorizontalTransformer;
import com.hw.common.ui.marquee.library.Transformers.FlipPageViewTransformer;
import com.hw.common.ui.marquee.library.Transformers.ForegroundToBackgroundTransformer;
import com.hw.common.ui.marquee.library.Transformers.RotateDownTransformer;
import com.hw.common.ui.marquee.library.Transformers.RotateUpTransformer;
import com.hw.common.ui.marquee.library.Transformers.StackTransformer;
import com.hw.common.ui.marquee.library.Transformers.TabletTransformer;
import com.hw.common.ui.marquee.library.Transformers.ZoomInTransformer;
import com.hw.common.ui.marquee.library.Transformers.ZoomOutSlideTransformer;
import com.hw.common.ui.marquee.library.Transformers.ZoomOutTransformer;
import com.hw.common.ui.marquee.library.Tricks.FixedSpeedScroller;
import com.hw.common.ui.marquee.library.Tricks.InfinitePagerAdapter;
import com.hw.common.ui.marquee.library.Tricks.InfiniteViewPager;
import com.hw.common.ui.marquee.library.Tricks.ViewPagerEx;
import com.hw.common.ui.marquee.library.Tricks.ViewPagerEx.OnPageChangeListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

/**
 * SliderLayout is compound layout. This is combined with
 * {@link com.hw.common.ui.marquee.library.Indicators.PagerIndicator} and
 * {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx} .
 *
 * There is some properties you can set in XML:
 *
 * indicator_visibility visible invisible
 *
 * indicator_shape oval rect
 *
 * indicator_selected_color
 *
 * indicator_unselected_color
 *
 * indicator_selected_drawable
 *
 * indicator_unselected_drawable
 *
 * pager_animation Default Accordion Background2Foreground CubeIn DepthPage Fade
 * FlipHorizontal FlipPage Foreground2Background RotateDown RotateUp Stack
 * Tablet ZoomIn ZoomOutSlide ZoomOut
 *
 * pager_animation_span
 *
 *
 */
public class SliderLayout extends RelativeLayout {

	private Context mContext;
	/**
	 * InfiniteViewPager is extended from ViewPagerEx. As the name says, it can
	 * scroll without bounder.
	 */
	private InfiniteViewPager mViewPager;

	/**
	 * InfiniteViewPager adapter.
	 */
	private SliderAdapter mSliderAdapter;

	/**
	 * {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx} indicator.
	 */
	private PagerIndicator mIndicator;

	/**
	 * A timer and a TimerTask using to cycle the
	 * {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx}.
	 */
	private Timer mCycleTimer;
	private TimerTask mCycleTask;

	/**
	 * For resuming the cycle, after user touch or click the
	 * {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx}.
	 */
	private Timer mResumingTimer;
	private TimerTask mResumingTask;

	/**
	 * If {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx} is Cycling
	 */
	private boolean mCycling;

	/**
	 * If auto recover after user touch the
	 * {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx}
	 */
	private boolean mAutoRecover;

	private int mTransformerId;

	/**
	 * {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx} transformer
	 * time span.
	 */
	private int mTransformerSpan;

	private boolean mAutoCycle;

	/**
	 * Visibility of
	 * {@link com.hw.common.ui.marquee.library.Indicators.PagerIndicator}
	 */
	private PagerIndicator.IndicatorVisibility mIndicatorVisibility = PagerIndicator.IndicatorVisibility.Visible;

	/**
	 * {@link com.hw.common.ui.marquee.library.Tricks.ViewPagerEx} 's
	 * transformer
	 */
	private BaseTransformer mViewPagerTransformer;

	/**
	 * @see com.hw.common.ui.marquee.library.Animations.BaseAnimationInterface
	 */
	private BaseAnimationInterface mCustomAnimation;

	/**
	 * {@link com.hw.common.ui.marquee.library.Indicators.PagerIndicator} shape,
	 * rect or oval.
	 */

	public SliderLayout(Context context) {
		this(context, null);
	}

	public SliderLayout(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.SliderStyle);
	}

	public SliderLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.slider_layout, this, true);

		final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SliderLayout, defStyle, 0);

		mTransformerSpan = attributes.getInteger(R.styleable.SliderLayout_pager_animation_span, 1100);
		mTransformerId = attributes.getInt(R.styleable.SliderLayout_pager_animation, Transformer.Default.ordinal());
		mAutoCycle = attributes.getBoolean(R.styleable.SliderLayout_auto_cycle, true);
		int visibility = attributes.getInt(R.styleable.SliderLayout_indicator_visibility, 0);
		for (PagerIndicator.IndicatorVisibility v : PagerIndicator.IndicatorVisibility.values()) {
			if (v.ordinal() == visibility) {
				mIndicatorVisibility = v;
				break;
			}
		}
		mSliderAdapter = new SliderAdapter(mContext);
		PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mSliderAdapter);

		mViewPager = (InfiniteViewPager) findViewById(R.id.daimajia_slider_viewpager);
		mViewPager.setAdapter(wrappedAdapter);

		mViewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_UP:
					recoverCycle();
					break;
				}
				return false;
			}
		});

		initImageLoader();
		
		attributes.recycle();
		setPresetIndicator(PresetIndicators.Center_Bottom);
		setPresetTransformer(mTransformerId);
		setSliderTransformDuration(mTransformerSpan, null);
		setIndicatorVisibility(mIndicatorVisibility);
		if (mAutoCycle) {
			startAutoCycle();
		}
	}

	private void initImageLoader() {
//		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ic_picture_loadfailed).cacheInMemory(true)
//				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
//
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).threadPoolSize(5).threadPriority(Thread.NORM_PRIORITY)
//				.tasksProcessingOrder(QueueProcessingType.LIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024)
//				.memoryCacheSizePercentage(13).discCache(new UnlimitedDiscCache(StorageUtils.getCacheDirectory(mContext, true)))
//				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100).discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//				.imageDownloader(new BaseImageDownloader(mContext))
//				.imageDecoder(new BaseImageDecoder(false))
//				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//				.defaultDisplayImageOptions(imageOptions).build();
//		ImageLoader.getInstance().init(config);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnFail(R.drawable.ic_picture_loadfailed)
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.displayer(new FadeInBitmapDisplayer(300)) // 设置图片加载好后渐入的动画时间
			.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).threadPoolSize(3)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).discCache(new UnlimitedDiskCache(StorageUtils.getCacheDirectory(mContext, true))) // 自定义缓存路径
				.memoryCache(new WeakMemoryCache()).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5
				.tasksProcessingOrder(QueueProcessingType.LIFO).defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);
	}

	public void setCustomIndicator(PagerIndicator indicator) {
		if (mIndicator != null) {
			mIndicator.destroySelf();
		}
		mIndicator = indicator;
		mIndicator.setIndicatorVisibility(mIndicatorVisibility);
		mIndicator.setViewPager(mViewPager);
		mIndicator.redraw();
	}

	public <T extends BaseSliderView> void addSlider(T imageContent) {
		mSliderAdapter.addSlider(imageContent);
	}

	public void startAutoCycle() {
		startAutoCycle(1000, 3400, true);
	}

	/**
	 * start auto cycle.
	 * 
	 * @param delay
	 *            delay time
	 * @param period
	 *            period time.
	 * @param autoRecover
	 */
	public void startAutoCycle(long delay, long period, boolean autoRecover) {
		mCycleTimer = new Timer();
		mAutoRecover = autoRecover;
		mCycleTask = new TimerTask() {
			@Override
			public void run() {
				mh.sendEmptyMessage(0);
			}
		};
		mCycleTimer.schedule(mCycleTask, delay, period);
		mCycling = true;
	}

	/**
	 * pause auto cycle.
	 */
	private void pauseAutoCycle() {
		if (mCycling) {
			mCycleTimer.cancel();
			mCycleTask.cancel();
			mCycling = false;
		} else {
			if (mResumingTimer != null && mResumingTask != null) {
				recoverCycle();
			}
		}
	}

	/**
	 * stop the auto circle
	 */
	public void stopAutoCycle() {
		if (mCycleTask != null) {
			mCycleTask.cancel();
		}
		if (mCycleTimer != null) {
			mCycleTimer.cancel();
		}
		if (mResumingTimer != null) {
			mResumingTimer.cancel();
		}
		if (mResumingTask != null) {
			mResumingTask.cancel();
		}
	}

	/**
	 * when paused cycle, this method can weak it up.
	 */
	private void recoverCycle() {

		if (!mAutoRecover) {
			return;
		}

		if (!mCycling) {
			if (mResumingTask != null && mResumingTimer != null) {
				mResumingTimer.cancel();
				mResumingTask.cancel();
			}
			mResumingTimer = new Timer();
			mResumingTask = new TimerTask() {
				@Override
				public void run() {
					startAutoCycle();
				}
			};
			mResumingTimer.schedule(mResumingTask, 6000);
		}
	}

	private android.os.Handler mh = new android.os.Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mViewPager.nextItem();
		}
	};

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			pauseAutoCycle();
			break;
		}
		return false;
	}

	/**
	 * set ViewPager transformer.
	 * 
	 * @param reverseDrawingOrder
	 * @param transformer
	 */
	public void setPagerTransformer(boolean reverseDrawingOrder, BaseTransformer transformer) {
		mViewPagerTransformer = transformer;
		mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
		mViewPager.setPageTransformer(reverseDrawingOrder, mViewPagerTransformer);
	}

	/**
	 * set the duration between two slider changes.
	 * 
	 * @param period
	 * @param interpolator
	 */
	public void setSliderTransformDuration(int period, Interpolator interpolator) {
		try {
			Field mScroller = ViewPagerEx.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), interpolator, period);
			mScroller.set(mViewPager, scroller);
		} catch (Exception e) {

		}
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mViewPager.setOnPageChangeListener(listener);
	}

	public void setOnIndicatorChangeListener(OnPageChangeListener listener) {
		mIndicator.setOnPageChangeListener(listener);
	}

	/**
	 * preset transformers and their names
	 */
	public enum Transformer {
		Default("Default"), Accordion("Accordion"), Background2Foreground("Background2Foreground"), CubeIn("CubeIn"), DepthPage("DepthPage"), Fade("Fade"), FlipHorizontal("FlipHorizontal"), FlipPage(
				"FlipPage"), Foreground2Background("Foreground2Background"), RotateDown("RotateDown"), RotateUp("RotateUp"), Stack("Stack"), Tablet("Tablet"), ZoomIn("ZoomIn"), ZoomOutSlide(
				"ZoomOutSlide"), ZoomOut("ZoomOut");

		private final String name;

		private Transformer(String s) {
			name = s;
		}

		public String toString() {
			return name;
		}

		public boolean equals(String other) {
			return (other == null) ? false : name.equals(other);
		}
	};

	/**
	 * set a preset viewpager transformer by id.
	 * 
	 * @param transformerId
	 */
	public void setPresetTransformer(int transformerId) {
		for (Transformer t : Transformer.values()) {
			if (t.ordinal() == transformerId) {
				setPresetTransformer(t);
				break;
			}
		}
	}

	/**
	 * set preset PagerTransformer via the name of transforemer.
	 * 
	 * @param transformerName
	 */
	public void setPresetTransformer(String transformerName) {
		for (Transformer t : Transformer.values()) {
			if (t.equals(transformerName)) {
				setPresetTransformer(t);
				return;
			}
		}
	}

	/**
	 * Inject your custom animation into PageTransformer, you can know more
	 * details in
	 * {@link com.hw.common.ui.marquee.library.Animations.BaseAnimationInterface}
	 * , and you can see a example in
	 * {@link com.hw.common.ui.marquee.library.Animations.DescriptionAnimation}
	 * 
	 * @param animation
	 */
	public void setCustomAnimation(BaseAnimationInterface animation) {
		mCustomAnimation = animation;
		if (mViewPagerTransformer != null) {
			mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
		}
	}

	/**
	 * pretty much right? enjoy it. :-D
	 *
	 * @param ts
	 */
	public void setPresetTransformer(Transformer ts) {
		//
		// special thanks to https://github.com/ToxicBakery/ViewPagerTransforms
		//
		BaseTransformer t = null;
		switch (ts) {
		case Default:
			t = new DefaultTransformer();
			break;
		case Accordion:
			t = new AccordionTransformer();
			break;
		case Background2Foreground:
			t = new BackgroundToForegroundTransformer();
			break;
		case CubeIn:
			t = new CubeInTransformer();
			break;
		case DepthPage:
			t = new DepthPageTransformer();
			break;
		case Fade:
			t = new FadeTransformer();
			break;
		case FlipHorizontal:
			t = new FlipHorizontalTransformer();
			break;
		case FlipPage:
			t = new FlipPageViewTransformer();
			break;
		case Foreground2Background:
			t = new ForegroundToBackgroundTransformer();
			break;
		case RotateDown:
			t = new RotateDownTransformer();
			break;
		case RotateUp:
			t = new RotateUpTransformer();
			break;
		case Stack:
			t = new StackTransformer();
			break;
		case Tablet:
			t = new TabletTransformer();
			break;
		case ZoomIn:
			t = new ZoomInTransformer();
			break;
		case ZoomOutSlide:
			t = new ZoomOutSlideTransformer();
			break;
		case ZoomOut:
			t = new ZoomOutTransformer();
			break;
		}
		setPagerTransformer(true, t);
	}

	/**
	 * Set the visibility of the indicators.
	 * 
	 * @param visibility
	 */
	public void setIndicatorVisibility(PagerIndicator.IndicatorVisibility visibility) {
		if (mIndicator == null) {
			return;
		}

		mIndicator.setIndicatorVisibility(visibility);
	}

	public PagerIndicator.IndicatorVisibility getIndicatorVisibility() {
		if (mIndicator == null) {
			return mIndicator.getIndicatorVisibility();
		}
		return PagerIndicator.IndicatorVisibility.Invisible;

	}

	/**
	 * get the
	 * {@link com.hw.common.ui.marquee.library.Indicators.PagerIndicator}
	 * instance. You can manipulate the properties of the indicator.
	 * 
	 * @return
	 */
	public PagerIndicator getPagerIndicator() {
		return mIndicator;
	}

	public enum PresetIndicators {
		Center_Bottom("Center_Bottom", R.id.default_center_bottom_indicator), Right_Bottom("Right_Bottom", R.id.default_bottom_right_indicator), Left_Bottom("Left_Bottom",
				R.id.default_bottom_left_indicator), Center_Top("Center_Top", R.id.default_center_top_indicator), Right_Top("Right_Top", R.id.default_center_top_right_indicator), Left_Top("Left_Top",
				R.id.default_center_top_left_indicator);

		private final String name;
		private final int id;

		private PresetIndicators(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public String toString() {
			return name;
		}

		public int getResourceId() {
			return id;
		}
	}

	public void setPresetIndicator(PresetIndicators presetIndicator) {
		PagerIndicator pagerIndicator = (PagerIndicator) findViewById(presetIndicator.getResourceId());
		setCustomIndicator(pagerIndicator);
	}

	public int getShouldDrawCount() {
		// if(mViewPager.getAdapter() instanceof InfinitePagerAdapter){
		// return
		// ((InfinitePagerAdapter)mViewPager.getAdapter()).getRealCount();
		// }else{
		return mViewPager.getAdapter().getCount();
		// }
	}

	// 设置viewPaper缓存数量
	public void setOffscreenPageLimit(int limit) {
		mViewPager.setOffscreenPageLimit(limit);
	}

	private InfinitePagerAdapter getWrapperAdapter() {
		PagerAdapter adapter = mViewPager.getAdapter();
		if (adapter != null) {
			return (InfinitePagerAdapter) adapter;
		} else {
			return null;
		}
	}

	private SliderAdapter getRealAdapter() {
		PagerAdapter adapter = mViewPager.getAdapter();
		if (adapter != null) {
			return ((InfinitePagerAdapter) adapter).getRealAdapter();
		}
		return null;
	}

	/**
	 * remove the slider at the position. Notice: It's a not perfect method, a
	 * very small bug still exists.
	 */
	public void removeSliderAt(int position) {
		if (getRealAdapter() != null) {
			getRealAdapter().removeSliderAt(position);
			mViewPager.setCurrentItem(mViewPager.getCurrentItem(), false);
		}
	}

	/**
	 * remove all the sliders. Notice: It's a not perfect method, a very small
	 * bug still exists.
	 */
	public void removeAllSliders() {
		if (getRealAdapter() != null) {
			int count = getRealAdapter().getCount();
			getRealAdapter().removeAllSliders();
			// a small bug, but fixed by this trick.
			// bug: when remove adapter's all the sliders.some caching slider
			// still alive.
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + count, false);
		}
	}
}
