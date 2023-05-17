package com.talkcloud.networkshcool.baselibrary.api;


import com.talkcloud.networkshcool.baselibrary.entity.CountryAreaEntity;
import com.talkcloud.networkshcool.baselibrary.entity.CourseDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.CourseListEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkListEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkTeacherDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.JoinPlaybackRoomEntity;
import com.talkcloud.networkshcool.baselibrary.entity.JoinRoomEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonFilesEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonMonthEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonReportEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskEntity;
import com.talkcloud.networkshcool.baselibrary.entity.NoticeInAppEntity;
import com.talkcloud.networkshcool.baselibrary.entity.StudentAllEntity;
import com.talkcloud.networkshcool.baselibrary.entity.StudentData;
import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UploadEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserExtInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserInfoEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Date:2021/5/10
 * Time:19:14
 * author:joker
 * 客户端接口服务
 */
public interface ApiService {


    /*****  接口方法路径 start *****/

    //登录
//    String login = "login";
    //发送短信
//    String sms = "sms";
    //国家区域
//    String countrycode = "countrycode";
    //切换身份 7:老师 8:学生
//    String changeLoginIdentity = "user/changeLoginIdentity";
    //忘记密码
//    String forgotpwd = "pwd/forgot";
    //修改手机
//    String mobile = "mobile";
    //修改密码
//    String pwdupdate = "pwd/update";
    //用户退出
//    String logout = "logout";
    //我的课表（月）
//    String lessonMonth = "lesson/{month}";
    //系统版本
//    String sysversion = "sys/version";
    //验证手机号是否存在
//    String checkMobile = "checkMobile";
    // 账号校验，登录状态 用来校验密码或者验证码是否正确
//    String verify = "verify";

    //作业点评
//    String homeworkremark = "homework/{id}/remark";
    //编辑点评
//    String editremark = "homework/{id}/remark/{student_id}";
    //删除点评
//    String deleteremark = "homework/{id}/remark";

    //快捷评语
//    String usefulExpressions = "homework/usefulExpressions";

    //企业网盘
//    String resource = "resource";

    //获取课节列表
//    String lessonList = "lesson/{day}";

    //获取课程报告
//    String lessonReport = "lesson/{serial}/report";

    //获取课程详情
//    String courseDetail = "course/{id}";

    //获取课程详情
//    String courseList = "course";

    //获取作业列表(学生端)
//    String homeworkList = "homework";

    //获取作业列表(老师端)
//    String homeworkListTeacher = "homework/teacher";

    //获取课节日期
//    String courseDateList = "lesson/beforeAndAfterToday";

    // 意见反馈
//    String suggestion = "suggestion";

    // 上传
//    String upload = "upload";

    // 保存用户信息
//    String user = "user";

    //用户配置
//    String userConfig = "user/homepage";

    //作业
//    String homework = "homework";

    /*****  接口方法路径 end *****/

    /*****  接口方法 start *****/
    //用户登录
    @POST("login")
    Observable<Response<ApiResponse<LoginEntity>>> login(@Query("company_id") String company_id, @Body Map<String, Object> bodys);

    //测试用
    @POST("login")
    Observable<Response<ApiResponse<LoginEntity>>> login2(@Body Map<String, Object> bodys);

    //短信验证码
    @POST("sms")
    Observable<Response<ApiResponse>> sms(@Body Map<String, Object> bodys);

    //短信验证码2
    @POST("sms")
    Observable<Response<ApiResponse>> smsV2(@Body Map<String, Object> bodys);

    //获取课节列表
    @GET("lesson/{day}")
    Observable<Response<ApiResponse<List<LessonInfoEntity>>>> getLessonList(@Path("day") String day);

    //课程报告
    @GET("lesson/{serial}/report")
    Observable<Response<ApiResponse<LessonReportEntity>>> getLessonReport(@Path("serial") String serial);

    //获取区号列表
    @GET("countrycode")
    Observable<Response<ApiResponse<List<CountryAreaEntity>>>> countrycode();

