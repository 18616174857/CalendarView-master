package com.haibin.calendarviewproject.custom;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.haibin.calendarviewproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示一个变态需求的月视图
 * Created by huanghaibin on 2018/2/9.
 */

public class CustomMonthView extends MonthView {

    private int mRadius;

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();


    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();

    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;
    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();

    private float mSchemeBaseLine;


    private int colorSolar = Color.parseColor("#333333");//阳历的日期颜色
    private int colorLunar = Color.parseColor("#666666");//阴历的日期颜色
    private int colorHoliday = Color.parseColor("#FF4A4A");//节假日的颜色
    private int teamColor = Color.parseColor("#2D8746");//24节气的颜色
    private int nextMonthColor = Color.parseColor("#999999");//不属于本月的文字颜色

    /**
     * 选中的背景边框
     */
    private Paint mSlectBG = new Paint();
    public CustomMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(colorSolar);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);


        mSolarTermTextPaint.setColor(teamColor);
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);


        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.STROKE);
        mCurrentDayPaint.setStrokeWidth(2);
        mCurrentDayPaint.setColor(colorHoliday);


        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.DKGRAY);



        mSlectBG = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSlectBG.setStyle(Paint.Style.STROKE);
        mSlectBG.setStrokeWidth(2);
        mSlectBG.setColor(colorHoliday);


        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

    }

    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;

       // canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);源码
        //自定义的圆角边框
        RectF rectF = new RectF(x + mPadding, y + mPadding, x + mItemWidth - mPadding,  y + mItemHeight - mPadding);
        canvas.drawRoundRect(rectF, 10, 10, mSlectBG);

        return true;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

        //小圆点
        boolean isSelected = isSelected(calendar);
        if (isSelected) {
            mPointPaint.setColor(Color.GRAY);
        } else {
            mPointPaint.setColor(Color.GRAY);
        }

        canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        int top = y - mItemHeight / 6;

        if (calendar.isCurrentDay() && !isSelected) {
            //是否是今天 ,源码
           //  canvas.drawCircle(cx, cy, mRadius, mCurrentDayPaint);
            //自定义的圆角边框
            RectF rectF = new RectF(x + mPadding, y + mPadding, x + mItemWidth - mPadding,  y + mItemHeight - mPadding);
            canvas.drawRoundRect(rectF, 10, 10, mSlectBG);
        }

        if (hasScheme) {
            //校历就设置。日程不设置
            //自定义的圆角边框
            RectF rectF = new RectF(x + mPadding, y + mPadding, x + mItemWidth - mPadding,  y + mItemHeight - mPadding);
            canvas.drawRoundRect(rectF, 10, 10, mSlectBG);
            mTextPaint.setColor(calendar.getSchemeColor());
          //  canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
        }

        //当然可以换成其它对应的画笔就不麻烦，

        if (calendar.isWeekend() && calendar.isCurrentMonth()) {
            mCurMonthTextPaint.setColor(colorHoliday);
            //传统农历节日
            if(calendar.getTraditionFestival().equals("除夕") ||calendar.getTraditionFestival().equals("春节")
                    ||calendar.getTraditionFestival().equals("端午")){
                mCurMonthLunarTextPaint.setColor(colorHoliday);
            }else {
                mCurMonthLunarTextPaint.setColor(colorLunar);
            }
            //公历节日
            if(calendar.getGregorianFestival().equals("元旦") ||calendar.getGregorianFestival().equals("劳动节")
                    ||calendar.getGregorianFestival().equals("国庆节")||calendar.getGregorianFestival().equals("圣诞节")){
                mCurMonthLunarTextPaint.setColor(colorHoliday);
            }else {
                mCurMonthLunarTextPaint.setColor(colorLunar);
            }
            mSchemeTextPaint.setColor(colorHoliday);
            mSchemeLunarTextPaint.setColor(colorLunar);
            mOtherMonthLunarTextPaint.setColor(nextMonthColor);
            mOtherMonthTextPaint.setColor(nextMonthColor);
        } else  if(calendar.isCurrentMonth()){
            mCurMonthTextPaint.setColor(colorSolar);
            //传统农历节日
            if(calendar.getTraditionFestival().equals("除夕") ||calendar.getTraditionFestival().equals("春节")
                    ||calendar.getTraditionFestival().equals("端午")){
                mCurMonthLunarTextPaint.setColor(colorHoliday);
            }else {
                mCurMonthLunarTextPaint.setColor(colorLunar);
            }
            //公历节日
            if(calendar.getGregorianFestival().equals("元旦") ||calendar.getGregorianFestival().equals("劳动节")
                    ||calendar.getGregorianFestival().equals("国庆节")){
                mCurMonthLunarTextPaint.setColor(colorHoliday);
            }else {
                mCurMonthLunarTextPaint.setColor(colorLunar);
            }
            mSchemeTextPaint.setColor(colorSolar);
            mSchemeLunarTextPaint.setColor(colorLunar);
            mOtherMonthTextPaint.setColor(nextMonthColor);
            mOtherMonthLunarTextPaint.setColor(nextMonthColor);
        }








        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mCurMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {

            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint : mSchemeLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                            calendar.isCurrentMonth() ? !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint  :
                                    mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



}
