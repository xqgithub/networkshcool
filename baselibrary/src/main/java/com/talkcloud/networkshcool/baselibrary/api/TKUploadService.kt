package com.talkcloud.networkshcool.baselibrary.api

import android.util.Log
import okhttp3.*
import okhttp3.MultipartBody.Builder
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class TKUploadService {

    companion object {

        var token =
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjoie1widXNlcmlkXCI6MTAwMTA2MCxcImxvY2FsZVwiOlwiQ05cIixcImFjY291bnRcIjpcIjg2MTg1MDcxMDMwMTZcIixcInVzZXJuYW1lXCI6XCJcXHU2NzMxXFx1NWUwOFxcdTVmYjdcIixcImF2YXRhclwiOlwiaHR0cHM6XFxcL1xcXC9od2RlbW9jZG4udGFsay1jbG91ZC5uZXRcXFwvdXBsb2FkXFxcLzIwMjEwNDIzXFxcLzUzMGM4MDAwMjcwZDcyMmYzYTI4YTQxMzljMjA4NTI3LnBuZ1wiLFwiY3VycmVudF9pZGVudGl0eVwiOjh9IiwiYXVkIjoiIiwiZXhwIjoyNDg1MjM1OTY1LCJpYXQiOjE2MjEyMzU5NjUsImlzcyI6IiIsImp0aSI6ImM3NmFjMGU0OGEyNTg2MGZkNDk1ODY5NjVkYzI1ZTUxIiwibmJmIjoxNjIxMjM1OTY1LCJzdWIiOiIifQ.apYgXUDtm7Weu0vHe1TTG9uexGKu5ded_e16swSmk-4"

        @JvmStatic
        fun uploadFile(url: String, filePathList: MutableList<String>) {

            val multipartBodyBuilder = Builder()

            filePathList.forEach {
                val file = File(it);

                Log.d("goyw", "filename : ${file.name}")

                multipartBodyBuilder.setType(MultipartBody.FORM)
                    .addFormDataPart("files[]", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file)
                );
            }

            val requestBody: RequestBody = multipartBodyBuilder.build()


            Log.d("goyw", "url : $url")

            val request: Request = Request.Builder()
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("version", "v1")
                .addHeader("terminal-type", "1")
                .addHeader("Authorization", token)
                .url(url)
                .post(requestBody)
                .build()


            // Log信息拦截器
            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY


            val mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .build();

            val call: Call = mOkHttpClient.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("goyw", "Response: ${e.message}");
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("goyw", "Response: $response");
                }
            });
        }
    }

}