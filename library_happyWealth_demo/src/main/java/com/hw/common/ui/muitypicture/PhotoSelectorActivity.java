package com.hw.common.ui.muitypicture;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hw.common.R;
import com.hw.common.ui.muitypicture.PhotoItem.onItemClickListener;
import com.hw.common.ui.muitypicture.PhotoItem.onPhotoItemCheckedListener;
import com.hw.common.utils.basicUtils.MLogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;

public class PhotoSelectorActivity extends Activity implements
		onItemClickListener, onPhotoItemCheckedListener, OnItemClickListener,
		OnClickListener {

	public static final int SINGLE_IMAGE = 1;
	public static final String KEY_MAX = "key_max";
	private int MAX_IMAGE = 5;

	public static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_CAMERA = 1;

	public static String RECCENT_PHOTO = null;

	private GridView gvPhotos;
	private ListView lvAblum;
	private Button btnOk;
	private ImageView btn_camera;
	private TextView tvAlbum, tvPreview, tvTitle;
	private PhotoSelectorDomain photoSelectorDomain;
	private PhotoSelectorAdapter photoAdapter;
	private AlbumAdapter albumAdapter;
	private RelativeLayout layoutAlbum;
	private ArrayList<PhotoModel> selected;
	private TextView tvNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RECCENT_PHOTO = getResources().getString(R.string.recent_photos);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_photoselector);

		if (getIntent().getExtras() != null) {
			MAX_IMAGE = getIntent().getIntExtra(KEY_MAX, 5);
		}

		initImageLoader();

		photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

		selected = new ArrayList<PhotoModel>();

		tvTitle = (TextView) findViewById(R.id.tv_title_lh);
		gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
		lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
		btnOk = (Button) findViewById(R.id.btn_right_lh);
		tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
		tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
		layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		btn_camera = (ImageView) findViewById(R.id.btn_camera);

		btnOk.setOnClickListener(this);
		btn_camera.setOnClickListener(this);
		tvAlbum.setOnClickListener(this);
		tvPreview.setOnClickListener(this);

		photoAdapter = new PhotoSelectorAdapter(getApplicationContext(),
				new ArrayList<PhotoModel>(), CommonUtils.getWidthPixels(this),
				this, this, this);
		gvPhotos.setAdapter(photoAdapter);

		albumAdapter = new AlbumAdapter(getApplicationContext(),
				new ArrayList<AlbumModel>());
		lvAblum.setAdapter(albumAdapter);
		lvAblum.setOnItemClickListener(this);

		findViewById(R.id.bv_back_lh).setOnClickListener(this); // 返回

		photoSelectorDomain.getReccent(reccentListener); // 更新最近照片
		photoSelectorDomain.updateAlbum(albumListener); // 跟新相册信息
	}

	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_right_lh)
			ok(); // 选完照片
		else if (v.getId() == R.id.tv_album_ar)
			album();
		else if (v.getId() == R.id.tv_preview_ar)
			priview();
		else if (v.getId() == R.id.btn_camera)
			catchPicture();
		else if (v.getId() == R.id.bv_back_lh)
			finish();
	}

	/** 拍照 */
	String cameraPath;
	private void catchPicture() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File dir = new File(CommonUtils.SDPATH);
		if(!dir.exists()){
			dir.mkdir();
		}
		cameraPath = CommonUtils.SDPATH + System.currentTimeMillis()+ ".jpg";
		File file = new File(cameraPath);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		CommonUtils.launchActivityForResult(this, openCameraIntent, REQUEST_CAMERA);
		
//		CommonUtils.launchActivityForResult(this, new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			
			Log.e("selected.size()",selected.size()+"");
			Log.e("MAX_IMAGE",MAX_IMAGE+"");
			Log.e("data",data+"");
			