    //切换身份
    @PUT("user/changeLoginIdentity")
    Observable<Response<ApiResponse<UserIdentityEntity>>> changeLoginIdentity(@Body Map<String, Object> bodys);

    //忘记密码
    @PUT("pwd/forgot")
    Observable<Response<ApiResponse<LoginEntity>>> forgotpwd(@Body Map<String, Object> bodys);

    //修改手机
    @PUT("mobile")
    Observable<Response<ApiResponse>> mobile(@Body Map<String, Object> bodys);

    //修改密码
    @PUT("pwd/update")
    Observable<Response<ApiResponse>> pwdupdate(@Body Map<String, Object> bodys);

    //用户退出
    @GET("logout")
    Observable<Response<ApiResponse>> logout();

    //我的课表（月）
    @GET("lesson/{month}")
    Observable<Response<ApiResponse<List<LessonMonthEntity>>>> lessonMonth(@Path("month") String month);

    //系统版本
    @GET("sys/version")
    Observable<Response<ApiResponse<SysVersionEntity>>> sysversion(@QueryMap Map<String, Object> options);

    //验证手机号是否存在
    @POST("checkMobile")
    Observable<Response<ApiResponse>> checkMobile(@Body Map<String, Object> bodys);

    //账号校验，登录状态 用来校验密码或者验证码是否正确
    @POST("verify")
    Observable<Response<ApiResponse>> verify(@Body Map<String, Object> bodys);

    //作业点评提交
    @POST("homework/{id}/remark")
    Observable<Response<ApiResponse>> homeworkremark(@Path("id") String id, @Body Map<String, Object> body);

    //编辑点评
    @PUT("homework/{id}/remark/{student_id}")
    Observable<Response<ApiResponse>> editremark(@Path("id") String id, @Path("student_id") String student_id, @Body Map<String, Object> body);

    //删除点评
    @HTTP(method = "DELETE", path = "homework/{id}/remark", hasBody = true)
    Observable<Response<ApiResponse>> deleteremark(@Path("id") String id, @Body Map<String, Object> body);

    //快捷评语
    @GET("homework/usefulExpressions")
    Observable<Response<ApiResponse>> usefulExpressions(@Query("homework_id") String homework_id);

    //企业网盘
    @GET("resource")
    Observable<Response<ApiResponse<NetworkDiskEntity>>> resource(@QueryMap Map<String, Object> options);

    //获取课程详情
    @GET("course/{id}")
    Observable<Response<ApiResponse<CourseDetailInfoEntity>>> getCourseDetail(@Path("id") String id);

    //获取课程列表
    @GET("course")
    Observable<Response<ApiResponse<CourseListEntity>>> getCourseList(@QueryMap Map<String, Object> options);

    //获取作业列表
    @GET("homework")
    Observable<Response<ApiResponse<HomeworkListEntity>>> getHomeworkList(@QueryMap Map<String, Object> options);

    @GET("homework/teacher")
    Observable<Response<ApiResponse<HomeworkListEntity>>> getHomeworkListTeacher(@QueryMap Map<String, Object> options);

    //获取作业列表
    @GET("homework/{homewordId}/students")
    Observable<Response<ApiResponse<List<HomeworkDetailInfoEntity>>>> getHomeworkDetailList(@Path("homewordId") String homework_id, @QueryMap Map<String, Object> options);

    //批量通知
    @POST("homework/{id}/remind")
    Observable<Response<ApiResponse<String>>> homeworkNotify(@Path("id") String homework_id, @Body Map<String, Object> body);


    //删除老师作业草稿
    @DELETE("homework/{id}")
    Observable<Response<ApiResponse<String>>> deleteHomework(@Path("id") String homework_id);


    //获取首页课程最近三次日期
    @GET("lesson/beforeAndAfterToday")
    Observable<Response<ApiResponse<List<String>>>> getCourseDateList();

    //意见反馈
    @POST("suggestion")
    Observable<Response<ApiResponse<String>>> requestSuggestion(@Body Map<String, Object> body);

    // 上传图片
    @Multipart
    @POST("upload")
    Observable<Response<ApiResponse<List<UploadEntity>>>> uploadFiles(@Part List<MultipartBody.Part> parts);

