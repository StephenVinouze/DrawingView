package com.stephenvinouze.drawingviewsample;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.stephenvinouze.drawingview.DrawingView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, ColorPicker.OnColorChangedListener {

    private DrawingView mDrawingView;
    private ImageView mDrawingImageView;
    private Button mClearButton;
    private Button mSubmitButton;
    private Button mColorButton;
    private TextView mThicknessText;
    private SeekBar mThicknessBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mDrawingView = (DrawingView) findViewById(R.id.drawing_view);
        mDrawingImageView = (ImageView) findViewById(R.id.drawing_image_view);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mColorButton = (Button) findViewById(R.id.color_button);

        mThicknessText = (TextView) findViewById(R.id.thickness_value);

        mThicknessBar = (SeekBar) findViewById(R.id.thickness_bar);
        mThicknessBar.setMax(50);

        mClearButton.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);
        mColorButton.setOnClickListener(this);
        mThicknessBar.setOnSeekBarChangeListener(this);

        updateThickness((int) mDrawingView.getThickness());
    }

    private void updateThickness(int value) {
        mThicknessText.setText("Thickness : " + value);
        mThicknessBar.setProgress(value);
    }

    @Override
    public void onClick(View v) {
        if (v == mClearButton) {
            mDrawingView.resetDrawing();

            if (mDrawingImageView.getVisibility() == View.VISIBLE) {
                mSubmitButton.setVisibility(View.VISIBLE);
                mDrawingImageView.setVisibility(View.GONE);
                mDrawingImageView.setImageBitmap(null);
                mThicknessText.setVisibility(View.VISIBLE);
                mThicknessBar.setVisibility(View.VISIBLE);
            }
        }
        else if (v == mSubmitButton) {
            mSubmitButton.setVisibility(View.GONE);
            mDrawingImageView.setVisibility(View.VISIBLE);
            mDrawingImageView.setImageBitmap(mDrawingView.getDrawing());
            mThicknessText.setVisibility(View.GONE);
            mThicknessBar.setVisibility(View.GONE);
        }
        else if (v == mColorButton) {
            ColorPicker colorPicker = new ColorPicker(this);
            colorPicker.setOldCenterColor(mDrawingView.getDrawingColor());
            colorPicker.setOnColorChangedListener(this);

            new AlertDialog.Builder(this).setView(colorPicker).show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateThickness(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onColorChanged(int color) {
        mDrawingView.setDrawingColor(color);
    }
}
