package com.hw.common.ui.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by John on 2014/10/15.
 */
class Solid extends View {

    private Paint aboveWavePaint;
    private Paint blowWavePaint;

	public Solid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Solid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        setLayoutParams(params);
    }

    public void setAboveWavePaint(Paint aboveWavePaint) {
        this.aboveWavePaint = aboveWavePaint;
    }

    public void setBlowWavePaint(Paint blowWavePaint) {
        this.blowWavePaint = blowWavePaint;
    }
    
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        int centre = getWidth()/2; //获取圆心的x坐标
//		int radius = (int) (centre - 100/2-5); //圆环的半径
//		canvas.drawCircle(centre, centre, radius, blowWavePaint);
//		canvas.drawCircle(centre, centre, radius, aboveWavePaint);
        
        canvas.drawRect(getLeft(), 0, getRight(), getBottom(), blowWavePaint);
        canvas.drawRect(getLeft(), 0, getRight(), getBottom(), aboveWavePaint);
    }
}
