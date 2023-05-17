package com.talkcloud.networkshcool.baselibrary.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle3.components.support.RxFragment

/**
 * Author  guoyw
 * Date    2021/5/13
 * Desc    fragment 基类
 */
open abstract class BaseFragment : RxFragment() {

    protected lateinit var mActivity : Activity
    protected lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView = inflater?.inflate(getLayoutId(), container, false)
        return mView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        initListener()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    abstract fun initListener()

}