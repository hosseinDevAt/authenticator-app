package com.example.authentication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authentication.R
import com.example.authentication.androidWrapper.ActUtils
import com.example.authentication.model.ModelMainActivity
import com.example.authentication.presenter.PresenterMainActivity
import com.example.authentication.view.ViewMainActivity

class MainActivity : AppCompatActivity(), ActUtils {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ViewMainActivity(this, this)
        setContentView(view.binding.root)


        val presenter = PresenterMainActivity(view, ModelMainActivity(this))
        presenter.onCreatePresenter()

    }

    override fun finished() {
        finish()
    }
}