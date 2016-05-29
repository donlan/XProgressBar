package dong.lan.xprogressbar;

import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 项目：  XProgressBar
 * 作者：  梁桂栋
 * 日期：  5/29/2016  13:40.
 * Email: 760625325@qq.com
 */
public class ScreenUtils {
    private ScreenUtils(){}

    public static int dp2px(int dpVal, DisplayMetrics metrics){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,metrics);
    }

    public static int sp2px(int dpVal, DisplayMetrics metrics){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,dpVal,metrics);
    }

}
