package yuku.ambilwarna;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SatValView extends View {

    public interface OnColorChangedListener {
        void onColorChanged();
    }

    private float[] hsv;
    private Bitmap bitmap;
    private Paint circlePaint;
    private Paint outlinePaint;
    private OnColorChangedListener listener;
    private float circleX = -1;
    private float circleY = -1;

    public SatValView(Context context, float[] hsv) {
        super(context);
        this.hsv = hsv.clone();
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4f);
        circlePaint.setColor(Color.WHITE);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(2f);
        outlinePaint.setColor(Color.BLACK);
    }

    public void setHue(float hue) {
        hsv[0] = hue;
        updateBitmap();
        invalidate();
    }

    public float[] getHSV() {
        return hsv.clone();
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateBitmap();
        updateCirclePosition();
    }

    private void updateBitmap() {
        if (getWidth() <= 0 || getHeight() <= 0) return;

        int w = getWidth();
        int h = getHeight();
        if (bitmap != null) bitmap.recycle();
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        float hue = hsv[0];
        int hueColor = Color.HSVToColor(new float[]{hue, 1f, 1f});

        for (int y = 0; y < h; y++) {
            float value = 1f - (float) y / h;
            float[] rowHsv = {hue, 0f, value};
            for (int x = 0; x < w; x++) {
                rowHsv[1] = (float) x / w;
                bitmap.setPixel(x, y, Color.HSVToColor(rowHsv));
            }
        }
    }

    private void updateCirclePosition() {
        if (getWidth() <= 0 || getHeight() <= 0) return;
        circleX = hsv[1] * getWidth();
        circleY = (1f - hsv[2]) * getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
        if (circleX >= 0 && circleY >= 0) {
            canvas.drawCircle(circleX, circleY, 10f, outlinePaint);
            canvas.drawCircle(circleX, circleY, 8f, circlePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = Math.max(0, Math.min(event.getX(), getWidth()));
        float y = Math.max(0, Math.min(event.getY(), getHeight()));

        circleX = x;
        circleY = y;

        hsv[1] = x / getWidth();
        hsv[2] = 1f - y / getHeight();

        invalidate();

        if (listener != null) {
            listener.onColorChanged();
        }

        return true;
    }
}
