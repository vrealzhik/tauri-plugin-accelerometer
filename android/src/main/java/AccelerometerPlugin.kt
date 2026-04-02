package com.plugin.accelerometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import app.tauri.annotation.Command
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Invoke
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin

private const val TAG = "AccelerometerPlugin"

@TauriPlugin
class AccelerometerPlugin(private val activity: android.app.Activity) : Plugin(activity) {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var listener: SensorEventListener? = null

    @Command
    fun startListening(invoke: Invoke) {
        Log.i(TAG, "=== start() вызвана ===")

        try {
            if (sensorManager == null) {
                Log.d(TAG, "Получаем SensorManager...")
                sensorManager = activity.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
                accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            }

            if (accelerometer == null) {
                Log.e(TAG, "Акселерометр не найден на устройстве!")
                invoke.reject("Accelerometer not available")
                return
            }

            Log.i(TAG, "Акселерометр найден: ${accelerometer?.name}, разрешение: ${accelerometer?.resolution}")

            listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        val data = JSObject()
                        data.put("x", it.values[0])
                        data.put("y", it.values[1])
                        data.put("z", it.values[2])
                        data.put("timestamp", System.currentTimeMillis())

                        Log.d(TAG, "onSensorChanged → x=${it.values[0]:.3f}, y=${it.values[1]:.3f}, z=${it.values[2]:.3f}")

                        trigger("accelerometer:update", data)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    Log.d(TAG, "onAccuracyChanged: accuracy=$accuracy")
                }
            }

            val registered = sensorManager?.registerListener(
                listener,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI   // можно поменять на SENSOR_DELAY_GAME для большей частоты
            ) ?: false

            if (registered) {
                Log.i(TAG, "Слушатель акселерометра успешно зарегистрирован (SENSOR_DELAY_UI)")
            } else {
                Log.e(TAG, "Не удалось зарегистрировать слушатель!")
            }

            invoke.resolve()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в start(): ${e.message}", e)
            invoke.reject("Error starting accelerometer: ${e.message}")
        }
    }

    @Command
    fun stopListening(invoke: Invoke) {
        Log.i(TAG, "=== stop() вызвана ===")
        try {
            listener?.let {
                sensorManager?.unregisterListener(it)
                Log.i(TAG, "Слушатель успешно отключён")
            }
            listener = null
            invoke.resolve()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в stop(): ${e.message}", e)
            invoke.reject("Error stopping accelerometer")
        }
    }
}