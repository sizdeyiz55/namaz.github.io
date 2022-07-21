package com.namazvakitleri.internetsiz.main

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.namazvakitleri.internetsiz.R

class Kible : AppCompatActivity(), SensorEventListener {
    private var imageView: ImageView? = null
    private var textView: TextView? = null
    private var sensorManager: SensorManager? = null
    private var currentdegree = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kible)
        imageView = findViewById<View>(R.id.compassimages) as ImageView
        textView = findViewById<View>(R.id.headingnames) as TextView
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val degree = Math.round(event.values[0]).toFloat()
        textView!!.text = java.lang.Float.toString(degree) + "°"
        val rotateAnimation = RotateAnimation(currentdegree, -degree, RotateAnimation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 210
        rotateAnimation.fillAfter = true
        imageView!!.startAnimation(rotateAnimation)
        currentdegree = -degree
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //no use
    }
}