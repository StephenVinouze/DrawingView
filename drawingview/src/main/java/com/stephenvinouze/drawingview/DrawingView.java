package com.stephenvinouze.drawingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Stephen Vinouze on 20/11/2015.
 */
public class DrawingView extends View {

    private static final int kColor = Color.BLACK;
    private static final float kTouchTolerance = 5;
    private static final float kLineThickness = 5;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private float mX;
    private float mY;
    private float mTolerance = kTouchTolerance;
    private float mThickness = kLineThickness;
    private int mColor = kColor;

    public DrawingView(Context context) {
        super(context);
        initView();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawingView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {

            int attr = a.getIndex(i);
            if (attr == R.styleable.DrawingView_drawingColor) {
                setDrawingColor(a.getColor(attr, kColor));
            }
            else if (attr == R.styleable.DrawingView_drawingThickness) {
                setDrawingThickness(a.getFloat(attr, kLineThickness));
            }
            else if (attr == R.styleable.DrawingView_drawingTolerance) {
                setDrawingTolerance(a.getFloat(attr, kTouchTolerance));
            }
        }

        a.recycle();
    }

    public int getDrawingColor() {
        return mColor;
    }

    public void setDrawingColor(int color) {
        mColor = color;
        mPaint.setColor(color);
    }

    public float getThickness() {
        return mThickness;
    }

    public void setDrawingThickness(float thickness) {
        mThickness = thickness;
        mPaint.setStrokeWidth(thickness);
    }

    public float getTolerance() {
        return mTolerance;
    }

    public void setDrawingTolerance(float tolerance) {
        mTolerance = tolerance;
    }

    /**
     * Reset the drawing view by cleaning the canvas that contains the drawing
     */
    public void resetDrawing() {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    /**
     * Let you retrieve the drawing that has been drawn inside the canvas
     * @return The drawing as a Bitmap
     */
    public Bitmap getDrawing() {
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        layout(getLeft(), getTop(), getRight(), getBottom());
        draw(c);

        return b;
    }

    private void initView() {
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(kLineThickness);
    }

    private void startTouch(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= mTolerance || dy >= mTolerance) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void endTouch() {
        if (!mPath.isEmpty()) {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
        } else {
            mCanvas.drawPoint(mX, mY, mPaint);
        }

        mPath.reset();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, (h > 0 ? h : ((View)getParent()).getHeight()), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endTouch();
                invalidate();
                break;
        }

        return true;
    }

}
