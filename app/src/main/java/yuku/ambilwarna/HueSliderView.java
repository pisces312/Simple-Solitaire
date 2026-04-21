package yuku.ambilwarna;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

public class HueSliderView extends View {

    public interface OnHueChangedListener {
        void onHueChanged(float hue);
    }

    private float[] hsv;
    private Paint barPaint;
    private Paint indicatorPaint;
    private Paint indicatorOutlinePaint;
    private OnHueChangedListener listener;

    public HueSliderView(Context context, float[] hsv, OnHueChangedListener listener) {
        super(context);
        this.hsv = hsv;
        this.listener = listener;
        init();
    }

    private void init() {
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setColor(Color.WHITE);

        indicatorOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorOutlinePaint.setStyle(Paint.Style.STROKE);
        indicatorOutlinePaint.setStrokeWidth(2f);
        indicatorOutlinePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        // Draw hue bar
        int[] colors = new int[7];
        float[] positions = new float[7];
        for (int i = 0; i < 7; i++) {
            colors[i] = Color.HSVToColor(new float[]{i * 60f, 1f, 1f});
            positions[i] = (float) i / 6f;
        }
        LinearGradient gradient = new LinearGradient(0, h / 2, w, h / 2, colors, positions, Shader.TileMode.CLAMP);
        barPaint.setShader(gradient);
        canvas.drawRect(new RectF(0, 0, w, h), barPaint);

        // Draw indicator
        float indicatorX = (hsv[0] / 360f) * w;
        float radius = h / 2f;
        canvas.drawCircle(indicatorX, h / 2f, radius - 2, indicatorOutlinePaint);
        canvas.drawCircle(indicatorX, h / 2f, radius - 4, indicatorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = Math.max(0, Math.min(event.getX(), getWidth()));
        float hue = (x / getWidth()) * 360f;
        hsv[0] = hue;
        invalidate();

        if (listener != null) {
            listener.onHueChanged(hue);
        }

        return true;
    }
}
