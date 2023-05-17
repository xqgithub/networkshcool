package com.talkcloud.networkshcool.baselibrary.entity

//    {"result":0,"data":[{"path":"upload\/20210518\/06b0c968f0006519b64137b0005bbf78.jpg","name":"IMG_20210514_162844R.jpg"}],"msg":"success"}


data class UploadEntity(
    val name: String,
    val path: String,
    val url: String,
    val id: String
)
