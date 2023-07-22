package ru.zakiralekperov.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    private lateinit var stopwatch: Chronometer
    private var isRunning = false
    private var offset: Long = 0
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button

    private val OFFSET_KEY = "offset"
    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        if(savedInstanceState != null){
            offset = savedInstanceState.getLong(OFFSET_KEY)
            isRunning = savedInstanceState.getBoolean(RUNNING_KEY)

            if (isRunning){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

        startButton = findViewById<Button>(R.id.start_button)

        startButton.setOnClickListener {
            startButtonClick()
        }

        pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            pauseButtonClick()
        }

        resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            resetButtonClick()
        }

    }

    private fun startButtonClick(){
        if (!isRunning){
            setBaseTime()
            stopwatch.start()
            isRunning = true
        }
    }

    private fun pauseButtonClick() {
        if (isRunning){
            saveOffset()
            stopwatch.stop()
            isRunning = false
        }
    }

    private fun resetButtonClick() {
        offset = 0
        setBaseTime()
    }

    //Обновляет время
    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, isRunning)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        if(isRunning){
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    override fun onPause() {
        super.onPause()
        if(isRunning){
            saveOffset()
            stopwatch.stop()
        }
    }
}