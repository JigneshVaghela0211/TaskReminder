package com.starter.app.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * A custom circular progress view that displays a progress arc with text
 */
public class ProgressCircleView extends View {

    // Progress percentage (0-100)
    private int progressPercent = 0;

    // Paint objects for different elements
    private Paint arcPaint;
    private Paint backgroundPaint;
    private Paint circlePaint;
    private Paint textPaint;
    private Paint subtitlePaint;

    // Bounds and rectangles
    private Rect textBounds;
    private Rect subtitleBounds;
    private RectF arcRect;

    // Size and text properties
    private int fixedSize = 0;
    private String mainText = "";
    private String subtitleText = null;

    // Style properties
    private boolean showShadow = false;
    private boolean isClockwise = false;
    private Typeface customTypeface;

    // Font scaling factors
    private float fontScaleFactor = 1.0f;
    private float subtitleOffsetFactor = 0.6f;

    public ProgressCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    /**
     * Initialize all paint objects and default values
     */
    private void initialize() {
        // Initialize paint objects with anti-aliasing
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        subtitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Set default colors
        arcPaint.setColor(ViewCompat.MEASURED_STATE_MASK); // Black
        backgroundPaint.setColor(Color.TRANSPARENT);
        circlePaint.setColor(0xFF88B8C8); // Light blue-gray
        textPaint.setColor(Color.WHITE);
        subtitlePaint.setColor(Color.WHITE);

        // Set paint styles
        arcPaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStyle(Paint.Style.STROKE);

        // Enable hardware acceleration
        setLayerType(LAYER_TYPE_HARDWARE, null);

        // Set text alignment
        textPaint.setTextAlign(Paint.Align.CENTER);
        subtitlePaint.setTextAlign(Paint.Align.CENTER);

        // Initialize rectangles
        textBounds = new Rect();
        subtitleBounds = new Rect();
        arcRect = new RectF();

        // Set default typeface
        customTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Determine the drawing size
        int width = getWidth();
        int height = getHeight();

        if (fixedSize != 0) {
            width = fixedSize;
            height = fixedSize;
        }

        int minDimension = Math.min(width, height);

        // Calculate stroke widths
        float arcStrokeWidth = (float) (minDimension * 0.06);
        arcPaint.setStrokeWidth(arcStrokeWidth);

        float circleStrokeWidth = Math.max(2.0f, Math.min(10.0f, arcStrokeWidth * 0.25f));
        circlePaint.setStrokeWidth(circleStrokeWidth);

        // Apply shadow effects if enabled
        applyShadowEffects(showShadow, circleStrokeWidth);

        // Calculate center and radius
        float centerX = width / 2.0f;
        float centerY = height / 2.0f;
        float radius = (minDimension / 2.0f) - arcStrokeWidth;

        // Set up the arc bounds
        arcRect.set(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);

        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

        // Draw circle outline
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Draw progress arc
        float sweepAngle = (progressPercent * 360f * 0.01f) * (isClockwise ? -1 : 1);
        canvas.drawArc(arcRect, 270.0f, sweepAngle, false, arcPaint);

        // Draw text
        drawText(canvas, centerX, centerY, radius);
    }

