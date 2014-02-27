package dk.redweb.hca2014;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/14/14
 * Time: 15:41
 */
public class CustomShapeDrawable extends ShapeDrawable {
    private final Paint fillpaint, strokepaint;
    private float strokeWidth;

    public CustomShapeDrawable(Shape s, int fill, int stroke, float strokeWidth){
        super(s);
        fillpaint = new Paint(this.getPaint());
        fillpaint.setColor(fill);
        strokepaint = new Paint(fillpaint);
        strokepaint.setStyle(Paint.Style.STROKE);
        strokepaint.setStrokeWidth(strokeWidth);
        strokepaint.setColor(stroke);
        this.strokeWidth = strokeWidth;
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint){
        shape.resize(canvas.getClipBounds().right,
                canvas.getClipBounds().bottom);
        shape.draw(canvas, fillpaint);

        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, canvas.getClipBounds().right,
                canvas.getClipBounds().bottom),
                new RectF(strokeWidth/2, strokeWidth/2, canvas.getClipBounds().right - strokeWidth/2,
                        canvas.getClipBounds().bottom - strokeWidth/2),
                Matrix.ScaleToFit.FILL);
        canvas.concat(matrix);

        shape.draw(canvas, strokepaint);
    }
}
