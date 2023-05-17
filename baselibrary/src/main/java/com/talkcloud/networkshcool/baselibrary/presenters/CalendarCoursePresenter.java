package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LessonMonthEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.utils.BarUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.CalendarCourseView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/19
 * Time:16:16
 * author:joker
 * 日历展示课程页面Presenter
 */
public class CalendarCoursePresenter {

    private Activity mActivity;
    private CalendarCourseView calendarCourseView;

    public CalendarCoursePresenter(Activity activity, CalendarCourseView calendarCourseView) {
        this.mActivity = activity;
        this.calendarCourseView = calendarCourseView;
    }


    /**
     * 设置状态栏的高度
     */
    public void setBarHeight(View view) {
        //获取状态的高度
        int BarHeight = BarUtils.getStatusBarHeight(mActivity);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = BarHeight;
        view.setLayoutParams(layoutParams);
    }

    /**
     * 根据语言切换设置 日历的title显示
     */
    public void setCalendarYearAndMonthUI(int mYear, int month, View view) {
        String[] months = mActivity.getResources().getStringArray(R.array.montharray);
        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String mLanguage = "";
        if (StringUtils.isBlank(locale_language)) {
            Map<String, String> map = PublicPracticalMethodFromJAVA.getInstance().getCurrentLanguageAndCountry();
            mLanguage = map.get(SPConstants.LOCALE_LANGUAGE);
        } else {
            mLanguage = locale_language;
        }
        if ("zh".equals(mLanguage)) {//表示是中文
            ((TextView) view).setText(mYear + "." + months[month - 1]);
        } else {
            ((TextView) view).setText(months[month - 1] + " " + mYear);
        }
    }

    /**
     * 设置日历上面有课节的日子
     */
    public void setLessonDays(CalendarView calendarView, List<LessonMonthEntity> lessonMonthEntity) {
        Map<String, Calendar> map = new HashMap<>();
        //测试数据
//        map.put(getSchemeCalendar(year, month, 7, "#ff4545", "课").toString(), getSchemeCalendar(year, month, 7, "#ff4545", "课"));
//        map.put(getSchemeCalendar(year, month, 17, "#ff4545", "课").toString(), getSchemeCalendar(year, month, 7, "#ff4545", "课"));
        List<LessonMonthEntity> dataBeans = lessonMonthEntity;
        if (!StringUtils.isBlank(dataBeans) && dataBeans.size() > 0) {
            for (int i = 0; i < dataBeans.size(); i++) {
                String year_month_day = dataBeans.get(i).getDay();
                String[] days = year_month_day.split("-");
                String year = days[0];
                String month = days[1];
                String day = days[2];
                Calendar calendar = getSchemeCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), "#ff4545", "课");
                map.put(calendar.toString(), calendar);
            }
//            map.put(getSchemeCalendar(2021, 4, 25, "#ff4545", "课").toString(), getSchemeCalendar(2021, 4, 25, "#ff4545", "课"));
//            map.put(getSchemeCalendar(2021, 4, Integer.parseInt("01"), "#ff4545", "课").toString(), getSchemeCalendar(2021, 4, Integer.parseInt("01"), "#ff4545", "课"));
            //此方法在巨大的数据量上不影响遍历性能，推荐使用
            calendarView.setSchemeDate(map);
        }


    }

    /**
     * 获取课件显示的日
     */
    private Calendar getSchemeCalendar(int year, int month, int day, String color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.parseColor(color));//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);//事件名称

        return calendar;
    }


    /**
     * 我的课表（月）请求
     */
    public void getLessonMonthDatas(int year, int month) {
        String mYear_month = year + "-" + getConversionMonth(month);
        
        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.lessonMonth(mYear_month)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<List<LessonMonthEntity>>>>(mActivity, false, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<List<LessonMonthEntity>>> apiResponseResponse) {
                        if (calendarCourseView != null)
                            calendarCourseView.lessonMonthCallback(true, apiResponseResponse.body().getMsg(), apiResponseResponse.body().getData());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        if (calendarCourseView != null)
                            calendarCourseView.lessonMonthCallback(false, message, null);
                    }
                });
    }


    /**
     * 得到转换后的月的值
     * 例子：5->05
     */
    public String getConversionMonth(int month) {
        String result = "";
        if (String.valueOf(month).length() == 1) {
            result = "0" + month;
        } else {
            result = String.valueOf(month);
        }
        return result;
    }
}
