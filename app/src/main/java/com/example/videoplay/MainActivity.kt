package com.example.videoplay

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var videoView: VideoView
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay = findViewById<FloatingActionButton>(R.id.btnplay)
        val btnPause = findViewById<FloatingActionButton>(R.id.btnpause)
        seekBar = findViewById(R.id.seekbar)
        videoView = findViewById(R.id.videoView)
        handler = Handler(Looper.getMainLooper())

        // Set the video URI to the video in res/raw directory
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.assignment)
        videoView.setVideoURI(videoUri)

        initialiseSeekBar()

        btnPlay.setOnClickListener {
            videoView.start()
        }

        btnPause.setOnClickListener {
            videoView.pause()
        }
    }

    private fun initialiseSeekBar() {
        videoView.setOnPreparedListener { mp ->
            seekBar.max = videoView.duration
            runnable = Runnable {
                seekBar.progress = videoView.currentPosition
                handler.postDelayed(runnable, 500)
            }
            handler.postDelayed(runnable, 0)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoView.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No action needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No action needed
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        videoView.stopPlayback()
    }
}
