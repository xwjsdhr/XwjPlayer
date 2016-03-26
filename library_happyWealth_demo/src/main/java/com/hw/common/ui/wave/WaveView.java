package com.hw.common.ui.wave;

import com.hw.common.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by John on 2014/10/15.
 */
public class WaveView extends LinearLayout {
    protected static final int LARGE = 1;
    protected static final int MIDDLE = 2;
    protected static final int LITTLE = 3;

    private int mAboveWaveColor;
    private int mBlowWaveColor;
    private int mProgress;
    private int mWaveHeight;
    private int mWaveMultiple;
    private int mWaveHz;

    private int mWaveToTop;

    private Wave mWave;
    private Solid mSolid;

    private final int DEFAULT_ABOVE_WAVE_COLOR = Color.WHITE;
    private final int DEFAULT_BLOW_WAVE_COLOR = Color.WHITE;
    private final int DEFAULT_PROGRESS = 0;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveView, R.attr.waveViewStyle, 0);
        mAboveWaveColor = attributes.getColor(R.styleable.WaveView_above_wave_color, DEFAULT_ABOVE_WAVE_COLOR);
        mBlowWaveColor = attributes.getColor(R.styleable.WaveView_blow_wave_color, DEFAULT_BLOW_WAVE_COLOR);
        mProgress = attributes.getInt(R.styleable.WaveView_wave_progress, DEFAULT_PROGRESS);
        mWaveHeight = attributes.getInt(R.styleable.WaveView_wave_height, MIDDLE);
        mWaveMultiple = attributes.getInt(R.styleable.WaveView_wave_length, LARGE);
        mWaveHz = attributes.getInt(R.styleable.WaveView_wave_hz, MIDDLE);
        attributes.recycle();

        mWave = new Wave(context, null);
        mWave.initializeWaveSize(mWaveMultiple, mWaveHeight, mWaveHz);
        mWave.setAboveWaveColor(mAboveWaveColor);
        mWave.setBlowWaveColor(mBlowWaveColor);
        mWave.initializePainters();

        mSolid = new Solid(context, null);
        mSolid.setAboveWavePaint(mWave.getAboveWavePaint());
        mSolid.setBlowWavePaint(mWave.getBlowWavePaint());
        
        addView(mWave);
        addView(mSolid);

        setProgress(mProgress);
    }

    public void setProgress(int progress) {
        this.mProgress = progress > 100 ? 100 : progress;
        computeWaveToTop();
    }

    public int getmProgress() {
		return mProgress;
	}

	@Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            computeWaveToTop();
        }
    }

    @SuppressLint("NewApi")
	private void computeWaveToTop() {
        mWaveToTop = (int) (getHeight() * (1f - mProgress / 100f));
        ViewGroup.LayoutParams params = mWave.getLayoutParams();
        if (params != null) {
            ((LayoutParams) params).topMargin = mWaveToTop;
        }
        mWave.setLayoutParams(params);
        
//        double per = Double.valueOf((1f - mProgress / 100) * getMeasuredHeight());
////        double per = Double.valueOf( mProgress / 100 * getMeasuredHeight());
//        Log.e("getHeight()-------- ",mWave.getHeight()+"");
//        Log.e("mProgress-------- ",mProgress+"");
//        Log.e("per-------- ",per+"");
//		ObjectAnimator.ofInt(new ViewWrapper(mWave), "marginTop", (int) per).setDuration(5500).start();
//		
//        double per = Double.valueOf(res_QianBao.getPer().toString()) / 100 * fram_buy_product_progressbar.getMeasuredWidth();
//		ObjectAnimator.ofInt(new ViewWrapper(tv_buy_product_progressbar), "width", (int) per).setDuration(1500).start();
    }
    
    class ViewWrapper {
		private View mTarget;

		public ViewWrapper(View target) {
			mTarget = target;
		}

		public int getWidth() {
			return mTarget.getLayoutParams().width;
		}
		
		@SuppressLint("NewApi")
		public void setMarginTop(int marginTop){
			ViewGroup.LayoutParams params = mTarget.getLayoutParams();
			((LayoutParams) params).topMargin = mTarget.getLayoutParams().height - marginTop;
			mTarget.setLayoutParams(params);
		}
		
		public int getMarginTop(){
			return mTarget.getTop();
		}

		public void setWidth(int width) {
			mTarget.getLayoutParams().width = width;
			mTarget.requestLayout();
		}

		public int getHeight() {
			Log.e("mTarget.getLayoutParams().height-------- ",mTarget.getLayoutParams().height+"");
			return mTarget.getLayoutParams().height;
		}

		public void setHeight(int height) {
			Log.e("heightheight-------- ",height+"");
			mTarget.getLayoutParams().height = height;
			mTarget.requestLayout();
		}

	}

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.progress = mProgress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
    }

    private static class SavedState extends BaseSavedState {
        int progress;

        /**
         * Constructor called from {@link android.widget.ProgressBar#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