    // 保存用户信息
    @PUT("user")
    Observable<Response<ApiResponse<String>>> saveUserInfo(@Body Map<String, String> body);

    // 获取用户信息
    @GET("user")
    Observable<Response<ApiResponse<UserInfoEntity>>> getUserInfo();

    // 获取用户信息
    @GET("user/homepage")
    Observable<Response<ApiResponse<UserExtInfoEntity>>> getUserExtInfo();


    // 获取去教室上课信息
//    String joinRoom = "lesson/{id}/room";
//
//    // 获取教室录制件
//    String joinPlaybackRoom = "lesson/{id}/record";

    //    // 进入房间信息
//    @GET(joinRoom)
//    Observable<Response<ApiResponse<String>>> joinRoom(@Path("id") String id);
//
//    // 回放
//    @GET(joinPlaybackRoom)
//    Observable<Response<ApiResponse<String>>> joinPlaybackRoom(@Path("id") String id);


    @GET("room/{serial}")
    Observable<Response<ApiResponse<JoinRoomEntity>>> joinRoom2(@Path("serial") String serial);


    @GET("lesson/{id}/room")
    Observable<Response<ApiResponse<JoinRoomEntity>>> joinRoom(@Path("id") String id);


    @GET("lesson/{id}/record")
    Observable<Response<ApiResponse<List<JoinPlaybackRoomEntity>>>> joinPlaybackRoom(@Path("id") String id);


    // 获取学生列表
    @GET("student")
    Observable<Response<ApiResponse<StudentAllEntity>>> getStudentList(@QueryMap Map<String, Object> body);

//    @GET("student")
//    Observable<Response<ApiResponse<StudentAllEntity>>> getStudentList(@Query("lesson_id") String lesson_id, @Query("page") int page);

    // 获取课堂学生列表
    @GET("lesson/{serial}/student")
    Observable<Response<ApiResponse<List<StudentData>>>> getLessonStudentList(@Path("serial") String serial);


    // 老师 发布作业
    @POST("homework")
    Observable<Response<ApiResponse<TKHomeworkDetailEntity>>> publishHomework(@Query("lesson_id") String lesson_id, @Body Map<String, Object> body);


    // 学生端 作业详情
    @GET("homework/{homework_id}-{student_id}")
    Observable<Response<ApiResponse<HomeworkStudentDetailEntity>>> getHomeworkStudentDetails(@Path("homework_id") String homework_id, @Path("student_id") String student_id);


    // 学生端 作业详情 撤回
    @PUT("homework/{homework_id}-{student_id}")
    Observable<Response<ApiResponse>> rollbackHomeworkStudentDetails(@Path("homework_id") String homework_id, @Path("student_id") String student_id);


    // 老师端作业详情
    @GET("homework/{id}")
    Observable<Response<ApiResponse<HomeworkTeacherDetailEntity>>> getHomeworkTeacherDetails(@Path("id") String homework_id);


    // 学生端 提交作业
    @POST("homework/{homework_id}-{student_id}")
    Observable<Response<ApiResponse<String>>> submitHomework(@Path("homework_id") String homework_id, @Path("student_id") String student_id, @Body Map<String, Object> body);


    // 老师 获取作业草稿 编辑
    @PUT("homework/{id}")
    Observable<Response<ApiResponse<String>>> putEditHomework(@Path("id") String homeworkId, @Query("lesson_id") String lessonId, @Body Map<String, Object> body);

    //获取教室课件
    @GET("lesson/{id}/files")
    Observable<Response<ApiResponse<List<LessonFilesEntity>>>> lessonfiles(@Path("id") String lessonId);

    //未读通知
    @GET("user/notice/new")
    Observable<Response<ApiResponse>> noticenew();

    //通知列表
    @GET("user/notice")
    Observable<Response<ApiResponse<NoticeInAppEntity>>> notice(@QueryMap Map<String, Object> options);

    //读取通知
    @POST("user/notice")
    Observable<Response<ApiResponse>> readnotice(@Body Map<String, Object> body);

    /*****  接口方法 end *****/


}
