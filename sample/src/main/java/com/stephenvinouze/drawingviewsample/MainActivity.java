package com.stephenvinouze.drawingviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.stephenvinouze.drawingview.DrawingView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mClearButton;
    private Button mSubmitButton;
    private DrawingView mDrawingView;
    private ImageView mDrawingImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mDrawingView = (DrawingView) findViewById(R.id.drawing_view);
        mDrawingImageView = (ImageView) findViewById(R.id.drawing_image_view);

        mClearButton.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mClearButton) {
            mDrawingView.resetDrawing();

            if (mDrawingImageView.getVisibility() == View.VISIBLE) {
                mSubmitButton.setEnabled(true);
                mDrawingImageView.setVisibility(View.GONE);
                mDrawingImageView.setImageBitmap(null);
            }
        }
        else if (v == mSubmitButton) {
            mSubmitButton.setEnabled(false);
            mDrawingImageView.setVisibility(View.VISIBLE);
            mDrawingImageView.setImageBitmap(mDrawingView.getDrawing());
        }
    }
}
