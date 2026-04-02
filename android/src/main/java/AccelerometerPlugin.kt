package com.plugin.accelerometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Invoke
import app.tauri.plugin.Plugin
import app.tauri.plugin.JSObject

@TauriPlugin
class AccelerometerPlugin(private val activity: android.app.Activity) : Plugin(activity) {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var listener: SensorEventListener? = null

    @Command
    fun startListening(invoke: Invoke) {
        if (sensorManager == null) {
            sensorManager = activity.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val data = JSObject()
                    data.put("x", it.values[0])
                    data.put("y", it.values[1])
                    data.put("z", it.values[2])
                    data.put("timestamp", System.currentTimeMillis())
                    trigger("accelerometer_update", data)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager?.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        invoke.resolve()
    }

    @Command
    fun stopListening(invoke: Invoke) {
        listener?.let { sensorManager?.unregisterListener(it) }
        invoke.resolve()
    }
}