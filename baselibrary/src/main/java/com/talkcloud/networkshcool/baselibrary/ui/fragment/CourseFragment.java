package com.talkcloud.networkshcool.baselibrary.ui.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.CourseMainPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.activities.CalendarCourseActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkListActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.NoticeInAppActivity;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.CommonFragmentPagerAdapter;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.CourseMainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:wsf
 * createTime:2021/5/12
 * description: 首页课节页面
 */
public class CourseFragment extends Fragment implements CourseMainView, View.OnClickListener {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CourseMainPresenter courseMainPresenter;
    private CommonFragmentPagerAdapter adapter;
    private LinearLayout ll_course, ll_homework;
    private int positionToday = 0;
    private TextView userName;
    private TextView tv_homework_title;
    private boolean isFromLessonfrgRefresh = false; //是否 从 刷新课节进入的
    private final List<String> curDatelist = new ArrayList<String>();

    private ConstraintLayout cl_notify;
    private ImageView iv_notify;
    private TextView tv_notify_num;
    private TextView tv_homework_new_label;


    public static CourseFragment newInstance(String text) {
        CourseFragment fg = new CourseFragment();
        Bundle agrs = new Bundle();
        agrs.putString("text", text);
        fg.setArguments(agrs);
        return fg;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (getActivity() != null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.course_fragment, container, false);
            tabLayout = view.findViewById(R.id.tp_tabLayout);
            viewPager = view.findViewById(R.id.contact_viewPager);
            ll_course = view.findViewById(R.id.ll_course);
            ll_homework = view.findViewById(R.id.ll_homework);
            userName = view.findViewById(R.id.userName);
            tv_homework_title = view.findViewById(R.id.tv_homework_title);
            tv_homework_new_label = view.findViewById(R.id.tv_homework_new_label);

       /*     String user_identity = MySPUtilsUser.getInstance().getUserIdentity();
            if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) { //如果是老师
                tv_homework_title.setText(getString(R.string.homework));
            } else {
                tv_homework_title.setText(getString(R.string.myhomework));
            }*/
            if (ScreenTools.getInstance().isPad(getActivity())) {
                userName.setVisibility(View.GONE);
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
            } else {

                cl_notify = view.findViewById(R.id.cl_notify);
                iv_notify = view.findViewById(R.id.iv_notify);
                tv_notify_num = view.findViewById(R.id.tv_notify_num);
                iv_notify.setOnClickListener(this);

                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                userName.setVisibility(View.VISIBLE);
                String localUserName = AppPrefsUtil.getUserName();
                if (!TextUtils.isEmpty(localUserName)) {
                    userName.setText(this.getResources().getString(R.string.hi) + "," + localUserName);
                } else {
                    String mobile = MySPUtilsUser.getInstance().getUserMobile();
                    userName.setText(this.getResources().getString(R.string.hi) + "," + mobile);
                }
            }


            //注册EventBus
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }

            // initTabLayout();