    /**
     * Draw the main text and subtitle
     */
    private void drawText(Canvas canvas, float centerX, float centerY, float radius) {
        // Ensure typeface is set
        if (customTypeface == null) {
            customTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        }

        textPaint.setTypeface(customTypeface);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Calculate maximum text width (70% of diameter)
        float maxTextWidth = radius * 2.0f * 0.7f;

        // Set initial text size
        textPaint.setTextSize(0.85f * radius * fontScaleFactor);

        // Adjust text size to fit
        float textWidth = textPaint.measureText(mainText);
        while (textWidth > maxTextWidth && textPaint.getTextSize() > 0.0f) {
            textPaint.setTextSize((textPaint.getTextSize() * maxTextWidth) / textWidth);
            textWidth = textPaint.measureText(mainText);
        }

        // Get text bounds
        textPaint.getTextBounds(mainText, 0, mainText.length(), textBounds);

        // Handle subtitle
        int subtitleHeight = 0;
        if (!TextUtils.isEmpty(subtitleText)) {
            subtitlePaint.setTextSize(radius * 0.25f * fontScaleFactor);
            subtitlePaint.setTypeface(customTypeface);
            subtitlePaint.getTextBounds(subtitleText, 0, subtitleText.length(), subtitleBounds);
            subtitleHeight = subtitleBounds.bottom - subtitleBounds.top;
        }

        // Calculate vertical position for main text
        float textY = (canvas.getHeight() / 2.0f) +
                ((textPaint.descent() - textPaint.ascent()) / 2.0f) -
                textPaint.descent();

        // Draw main text
        canvas.drawText(mainText, canvas.getWidth() / 2.0f,
                textY - (subtitleHeight / 2.0f), textPaint);

        // Draw subtitle if present
        if (!TextUtils.isEmpty(subtitleText)) {
            int mainTextHeight = textBounds.bottom - textBounds.top;
            float subtitleY = centerY +
                    (mainTextHeight / 2.0f) +
                    (subtitleBounds.height() / 2.0f) +
                    (subtitleHeight * subtitleOffsetFactor);
            canvas.drawText(subtitleText, centerX, subtitleY, subtitlePaint);
        }
    }

    /**
     * Apply or remove shadow effects on all paint objects
     */
    private void applyShadowEffects(boolean enable, float strokeWidth) {
        float shadowRadius = enable ? strokeWidth : 0.0f;
        int shadowColor = enable ? Color.argb(160, 0, 0, 0) : 0;

        textPaint.setShadowLayer(shadowRadius, shadowRadius, shadowRadius, shadowColor);
        arcPaint.setShadowLayer(shadowRadius, shadowRadius, shadowRadius, shadowColor);
        circlePaint.setShadowLayer(shadowRadius, shadowRadius, shadowRadius, shadowColor);
        subtitlePaint.setShadowLayer(shadowRadius, shadowRadius, shadowRadius, shadowColor);
    }

    // Getters
    public int getPercent() {
        return progressPercent;
    }

    public String getText() {
        return mainText;
    }

    public String getTextSubtitle() {
        return subtitleText;
    }

    // Setters
    public void setPercent(int percent) {
        this.progressPercent = Math.max(0, Math.min(100, percent));
        invalidate();
    }

    public void setText(String text) {
        this.mainText = text != null ? text : "";
        invalidate();
    }

    public void setTextSubtitle(String subtitle) {
        this.subtitleText = subtitle;
        invalidate();
    }

    public void setColorArc(int color) {
        arcPaint.setColor(color);
        invalidate();
    }

    public void setColorBackground(int color) {
        backgroundPaint.setColor(color);
        invalidate();
    }

    public void setColorCircle(int color) {
        circlePaint.setColor(color);
        invalidate();
    }

    public void setColorFont(int color) {
        textPaint.setColor(color);
        subtitlePaint.setColor(color);
        invalidate();
    }

    public void setShowShadow(boolean show) {
        this.showShadow = show;
        invalidate();
    }

    public void setClockwise(boolean clockwise) {
        this.isClockwise = clockwise;
        invalidate();
    }

    public void setFont(String fontPath) {
        // Note: This assumes Tools.f27667a, Tools.c, and Tools.d arrays exist
        // If you don't have these, you can remove this method or implement custom logic

        if (TextUtils.isEmpty(fontPath)) {
            customTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            fontScaleFactor = 1.0f;
            subtitleOffsetFactor = 0.6f;
        } else {
            try {
                customTypeface = Typeface.createFromAsset(getContext().getAssets(), fontPath);
                // You can set custom scale factors here if needed
                fontScaleFactor = 1.0f;
                subtitleOffsetFactor = 0.6f;
            } catch (Exception e) {
                customTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                fontScaleFactor = 1.0f;
                subtitleOffsetFactor = 0.6f;
            }
        }
        invalidate();
    }

    public void setFixedSize(int size) {
        this.fixedSize = size;
        invalidate();
        requestLayout();
    }
}