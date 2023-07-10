package com.example.easetimer

import android.R.attr.button
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    private var timeSelected : Int = 0
    private var timeCountDown : CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffset : Long = 0
    private var isStart = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addBtn : ImageButton = findViewById(R.id.addBtn)
        addBtn.setOnClickListener {
            setTimeFunction()
        }
        val startBtn:Button = findViewById(R.id.startBtn)
        startBtn.setOnClickListener {
            startTimerSetup()
        }

        val resetBtn:ImageButton = findViewById(R.id.restart)
        resetBtn.setOnClickListener {
            resetTime()
        }

        val menuBtn:ImageButton = findViewById(R.id.menuBtn)
        menuBtn.setOnClickListener {
            settingsFunction()
        }

    }


    private fun resetTime() {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
            timeProgress = 0
            timeSelected = 0
            pauseOffset = 0
            timeCountDown = null
            val startBtn:Button = findViewById(R.id.startBtn)
            startBtn.text = "Start"
            isStart = true
            val progressBar = findViewById<ProgressBar>(R.id.cdTimerProgress)
            progressBar.progress = 0
            val timeRemaining : TextView = findViewById(R.id.cdTime)
            timeRemaining.text = "00"
            var timeRemainingSeconds : TextView = findViewById(R.id.cdTimeSeconds)
            timeRemainingSeconds.text = "00"

        }
    }
    private fun timerPause() {
        if (timeCountDown!=null) {
            timeCountDown!!.cancel()
        }
    }

    private fun startTimerSetup() {
        val startBtn: Button = findViewById(R.id.startBtn)
        if (timeSelected > timeProgress) {
            if (isStart) {
                startBtn.text = "Pause"
                startTimer(pauseOffset)
                isStart = false
            } else {
                isStart = true
                startBtn.text = "Resume"
                timerPause()
            }
        } else {
            Toast.makeText(this, "Enter Time", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(pauseOffSetL : Long) {
       val progressBar = findViewById<ProgressBar>(R.id.cdTimerProgress)
        progressBar.progress = timeProgress
        timeCountDown = object:CountDownTimer((timeSelected * 1000).toLong() - pauseOffSetL*1000, 1000) {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffset = timeSelected.toLong() - p0/60000
                progressBar.progress = timeSelected - timeProgress
                val timeRemaining:TextView = findViewById(R.id.cdTime)
//                timeRemaining.text = (timeSelected - timeProgress).toString()
                timeRemaining.text = String.format("%02d", ((timeSelected - timeProgress) / 60))
                val timeRemainingSeconds:TextView = findViewById(R.id.cdTimeSeconds)
                timeRemainingSeconds.text = String.format("%02d", ((timeSelected - timeProgress) % 60))
            }

            override fun onFinish() {
                resetTime()
                Toast.makeText(this@MainActivity, "Timer Up", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun setTimeFunction() {

        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.add_dialog)
        var numberPicker = timeDialog.findViewById<NumberPicker>(R.id.numPicker)
        numberPicker.minValue = 1
        numberPicker.maxValue = 60
        var timeRemaining : TextView = findViewById(R.id.cdTime)
        var timeRemainingSeconds : TextView = findViewById(R.id.cdTimeSeconds)
        val startBtn : Button = findViewById(R.id.startBtn)
        val progressBar = findViewById<ProgressBar>(R.id.cdTimerProgress)


        timeDialog.findViewById<Button>(R.id.selectBtn).setOnClickListener {
            resetTime()
            timeSelected = numberPicker.value * 60
            timeRemaining.text = String.format("%02d", ((timeSelected - timeProgress) / 60))
            timeRemainingSeconds.text = String.format("%02d", ((timeSelected - timeProgress) % 60))
            startBtn.text = "Start"
            progressBar.max = timeSelected
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    private fun settingsFunction() {
        val progressBar = findViewById<ProgressBar>(R.id.cdTimerProgress)

        val settingDialog = Dialog(this)
        settingDialog.setContentView(R.layout.settings_dialog)
        var ringBtn = settingDialog.findViewById<ImageButton>(R.id.ringBtn)
        var piBtn = settingDialog.findViewById<ImageButton>(R.id.piBtn)
        ringBtn.setOnClickListener {
            progressBar.setProgressDrawableTiled(getDrawable(R.drawable.progress_bar))
            Toast.makeText(this, "Ring Timer Selected", Toast.LENGTH_SHORT).show()
        }

        piBtn.setOnClickListener {
            progressBar.setProgressDrawableTiled(getDrawable(R.drawable.progress_bar_pi))
            Toast.makeText(this, "Pi Timer Selected", Toast.LENGTH_SHORT).show()
        }

        settingDialog.findViewById<Button>(R.id.exitBtn).setOnClickListener {

            settingDialog.dismiss()
        }
        settingDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timeCountDown != null) {
            timeCountDown?.cancel()
            timeProgress = 0
        }
    }
}