            ll_course.setOnClickListener(this);
            ll_homework.setOnClickListener(this);
            courseMainPresenter = new CourseMainPresenter(this, getActivity());
            courseMainPresenter.getCourseDate();
        }
        return view;
    }


    @Override
    public void courseDateCallback(boolean isSuccess, Object data) {
        if (isSuccess) {

            if (isFromLessonfrgRefresh) { //如果日期有不一样则刷新，否则return
                isFromLessonfrgRefresh = false;//重置判断
                if (data instanceof List) {

                    List<String> datelist = (List<String>) data;
                    if (equalsDate(curDatelist, datelist)) {
                        return;
                    }
                }
            }
            //重置tab
            tabLayout.removeAllTabs();
            //  adapter.clearFragments();
            viewPager.removeAllViews();
            // adapter = null;
            positionToday = 0;
            if (isAdded()) {
                adapter = new CommonFragmentPagerAdapter(getChildFragmentManager());
                adapter.clearFragments();
            }
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

            if (data instanceof List) {
                List<String> datelist = (List<String>) data;
                curDatelist.clear();
                curDatelist.addAll(datelist);
                if (datelist != null && datelist.size() > 0) {
                    try {
                        for (int i = 0; i < datelist.size(); i++) {
                            String datestr = datelist.get(i);
                            LessonListFragment frag = LessonListFragment.newInstance(datestr);
                            // frag.refresh(datestr);
                            adapter.addFragment(frag);
                            if (StringUtil.isSameData(System.currentTimeMillis() + "", StringUtil.dateToStamp(datelist.get(i)))) {
                                tabLayout.addTab(tabLayout.newTab().setText("" + getResources().getString(R.string.today) + ""));
                                positionToday = i;
                            } else {
                                tabLayout.addTab(tabLayout.newTab().setText("" + dateStr(datelist.get(i)) + ""));
                            }

                        }
                    } catch (ParseException e) {

                    }
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(positionToday);

                    //去掉tab点击效果
                    if (getActivity() != null)
                        tabLayout.setTabRippleColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.transparent)));

                }
            }
        } else {
            //失败返回
            int error_code = (int) data;
            if (error_code == 404) { //表示用户是无身份用户，显示今天，暂无数据
                tabLayout.addTab(tabLayout.newTab().setText("  " + getResources().getString(R.string.today) + "  "));
                if (adapter == null && getChildFragmentManager() != null)
                    adapter = new CommonFragmentPagerAdapter(getChildFragmentManager());
                adapter.addFragment(LessonListFragment.newInstance(StringUtil.getCurTime()));
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(positionToday);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_course) {//跳转到日历展示课程页面
            if (getActivity() != null)
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(getActivity(), CalendarCourseActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.ll_homework) {//跳转到作业列表
            if (getActivity() != null)
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(getActivity(), HomeworkListActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.iv_notify) {
            //跳转到应用内通知列表页面
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(getActivity(), NoticeInAppActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void RefreshData(MessageEvent messageEvent) {

        if (messageEvent.getMessage_type().equals(EventConstant.CHANGE_IDENTITY_REFRESH)) {
            if (courseMainPresenter != null)
                courseMainPresenter.getCourseDate();
            String user_identity = MySPUtilsUser.getInstance().getUserIdentity();
            if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) { //如果是老师
                tv_homework_title.setText(getString(R.string.homework));
            } else {
                tv_homework_title.setText(getString(R.string.myhomework));
            }
        } else if (messageEvent.getMessage_type().equals(EventConstant.EVENT_EDIT_USER_INFO)) {

            String localUserName = AppPrefsUtil.getUserName();
            if (!TextUtils.isEmpty(localUserName)) {
                userName.setText(this.getResources().getString(R.string.hi) + "," + localUserName);
            } else {
                String mobile = MySPUtilsUser.getInstance().getUserMobile();
                userName.setText(this.getResources().getString(R.string.hi) + "," + mobile);
            }
        } else if (messageEvent.getMessage_type().equals(EventConstant.RECENTCOURSE_DATE_REFRESH)) {
            isFromLessonfrgRefresh = true;
            if (courseMainPresenter != null) //刷新日期，比对当前日期 如果有不同 则刷新整体数据
                courseMainPresenter.getCourseDate();
        } else if (messageEvent.getMessage_type().equals(EventConstant.CHANGE_IDENTITY_REFRESH)) {//check身份后刷新接口
            VariableConfig.checkIdentityFlag = false;
            courseMainPresenter.getCourseDate();
        } else if (messageEvent.getMessage_type().equals(EventConstant.EVENT_UNREAD_NOTIFICATION_MAIN_UI_UPDATE)) {//未读通知数据首页UI更新
            Map<String, Integer> map = (Map<String, Integer>) messageEvent.getMessage();
            if (!ScreenTools.getInstance().isPad(getActivity())) {
                int count = map.get("count");
                if (count > 0) {
                    tv_notify_num.setVisibility(View.VISIBLE);
                    //设置通知数字显示的背景
//                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(TKBaseApplication.myApplication.getApplicationContext(), tv_notify_num, getResources().getDimensionPixelSize(R.dimen.dimen_16x), "#FF2855");
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(TKBaseApplication.myApplication.getApplicationContext(), tv_notify_num,
                            getResources().getDimensionPixelSize(R.dimen.dimen_20x), -1, "", "#FF2855");
                    if (count > 999) {
                        tv_notify_num.setText("···");
                        return;
                    }
                    tv_notify_num.setText(count + "");
                } else {
                    tv_notify_num.setVisibility(View.GONE);
                }
            }

            //作业提示新标签
            int homework_unreads = map.get("homework_unreads");
            if (homework_unreads > 0) {
                tv_homework_new_label.setVisibility(View.VISIBLE);
                //设置新作业标签背景
                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(getActivity(), tv_homework_new_label,
                        getResources().getDimensionPixelSize(R.dimen.dimen_12x), -1, "", "#FF2855");
            } else {
                tv_homework_new_label.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private String dateStr(String sourceDateStr) {
        String dateStr = "";

        //判断下当前语言
        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
        if (!StringUtils.isBlank(locale_language) && !StringUtils.isBlank(locale_country) && getActivity() != null) {
            //获取本地app的国家语言
            List<Map<String, String>> datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(getActivity());

            //USERAGREEMENT_URL_CN ||USERAGREEMENT_URL_TW
            if (locale_language.equals(datas.get(1).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(1).get(SPConstants.LOCALE_COUNTRY)) ||
                    (locale_language.equals(datas.get(2).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(2).get(SPConstants.LOCALE_COUNTRY)))) {
                if (sourceDateStr != null && sourceDateStr.length() >= 10) {

                    if (!sourceDateStr.substring(5).equals("")) {  //判断月份

                        dateStr = sourceDateStr.substring(5).replace("-", getActivity().getResources().getString(R.string.month)) +
                                getActivity().getResources().getString(R.string.day);
                    }
                }
            } else {       //USERAGREEMENT_URL_EN
                String monthStr = "";
                String[] months = getActivity().getResources().getStringArray(R.array.montharray);
                String tempMonth = "";
                String tempDay = "";
                if (sourceDateStr != null && sourceDateStr.length() >= 10) {
                    tempMonth = sourceDateStr.substring(5, 7);
                    tempDay = sourceDateStr.substring(8, 10);
                    if (tempMonth.equals("01")) { //判断月份
                        monthStr = months[0];
                    } else if (tempMonth.equals("02")) {
                        monthStr = months[1];
                    } else if (tempMonth.equals("03")) {
                        monthStr = months[2];
                    } else if (tempMonth.equals("04")) {
                        monthStr = months[3];
                    } else if (tempMonth.equals("05")) {
                        monthStr = months[4];
                    } else if (tempMonth.equals("06")) {
                        monthStr = months[5];
                    } else if (tempMonth.equals("07")) {
                        monthStr = months[6];
                    } else if (tempMonth.equals("08")) {
                        monthStr = months[7];
                    } else if (tempMonth.equals("09")) {
                        monthStr = months[8];
                    } else if (tempMonth.equals("10")) {
                        monthStr = months[9];
                    } else if (tempMonth.equals("11")) {
                        monthStr = months[10];
                    } else if (tempMonth.equals("12")) {
                        monthStr = months[11];
                    }
                    String dayStr = "";
                    if (tempDay.equals("01")) {
                        dayStr = "1st,";
                    } else if (tempDay.equals("02")) {
                        dayStr = "2nd,";
                    } else if (tempDay.equals("03")) {
                        dayStr = "3rd,";
                    } else {
                        if (!tempDay.equals(""))
                            dayStr = tempDay + "th,";
                    }

                    dateStr = dayStr + monthStr;
                }
            }
        }

        return dateStr;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//		((ViewGroup) mTabHost.getParent()).removeView(mTabHost);
//		((ViewGroup) mTabHost.getParent()).removeAllViewsInLayout();
//		((ViewGroup) mTabHost.getParent()).removeAllViews();

        if (tabLayout != null) {
            ViewGroup parenGroup = (ViewGroup) tabLayout.getParent();
            if (parenGroup != null) {
                parenGroup.removeAllViewsInLayout();
            }
        }
    }

    private boolean equalsDate(List<String> curDateList, List<String> newDateList) {
        return newDateList.equals(curDateList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
