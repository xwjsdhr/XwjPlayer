package com.hw.common.ui.marquee.library.SliderTypes;

import java.io.File;

import com.hw.common.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods. Such loadImage, setImage,and so
 * on. I provide two example:
 * {@link com.hw.common.ui.marquee.library.SliderTypes.DefaultSliderView} and
 * {@link com.hw.common.ui.marquee.library.SliderTypes.TextSliderView} if you
 * want to show progressbar, you just need to set a progressbar id as
 * 
 * @+id/loading_bar.
 */
public abstract class BaseSliderView {

	protected Context mContext;

	private Bundle mBundle;

	/**
	 * Error place holder image.
	 */
	private int mErrorPlaceHolderRes;

	/**
	 * Empty imageView placeholder.
	 */
	private int mEmptyPlaceHolderRes;

	private int LoadingPlaceHolderRes;

	private String mUrl;
	private File mFile;
	private int mRes;

	protected OnSliderClickListener mOnSliderClickListener;

	private boolean mErrorDisappear;

	private ImageLoadListener mLoadListener;

	private String mDescription;
	private DisplayImageOptions options;

	protected BaseSliderView(Context context) {
		mContext = context;
		this.mBundle = new Bundle();
	}

	/**
	 * the placeholder image when loading image from url or file.
	 * 
	 * @param resId
	 *            Image resource id
	 * @return
	 */
	public BaseSliderView empty(int resId) {
		mEmptyPlaceHolderRes = resId;
		return this;
	}

	public BaseSliderView loading(int resId) {
		LoadingPlaceHolderRes = resId;
		options = new DisplayImageOptions.Builder().showImageOnLoading(resId).showImageOnFail(R.drawable.ic_picture_loadfailed).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.displayer(new FadeInBitmapDisplayer(300)) // 设置图片加载好后渐入的动画时间
				.build(); // 创建配置过得DisplayImageOption对象
		return this;
	}

	/**
	 * determine whether remove the image which failed to download or load from
	 * file
	 * 
	 * @param disappear
	 * @return
	 */
	public BaseSliderView errorDisappear(boolean disappear) {
		mErrorDisappear = disappear;
		return this;
	}

	/**
	 * if you set errorDisappear false, this will set a error placeholder image.
	 * 
	 * @param resId
	 *            image resource id
	 * @return
	 */
	public BaseSliderView error(int resId) {
		mErrorPlaceHolderRes = resId;
		return this;
	}

	/**
	 * the description of a slider image.
	 * 
	 * @param description
	 * @return
	 */
	public BaseSliderView description(String description) {
		mDescription = description;
		return this;
	}

	/**
	 * set a url as a web that preparing to load
	 * 
	 * @param url
	 * @return
	 */
	public BaseSliderView WebView(String url) {
		if (mFile != null || mRes != 0) {
			throw new IllegalStateException("Call multi image function," + "you only have permission to call it once");
		}
		mUrl = url;
		return this;
	}

	public BaseSliderView LayoutView() {
		return this;
	}

	/**
	 * set a url as a image that preparing to load
	 * 
	 * @param url
	 * @return
	 */
	public BaseSliderView image(String url) {
		if (mFile != null || mRes != 0) {
			throw new IllegalStateException("Call multi image function," + "you only have permission to call it once");
		}
		mUrl = url;
		return this;
	}

	/**
	 * set a file as a image that will to load
	 * 
	 * @param file
	 * @return
	 */
	public BaseSliderView image(File file) {
		if (mUrl != null || mRes != 0) {
			throw new IllegalStateException("Call multi image function," + "you only have permission to call it once");
		}
		mFile = file;
		return this;
	}

	public BaseSliderView image(int res) {
		if (mUrl != null || mFile != null) {
			throw new IllegalStateException("Call multi image function," + "you only have permission to call it once");
		}
		mRes = res;
		return this;
	}

	public String getUrl() {
		return mUrl;
	}

	public boolean isErrorDisappear() {
		return mErrorDisappear;
	}

	public int getEmpty() {
		return mEmptyPlaceHolderRes;
	}

	public int getError() {
		return mErrorPlaceHolderRes;
	}

	public int getLoading() {
		return LoadingPlaceHolderRes;
	}

