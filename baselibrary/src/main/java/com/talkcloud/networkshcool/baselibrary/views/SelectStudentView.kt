package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.networkshcool.baselibrary.entity.StudentAllEntity

interface SelectStudentView : IBaseView {

    fun handlerStudentInfo(studentEntity: StudentAllEntity, name: String = "")

    fun handlerStudentInfoFailed(message: String)


    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun hideSuccessLoading() {
    }

    override fun showFailed() {
    }

}