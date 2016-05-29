package dong.lan.xprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import dong.lan.xprogressbar.R;
import dong.lan.xprogressbar.ScreenUtils;

/**
 * 项目：  XProgressBar
 * 作者：  梁桂栋
 * 日期：  5/29/2016  19:06.
 * Email: 760625325@qq.com
 */
public class CircleProgressBar extends ProgressBar {

    /*
        default attrs
     */
    private static final int DEFAULT_REACHED_COLOR = 0xFFFF4081;
    private static final int DEFAULT_UNREACHED_COLOR = 0x55FF4081;
    private static final int DEFAULT_RADIUS = 50;
    private static final int DEFAULT_TEXT_SIZE = 20;
    private static final int DEFAULT_TEXT_COLOR = 0xFF000000;
    private static final int DEFAULT_CIRCLE_WIDTH = 6;

    private int reachedColor = DEFAULT_REACHED_COLOR;
    private int unreachedColor = DEFAULT_UNREACHED_COLOR;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int radius;
    private int textSize;
    private int circleWidth;

    private Paint paint = new Paint();
    private RectF arc = null;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // init default attrs
        textSize = ScreenUtils.sp2px(DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics());
        radius = ScreenUtils.dp2px(DEFAULT_RADIUS, getResources().getDisplayMetrics());
        circleWidth = ScreenUtils.dp2px(DEFAULT_CIRCLE_WIDTH, getResources().getDisplayMetrics());

        // obtainStyledAttrs
        obtainStyledAttrs(attrs);
        paint.setAntiAlias(true);
        //before measure the text width,you need to set the textSize first
        paint.setTextSize(textSize);

    }

    /*
        get styledAttrs from xml
     */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        reachedColor = typedArray.getColor(R.styleable.CircleProgressBar_circle_progress_reached_color, reachedColor);
        unreachedColor = typedArray.getColor(R.styleable.CircleProgressBar_circle_progress_unreached_color, unreachedColor);
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_circle_progress_text_color, textColor);
        textSize = (int) typedArray.getDimension(R.styleable.CircleProgressBar_circle_progress_text_size, textSize);
        radius = (int) typedArray.getDimension(R.styleable.CircleProgressBar_circle_progress_radius, radius);
        circleWidth = (int) typedArray.getDimension(R.styleable.CircleProgressBar_circle_progress_circle_width, circleWidth);
        typedArray.recycle();
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int padding = Math.max((getPaddingBottom() + getPaddingTop()), (getPaddingLeft() + getPaddingRight()));
        int realWidth = circleWidth + radius * 2 + padding;

        // you can see the resolveSize() method in the android source code
        int width = resolveSize(realWidth, widthMeasureSpec);
        int height = resolveSize(realWidth, heightMeasureSpec);

        realWidth = Math.min(width, height);

        radius = Math.min((realWidth - padding - circleWidth) / 2, radius);

        setMeasuredDimension(realWidth, realWidth);

        if (arc == null)
            arc = new RectF(0, 0, radius * 2, radius * 2);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {

        String text = getProgress() + " %";
        float textWidth = paint.measureText(text);
        float textHeight = (paint.descent() + paint.ascent()) / 2;

        canvas.save();
        //get translate values,in order to place the circleBar in center
        float trl = (getWidth() - getPaddingLeft() - radius*2)/2;
        canvas.translate(trl, trl);

        //draw the unreached bar
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(unreachedColor);
        paint.setStrokeWidth(circleWidth / 3);
        canvas.drawCircle(radius, radius, radius, paint);

        //draw the reached bar
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        paint.setColor(reachedColor);
        paint.setStrokeWidth(circleWidth);
        canvas.drawArc(arc, 0, sweepAngle, false, paint);

        //draw text
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        //make the text in the center of circle
        canvas.drawText(text, radius - textWidth / 2 , radius - textHeight, paint);
        canvas.restore();
    }
}
