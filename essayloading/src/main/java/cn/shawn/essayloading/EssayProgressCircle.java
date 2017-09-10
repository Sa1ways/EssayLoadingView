package cn.shawn.essayloading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by shawn on 17-9-10.
 * a material progress view
 */

public class EssayProgressCircle extends View {

    public static final String TAG = EssayProgressCircle.class.getSimpleName();

    public static final int DEFAULT_PAINT_WIDTH = 20;

    public static final int DEFAULT_MIN_SWEEP_ANGLE = 30;

    public static final int DEFAULT_MAX_SWEEP_ANGLE = 270;

    public static final int DEFAULT_CIRCLE_STEPS = 6;

    public static final int DEFAULT_SWEEP_STEPS = 5;

    private int mColor;

    private int mPaintWidth ;

    private int mMinSweepAngle;

    private int mMaxSweepAngle;

    private int mSweepSteps;

    private int mCircleSteps;

    private boolean sNeedIncrease = true;

    private Paint mPaint;

    private RectF mCanvasRect;

    private int mStartAngle;

    private int mSweepAngle = DEFAULT_MIN_SWEEP_ANGLE;

    private Runnable mTrigger = new Runnable() {
        @Override
        public void run() {
            //
            if(mStartAngle == 360) mStartAngle = 0;
            mStartAngle+=mCircleSteps;
            // modify sweep
            if(mSweepAngle == mMaxSweepAngle){
                sNeedIncrease =false;
            }else if(mSweepAngle == mMinSweepAngle){
                sNeedIncrease = true;
            }
            mSweepAngle += sNeedIncrease? mSweepSteps: -mSweepSteps;
            invalidate();
            postDelayed(this,16);
        }
    };

    public EssayProgressCircle(Context context) {
        this(context, null);
    }

    public EssayProgressCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EssayProgressCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
        initPaint();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, cn.shawn.essayloading.R.styleable.EssayProgressCircle);
        mMaxSweepAngle = array.getInt(cn.shawn.essayloading.R.styleable.EssayProgressCircle_maxSweepAngle,DEFAULT_MAX_SWEEP_ANGLE);
        mMinSweepAngle = array.getInt(cn.shawn.essayloading.R.styleable.EssayProgressCircle_minSweepAngle,DEFAULT_MIN_SWEEP_ANGLE);
        mSweepSteps = array.getInt(cn.shawn.essayloading.R.styleable.EssayProgressCircle_sweepSteps,DEFAULT_SWEEP_STEPS);
        mCircleSteps = array.getInt(cn.shawn.essayloading.R.styleable.EssayProgressCircle_circleSteps, DEFAULT_CIRCLE_STEPS);
        mPaintWidth = array.getDimensionPixelSize(R.styleable.EssayProgressCircle_paintWidth,dp2px(DEFAULT_PAINT_WIDTH));
        mColor = array.getColor(R.styleable.EssayProgressCircle_progressColor,Color.BLUE);
        array.recycle();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCanvasRect = new RectF(mPaintWidth/2,mPaintWidth/2,
                getMeasuredWidth()-mPaintWidth/2,getMeasuredHeight()-mPaintWidth/2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTrigger.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mCanvasRect,mStartAngle, -mSweepAngle,false,mPaint);
    }

    private int dp2px(float value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,
                getContext().getResources().getDisplayMetrics());
    }

}
