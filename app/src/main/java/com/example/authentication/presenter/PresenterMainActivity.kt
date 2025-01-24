package com.example.authentication.presenter

import com.example.authentication.model.ModelMainActivity
import com.example.authentication.view.ViewMainActivity

class PresenterMainActivity(
    private val view: ViewMainActivity,
    private val model: ModelMainActivity
) {

    fun onCreatePresenter(){

        view.onClickHandler(model.getAndroidId())

    }
}