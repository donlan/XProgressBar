package dong.lan.xprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import dong.lan.xprogressbar.R;
import dong.lan.xprogressbar.ScreenUtils;

/**
 * 项目：  XProgressBar
 * 作者：  梁桂栋
 * 日期：  5/29/2016  13:17.
 * Email: 760625325@qq.com
 */
public class TextProgressBar extends ProgressBar {

    /*
      declare the default attribute
     */
    private static final int DEFAULT_LEFT_COLOR = 0XFFFF0000;
    private static final int DEFAULT_RIGHT_COLOR = 0XFF00FF00;
    private static final int DEFAULT_LEFT_HEIGHT = 2;
    private static final int DEFAULT_RIGHT_HEIGHT = 1;
    private static final int DEFAULT_TEXT_COLOR = 0XFFFFFF00;
    private static final int DEFAULT_TEXT_SIZE = 6;
    private static final int DEFAULT_TEXT_OFFSET = 10;
    private static final int DEFAULT_CIRCLE_RADIUS = 8;
    private static final int DEFAULT_CIRCLE_COLOR = 0XFFFF1122;

    private int textSize;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textOffset;
    private int leftHeight;
    private int leftColor = DEFAULT_LEFT_COLOR;
    private int rightColor = DEFAULT_RIGHT_COLOR;
    private int rightHeight;
    private int circleRadius;
    private int circleColor = DEFAULT_CIRCLE_COLOR;


    private Paint paint = new Paint();
    private int realWidth;  // the real width than we can use to draw

    public TextProgressBar(Context context) {
        this(context, null);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /*
         initial attribute by device display metrics
         */
        textSize = ScreenUtils.sp2px(DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics());
        textOffset = ScreenUtils.dp2px(DEFAULT_TEXT_OFFSET, getResources().getDisplayMetrics());
        leftHeight = ScreenUtils.dp2px(DEFAULT_LEFT_HEIGHT, getResources().getDisplayMetrics());
        rightHeight = ScreenUtils.dp2px(DEFAULT_RIGHT_HEIGHT, getResources().getDisplayMetrics());
        circleRadius = ScreenUtils.dp2px(DEFAULT_CIRCLE_RADIUS, getResources().getDisplayMetrics());
        obtainStyledAttrs(attrs);
        //before measure the text width,you need to set the textSize first
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
    }

    /*
       get styledAttrs from xml
    */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextProgressBar);
        leftColor = typedArray.getColor(R.styleable.TextProgressBar_progress_left_color, leftColor);
        rightColor = typedArray.getColor(R.styleable.TextProgressBar_progress_right_color, rightColor);
        textColor = typedArray.getColor(R.styleable.TextProgressBar_progress_text_color, textColor);
        circleColor = typedArray.getColor(R.styleable.TextProgressBar_progress_circle_color, circleColor);
        textSize = (int) typedArray.getDimension(R.styleable.TextProgressBar_progress_text_size, textSize);
        textOffset = (int) typedArray.getDimension(R.styleable.TextProgressBar_progress_text_offset, textOffset);
        leftHeight = (int) typedArray.getDimension(R.styleable.TextProgressBar_progress_left_height, leftHeight);
        rightHeight = (int) typedArray.getDimension(R.styleable.TextProgressBar_progress_right_height, rightHeight);
        circleRadius = (int) typedArray.getDimension(R.styleable.TextProgressBar_progress_circle_radius, circleRadius);
        typedArray.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthSize, height);
        realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);


        boolean isNeedRightBar = true;
        float radio = (getProgress() * 1.0f / getMax());
        String text = String.valueOf(radio);
        int textWidth = (int) paint.measureText(text);
        float progressX = radio * realWidth;  //current progress x coordinate
        if (progressX + circleRadius > realWidth) {
            isNeedRightBar = false;
            progressX = realWidth - circleRadius;

        }
        if (progressX > 0) {
            paint.setColor(leftColor);
            paint.setStrokeWidth(leftHeight);
            canvas.drawLine(0, 0, progressX, 0, paint);
        }

        /*
        if the progressbar got max progress values ,we don't need to draw the right bar
         */
        if (isNeedRightBar) {
            paint.setColor(rightColor);
            paint.setStrokeWidth(rightHeight);
            canvas.drawLine(progressX, 0, realWidth, 0, paint);
        }

        int y = (int) ((paint.descent() + paint.ascent()) / 2);
        paint.setColor(circleColor);
        float textStartX;
        //if progress values less than circleRadius,we don't need to move the circle
        if (progressX < circleRadius) {
            canvas.drawCircle(circleRadius, 0, circleRadius, paint);
            textStartX = circleRadius - textWidth / 2;
        }
        //if progress values great than circleRadius,we don't need to move the circle
        else if (progressX + circleRadius >= realWidth) {
            canvas.drawCircle(realWidth - circleRadius, 0, circleRadius, paint);
            textStartX = realWidth - circleRadius - textWidth / 2;
        }
        //others ,move the circle by progress values
        else {
            canvas.drawCircle(progressX, 0, circleRadius, paint);
            textStartX = progressX - textWidth / 2;
        }
        //draw text in the hint-circle
        paint.setColor(textColor);
        canvas.drawText(text, textStartX, -y, paint);
        canvas.restore();
    }

    /*
    measure the height than we can draw
     */
    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int result;
        int l = circleRadius * 2; //the progress hint-circle's perimeter
        if (mode == MeasureSpec.EXACTLY) {
            if (height < l)
                result = l;
            else
                result = height;
        } else {
            int textHeight = (int) (paint.descent() - paint.ascent());
            result = getPaddingBottom() + getPaddingTop()
                    + Math.max(l, Math.max(Math.max(leftHeight, rightHeight), textHeight));

            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, height);
            }
        }
        return result;
    }
}