	public String getDescription() {
		return mDescription;
	}

	public Context getContext() {
		return mContext;
	}

	/**
	 * set a slider image click listener
	 * 
	 * @param l
	 * @return
	 */
	public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
		mOnSliderClickListener = l;
		return this;
	}

	protected void loadWeb(WebView targetWebView) {
		final BaseSliderView me = this;
		WebSettings webSettings = targetWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setRenderPriority(RenderPriority.HIGH);
		// webSettings.setSupportZoom(true);
		// webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //设置 缓存模式
		// webSettings.setDisplayZoomControls(false);
		// webSettings.setBuiltInZoomControls(true);
		// webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
		// webSettings.setDatabaseEnabled(true); //开启 database storage API 功能
		// webSettings.setAppCacheEnabled(true); //开启 Application Caches 功能
		targetWebView.setWebViewClient(new WebViewClient() {
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

			public boolean shouldOverrideUrlLoading(WebView webview, String url) {
				webview.loadUrl(url);
				return true;
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mLoadListener.onStart(me);
			}

			public void onPageFinished(WebView view, String url) {
				if (mLoadListener != null) {
					mLoadListener.onEnd(false, me);
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				if (mLoadListener != null) {
					mLoadListener.onEnd(false, me);
				}
			}
		});

		targetWebView.loadUrl(mUrl);
	}

	/**
	 * when you want to extends this class, please use this method to load a
	 * image to a imageview.
	 * 
	 * @param targetImageView
	 */
	protected void loadImage(ImageView targetImageView) {
		// final BaseSliderView me = this;
		// mLoadListener.onStart(me);

		// MLogUtil.e("mUrl "+mUrl);

		if(options == null){
			options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ic_picture_loadfailed).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
					.displayer(new FadeInBitmapDisplayer(300)) // 设置图片加载好后渐入的动画时间
					.build(); // 创建配置过得DisplayImageOption对象
		}

		ImageLoader.getInstance().displayImage(mUrl, targetImageView, options);

		// Picasso p = Picasso.with(mContext);
		// RequestCreator rq = null;
		// if(mUrl!=null){
		// rq = p.load(mUrl);
		// }else if(mFile != null){
		// rq = p.load(mFile);
		// }else if(mRes != 0){
		// rq = p.load(mRes);
		// }else{
		// return;
		// }
		// if(rq == null){
		// return;
		// }
		//
		// if(getEmpty() != 0){
		// rq.placeholder(getEmpty());
		// }else{
		// // rq.placeholder(R.drawable.img_loading);
		// rq.placeholder(getLoading());
		// }
		//
		// if(getError() != 0){
		// rq.error(getError());
		// }
		// rq.fit();
		//
		// rq.into(targetImageView,new Callback() {
		// @Override
		// public void onSuccess() {
		// if(progressBar !=null)
		// progressBar.setVisibility(View.INVISIBLE);
		// }
		//
		// @Override
		// public void onError() {
		// if(mLoadListener != null){
		// mLoadListener.onEnd(false,me);
		// }
		// }
		// });
	}

	private View progressBar = null;

	/**
	 * when you want to extends this class, you must call this method to bind
	 * click event to your view.
	 * 
	 * @param v
	 */
	protected void bindClickEvent(View v) {
		final BaseSliderView me = this;
		progressBar = v.findViewById(R.id.loading_bar);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnSliderClickListener != null) {
					mOnSliderClickListener.onSliderClick(me);
				}
			}
		});
	}

	/**
	 * the extended class have to implement getView(), which is called by the
	 * adapter, every extended class response to render their own view.
	 * 
	 * @return
	 */
	public abstract View getView();

	/**
	 * set a listener to get a message , if load error.
	 * 
	 * @param l
	 */
	public void setOnImageLoadListener(ImageLoadListener l) {
		mLoadListener = l;
	}

	public interface OnSliderClickListener {
		public void onSliderClick(BaseSliderView slider);
	}

	/**
	 * when you have some extra information, please put it in this bundle.
	 * 
	 * @return
	 */
	public Bundle getBundle() {
		return mBundle;
	}

	public interface ImageLoadListener {
		public void onStart(BaseSliderView target);

		public void onEnd(boolean result, BaseSliderView target);
	}

}
