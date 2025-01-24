package com.example.authentication.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import com.example.authentication.androidWrapper.ActUtils
import com.example.authentication.databinding.ActivityMainBinding
import com.example.authentication.remote.RetrofitService
import com.example.authentication.remote.dataModel.DefaultModel
import com.example.authentication.remote.dataModel.GetApiModel
import com.example.authentication.remote.ext.ErrorUtils
import com.example.authentication.ui.MainActivity2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class ViewMainActivity(
    private val context: Context,
    private val actUtils: ActUtils
    ) {

    val binding = ActivityMainBinding.inflate(LayoutInflater.from(context))
    private lateinit var countDownTimer: CountDownTimer



    fun onClickHandler(androidId: String){

        binding.btnSend.setOnClickListener {

            val email = binding.edtInputEmail.text.toString()

            if (email.isNotEmpty()){

                sendCodeInEmail(email)
                binding.textInputEmail.error = null
                binding.btnSend.visibility = INVISIBLE
                binding.textInputEmail.visibility = INVISIBLE
                binding.txtSendEmail.visibility = VISIBLE
                binding.textInputCode.visibility = VISIBLE
                binding.btnConfirm.visibility = VISIBLE
                binding.txtWrong.visibility = VISIBLE
                binding.txtTimer.visibility = VISIBLE
                binding.txtSendEmail.text = "Send Code To Email: $email"

            }else {
                binding.textInputEmail.error = "Pleas Enter Email"
                return@setOnClickListener
            }
            startTimer()

        }

        binding.txtWrong.setOnClickListener {

                binding.btnSend.visibility = VISIBLE
                binding.textInputEmail.visibility = VISIBLE
                binding.txtSendEmail.visibility = INVISIBLE
                binding.textInputCode.visibility = INVISIBLE
                binding.btnConfirm.visibility = INVISIBLE
                binding.txtWrong.visibility = INVISIBLE
                binding.txtTimer.visibility = INVISIBLE
            stopTimer()
        }

        binding.btnConfirm.setOnClickListener {

            val code = binding.edtCode.text.toString()
            val email = binding.edtInputEmail.text.toString()

            when{
                code.isEmpty() -> binding.textInputCode.error = "لطفا مقادیر ورودی را وارد کنید"
                code.length < 6 -> binding.textInputCode.error = "لطفا کد 6 رقمی وارد کنید"
                else ->{
                    binding.textInputCode.error = null

                    verifyCode(
                        androidId,
                        code,
                        email
                    )
                }
            }

        }
    }

    private fun startTimer() {

        var timerText = binding.txtTimer

        countDownTimer = object : CountDownTimer(120000, 1000){

            override fun onTick(millisUntilFinished : Long) {

                val second = (millisUntilFinished / 1000).toInt()
                timerText.text = "$second"

            }

            override fun onFinish() {

                timerText.text = "00:00"

            }

        }

        countDownTimer.start()

    }

    private fun stopTimer() {
        countDownTimer.cancel()
    }

    private fun sendCodeInEmail(email: String) {

        val service = RetrofitService.loginApiService

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.sendRequest(email)

            try {

                if (response.isSuccessful){
                    launch(Dispatchers.Main) {

                        val data = response.body() as DefaultModel

                        Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()

                    }
                }else{
                    launch(Dispatchers.Main) {

                        Toast.makeText(
                            context,
                            ErrorUtils.getError(response),
                            Toast.LENGTH_SHORT).show()

                    }
                }

            }catch (e: java.lang.Exception){
                Log.e("ERR_PROGRAM", e.message.toString())
            }

        }

    }

    private fun verifyCode(androidId: String, code: String, email: String) {

        val service = RetrofitService.loginApiService

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.verifyCode(androidId, code, email)

            try {

                if (response.isSuccessful){
                    launch(Dispatchers.Main) {

                        val data = response.body() as GetApiModel
                        data.api
                        stopTimer()
                        Toast.makeText(context, "ورود شما موفقیت آمیز بود", Toast.LENGTH_LONG).show()

                        context.startActivities(arrayOf(Intent(context, MainActivity2::class.java)))
                        actUtils.finished()

                    }
                }else{
                    launch(Dispatchers.Main) {

                        Toast.makeText(
                            context,
                            ErrorUtils.getError(response),
                            Toast.LENGTH_SHORT).show()

                    }
                }

            }catch (e: java.lang.Exception){
                Log.e("ERR_PROGRAM", e.message.toString())
            }

        }

    }


}