//			String fileName = System.currentTimeMillis()+ ".jpg";
//			CommonUtils.saveBitmap((Bitmap) data.getParcelableExtra("data"), fileName);
//			PhotoModel photoModel = new PhotoModel(CommonUtils.SDPATH + fileName);
			
			PhotoModel photoModel = new PhotoModel(cameraPath);
			
			MLogUtil.e("cameraPath "+cameraPath);
			MLogUtil.e("photoModel "+photoModel.getOriginalPath());
			MLogUtil.e("selected.contains(photoModel) "+selected.contains(photoModel));
			
			if (selected.size() >= MAX_IMAGE) {
				Toast.makeText(this,String.format(getString(R.string.max_img_limit_reached),MAX_IMAGE), Toast.LENGTH_SHORT).show();
				photoModel.setChecked(false);
				photoAdapter.notifyDataSetChanged();
			} else {
				if (!selected.contains(photoModel)) {
					selected.add(photoModel);
				}
			}
			tvNumber.setText("(" + selected.size() + ")");
			if (selected.isEmpty()) {
				tvPreview.setEnabled(false);
				tvPreview.setText(getString(R.string.preview));
			}else{
				tvPreview.setEnabled(true);
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("selected",selected);
	}
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			selected = (ArrayList<PhotoModel>) savedInstanceState.getSerializable("selected");
		}
	}

	/** 完成 */
	private void ok() {
		if (selected.isEmpty()) {
			setResult(RESULT_CANCELED);
		} else {
			MLogUtil.e("selected.size() "+selected.size());
//			for(PhotoModel PhotoModel : selected){
//				Log.e("photos", PhotoModel.getOriginalPath());
//			}
			
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("photos", selected);
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
		}
		finish();
	}

	/** 预览照片 */
	private void priview() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("photos", selected);
		CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}

	private void album() {
		if (layoutAlbum.getVisibility() == View.GONE) {
			popAlbum();
		} else {
			hideAlbum();
		}
	}

	/** 弹出相册列表 */
	private void popAlbum() {
		layoutAlbum.setVisibility(View.VISIBLE);
		new AnimationUtil(getApplicationContext(), R.anim.translate_up_current)
				.setLinearInterpolator().startAnimation(layoutAlbum);
	}

	/** 隐藏相册列表 */
	private void hideAlbum() {
		new AnimationUtil(getApplicationContext(), R.anim.translate_down)
				.setLinearInterpolator().startAnimation(layoutAlbum);
		layoutAlbum.setVisibility(View.GONE);
	}

	/** 清空选中的图片 */
	private void reset() {
		selected.clear();
		tvNumber.setText("(0)");
		tvPreview.setEnabled(false);
	}

	@Override
	/** 点击查看照片 */
	public void onItemClick(int position) {
		Bundle bundle = new Bundle();
		if (tvAlbum.getText().toString().equals(RECCENT_PHOTO))
			bundle.putInt("position", position - 1);
		else
			bundle.putInt("position", position);
		bundle.putString("album", tvAlbum.getText().toString());
		CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}

	@Override
	/** 照片选中状态改变之后 */
	public void onCheckedChanged(PhotoModel photoModel,
			CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (selected.size() >= MAX_IMAGE) {
				Toast.makeText(this,String.format(getString(R.string.max_img_limit_reached),MAX_IMAGE), Toast.LENGTH_SHORT).show();
				photoModel.setChecked(false);
				photoAdapter.notifyDataSetChanged();
			} else {
				if (!selected.contains(photoModel)) {
					selected.add(photoModel);
				}
			}
			tvPreview.setEnabled(true);
		} else {
			selected.remove(photoModel);
		}
		tvNumber.setText("(" + selected.size() + ")");

		if (selected.isEmpty()) {
			tvPreview.setEnabled(false);
			tvPreview.setText(getString(R.string.preview));
		}
	}

	@Override
	public void onBackPressed() {
		if (layoutAlbum.getVisibility() == View.VISIBLE) {
			hideAlbum();
		} else
			super.onBackPressed();
	}

	@Override
	/** 相册列表点击事件 */
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
			for (int i = 0; i < parent.getCount(); i++) {
				AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
				if (i == position)
					album.setCheck(true);
				else
					album.setCheck(false);
			}
			albumAdapter.notifyDataSetChanged();
			hideAlbum();
			tvAlbum.setText(current.getName());
			// tvTitle.setText(current.getName());

			// 更新照片列表
			if (current.getName().equals(RECCENT_PHOTO))
				photoSelectorDomain.getReccent(reccentListener);
			else
				photoSelectorDomain.getAlbum(current.getName(), reccentListener); // 获取选中相册的照片
	}

	/** 获取本地图库照片回调 */
	public interface OnLocalReccentListener {
		public void onPhotoLoaded(List<PhotoModel> photos);
	}

	/** 获取本地相册信息回调 */
	public interface OnLocalAlbumListener {
		public void onAlbumLoaded(List<AlbumModel> albums);
	}

	private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
		@Override
		public void onAlbumLoaded(List<AlbumModel> albums) {
			albumAdapter.update(albums);
		}
	};

	private OnLocalReccentListener reccentListener = new OnLocalReccentListener() {
		@Override
		public void onPhotoLoaded(List<PhotoModel> photos) {
			for (PhotoModel model : photos) {
				if (selected.contains(model)) {
					model.setChecked(true);
				}
			}
			photoAdapter.update(photos);
			gvPhotos.smoothScrollToPosition(0); // 滚动到顶端
			// reset(); //--keep selected photos

		}
	};
	
	private void initImageLoader() {
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_picture_loading)
		.showImageOnFail(R.drawable.ic_picture_loadfailed)
		.cacheInMemory(false).cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();

//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(3)// 线程池内加载的数量
//				.threadPriority(Thread.NORM_PRIORITY - 2).discCache(new UnlimitedDiscCache(cacheDir)) // 自定义缓存路径
//				.memoryCache(new WeakMemoryCache()).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5
//				.tasksProcessingOrder(QueueProcessingType.LIFO).defaultDisplayImageOptions(options).writeDebugLogs().build();
		
ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
		this)
		.memoryCacheExtraOptions(400, 400)
		.threadPoolSize(3)
		.threadPriority(Thread.NORM_PRIORITY - 1)
		.denyCacheImageMultipleSizesInMemory()
		.memoryCache(new WeakMemoryCache())
		.discCache(
				new UnlimitedDiskCache(StorageUtils.getCacheDirectory(
						this, true)))
		// default
		.discCacheFileCount(100)
		.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
		// default
		.imageDownloader(new BaseImageDownloader(this))
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		// default
		.defaultDisplayImageOptions(imageOptions).build();
		
		/*DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_picture_loading)
				.showImageOnFail(R.drawable.ic_picture_loadfailed)
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true).considerExifParams(false)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCacheExtraOptions(400, 400)
				// default = device screen dimensions
//				.diskCacheExtraOptions(400, 400, null)
				.threadPoolSize(5)
				// default Thread.NORM_PRIORITY - 1
				.threadPriority(Thread.NORM_PRIORITY)
				// default FIFO
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13)
				// default
				.discCache(
						new UnlimitedDiscCache(StorageUtils.getCacheDirectory(
								this, true)))
				// default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// default
				.imageDownloader(new BaseImageDownloader(this))
				// default
				.imageDecoder(new BaseImageDecoder(false))
				// default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// default
				.defaultDisplayImageOptions(imageOptions).build();*/

		ImageLoader.getInstance().init(config);
	}
}
