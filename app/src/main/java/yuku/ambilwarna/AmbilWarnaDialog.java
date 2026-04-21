/*
 * AmbilWarnaDialog - inline replacement for the deprecated yuku/ambilwarna library.
 * Simple HSV color picker dialog.
 * Compatible API: AmbilWarnaDialog(Context, int, OnAmbilWarnaListener)
 */

package yuku.ambilwarna;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

public class AmbilWarnaDialog {

    public interface OnAmbilWarnaListener {
        void onOk(AmbilWarnaDialog dialog, int color);
        void onCancel(AmbilWarnaDialog dialog);
    }

    private final AlertDialog dialog;
    private final OnAmbilWarnaListener listener;
    private int currentColor;
    private final ImageView colorPreview;
    private final SatValView satValView;
    private final HueSliderView hueSliderView;

    public AmbilWarnaDialog(Context context, int initialColor, final OnAmbilWarnaListener listener) {
        this.listener = listener;
        this.currentColor = initialColor;

        float[] hsv = new float[3];
        Color.colorToHSV(initialColor, hsv);

        // Create layout
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(32, 32, 32, 16);

        // Saturation/Value picker
        satValView = new SatValView(context, hsv);
        int svSize = (int) (280 * context.getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams svParams = new LinearLayout.LayoutParams(svSize, svSize);
        satValView.setLayoutParams(svParams);
        mainLayout.addView(satValView);

        // Hue slider
        hueSliderView = new HueSliderView(context, hsv, new HueSliderView.OnHueChangedListener() {
            @Override
            public void onHueChanged(float hue) {
                satValView.setHue(hue);
                updateColor();
            }
        });
        int hueHeight = (int) (32 * context.getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams hueParams = new LinearLayout.LayoutParams(svSize, hueHeight);
        hueParams.topMargin = 16;
        hueSliderView.setLayoutParams(hueParams);
        mainLayout.addView(hueSliderView);

        // Color preview
        colorPreview = new ImageView(context);
        int previewSize = (int) (48 * context.getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams previewParams = new FrameLayout.LayoutParams(previewSize, previewSize);
        colorPreview.setLayoutParams(previewParams);
        colorPreview.setBackgroundColor(initialColor);
        LinearLayout previewRow = new LinearLayout(context);
        previewRow.setOrientation(LinearLayout.HORIZONTAL);
        previewRow.setGravity(Gravity.END);
        previewRow.setPadding(0, 16, 0, 0);
        previewRow.addView(colorPreview);
        mainLayout.addView(previewRow);

        satValView.setOnColorChangedListener(new SatValView.OnColorChangedListener() {
            @Override
            public void onColorChanged() {
                updateColor();
            }
        });

        dialog = new AlertDialog.Builder(context)
                .setTitle("Pick a color")
                .setView(mainLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (AmbilWarnaDialog.this.listener != null) {
                            AmbilWarnaDialog.this.listener.onOk(AmbilWarnaDialog.this, currentColor);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (AmbilWarnaDialog.this.listener != null) {
                            AmbilWarnaDialog.this.listener.onCancel(AmbilWarnaDialog.this);
                        }
                    }
                })
                .create();
    }

    private void updateColor() {
        float[] hsv = satValView.getHSV();
        currentColor = Color.HSVToColor(hsv);
        colorPreview.setBackgroundColor(currentColor);
    }

    public void show() {
        dialog.show();
    }
}
