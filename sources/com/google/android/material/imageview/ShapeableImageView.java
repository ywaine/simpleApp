package com.google.android.material.imageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.C0077R;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import com.google.android.material.shape.Shapeable;

public class ShapeableImageView extends AppCompatImageView implements Shapeable {
    private static final int DEF_STYLE_RES = C0077R.style.Widget_MaterialComponents_ShapeableImageView;
    private final Paint borderPaint;
    private final Paint clearPaint;
    /* access modifiers changed from: private */
    public final RectF destination;
    private Path maskPath;
    private final RectF maskRect;
    private final Path path;
    private final ShapeAppearancePathProvider pathProvider;
    /* access modifiers changed from: private */
    public final MaterialShapeDrawable shadowDrawable;
    /* access modifiers changed from: private */
    public ShapeAppearanceModel shapeAppearanceModel;
    private ColorStateList strokeColor;
    private float strokeWidth;

    public ShapeableImageView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public ShapeableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ShapeableImageView(android.content.Context r6, android.util.AttributeSet r7, int r8) {
        /*
            r5 = this;
            int r0 = DEF_STYLE_RES
            android.content.Context r1 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r6, r7, r8, r0)
            r5.<init>(r1, r7, r8)
            com.google.android.material.shape.ShapeAppearancePathProvider r1 = new com.google.android.material.shape.ShapeAppearancePathProvider
            r1.<init>()
            r5.pathProvider = r1
            android.graphics.Path r1 = new android.graphics.Path
            r1.<init>()
            r5.path = r1
            android.content.Context r6 = r5.getContext()
            android.graphics.Paint r1 = new android.graphics.Paint
            r1.<init>()
            r5.clearPaint = r1
            r2 = 1
            r1.setAntiAlias(r2)
            r3 = -1
            r1.setColor(r3)
            android.graphics.PorterDuffXfermode r3 = new android.graphics.PorterDuffXfermode
            android.graphics.PorterDuff$Mode r4 = android.graphics.PorterDuff.Mode.DST_OUT
            r3.<init>(r4)
            r1.setXfermode(r3)
            android.graphics.RectF r1 = new android.graphics.RectF
            r1.<init>()
            r5.destination = r1
            android.graphics.RectF r1 = new android.graphics.RectF
            r1.<init>()
            r5.maskRect = r1
            android.graphics.Path r1 = new android.graphics.Path
            r1.<init>()
            r5.maskPath = r1
            int[] r1 = com.google.android.material.C0077R.styleable.ShapeableImageView
            android.content.res.TypedArray r1 = r6.obtainStyledAttributes(r7, r1, r8, r0)
            int r3 = com.google.android.material.C0077R.styleable.ShapeableImageView_strokeColor
            android.content.res.ColorStateList r3 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r6, (android.content.res.TypedArray) r1, (int) r3)
            r5.strokeColor = r3
            int r3 = com.google.android.material.C0077R.styleable.ShapeableImageView_strokeWidth
            r4 = 0
            int r3 = r1.getDimensionPixelSize(r3, r4)
            float r3 = (float) r3
            r5.strokeWidth = r3
            android.graphics.Paint r3 = new android.graphics.Paint
            r3.<init>()
            r5.borderPaint = r3
            android.graphics.Paint$Style r4 = android.graphics.Paint.Style.STROKE
            r3.setStyle(r4)
            r3.setAntiAlias(r2)
            com.google.android.material.shape.ShapeAppearanceModel$Builder r0 = com.google.android.material.shape.ShapeAppearanceModel.builder((android.content.Context) r6, (android.util.AttributeSet) r7, (int) r8, (int) r0)
            com.google.android.material.shape.ShapeAppearanceModel r0 = r0.build()
            r5.shapeAppearanceModel = r0
            com.google.android.material.shape.MaterialShapeDrawable r0 = new com.google.android.material.shape.MaterialShapeDrawable
            com.google.android.material.shape.ShapeAppearanceModel r2 = r5.shapeAppearanceModel
            r0.<init>((com.google.android.material.shape.ShapeAppearanceModel) r2)
            r5.shadowDrawable = r0
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 21
            if (r0 < r2) goto L_0x0092
            com.google.android.material.imageview.ShapeableImageView$OutlineProvider r0 = new com.google.android.material.imageview.ShapeableImageView$OutlineProvider
            r0.<init>()
            r5.setOutlineProvider(r0)
        L_0x0092:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.imageview.ShapeableImageView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        setLayerType(0, (Paint) null);
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLayerType(2, (Paint) null);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.maskPath, this.clearPaint);
        drawStroke(canvas);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        updateShapeMask(width, height);
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel2) {
        this.shapeAppearanceModel = shapeAppearanceModel2;
        this.shadowDrawable.setShapeAppearanceModel(shapeAppearanceModel2);
        updateShapeMask(getWidth(), getHeight());
        invalidate();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.shapeAppearanceModel;
    }

    private void updateShapeMask(int width, int height) {
        this.destination.set((float) getPaddingLeft(), (float) getPaddingTop(), (float) (width - getPaddingRight()), (float) (height - getPaddingBottom()));
        this.pathProvider.calculatePath(this.shapeAppearanceModel, 1.0f, this.destination, this.path);
        this.maskPath.rewind();
        this.maskPath.addPath(this.path);
        this.maskRect.set(0.0f, 0.0f, (float) width, (float) height);
        this.maskPath.addRect(this.maskRect, Path.Direction.CCW);
    }

    private void drawStroke(Canvas canvas) {
        if (this.strokeColor != null) {
            this.borderPaint.setStrokeWidth(this.strokeWidth);
            int colorForState = this.strokeColor.getColorForState(getDrawableState(), this.strokeColor.getDefaultColor());
            if (this.strokeWidth > 0.0f && colorForState != 0) {
                this.borderPaint.setColor(colorForState);
                canvas.drawPath(this.path, this.borderPaint);
            }
        }
    }

    public void setStrokeColorResource(int strokeColorResourceId) {
        setStrokeColor(AppCompatResources.getColorStateList(getContext(), strokeColorResourceId));
    }

    public ColorStateList getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeWidth(float strokeWidth2) {
        if (this.strokeWidth != strokeWidth2) {
            this.strokeWidth = strokeWidth2;
            invalidate();
        }
    }

    public void setStrokeWidthResource(int strokeWidthResourceId) {
        setStrokeWidth((float) getResources().getDimensionPixelSize(strokeWidthResourceId));
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeColor(ColorStateList strokeColor2) {
        this.strokeColor = strokeColor2;
        invalidate();
    }

    class OutlineProvider extends ViewOutlineProvider {
        private final Rect rect = new Rect();

        OutlineProvider() {
        }

        public void getOutline(View view, Outline outline) {
            if (ShapeableImageView.this.shapeAppearanceModel != null) {
                ShapeableImageView.this.destination.round(this.rect);
                ShapeableImageView.this.shadowDrawable.setBounds(this.rect);
                ShapeableImageView.this.shadowDrawable.getOutline(outline);
            }
        }
    }
}
