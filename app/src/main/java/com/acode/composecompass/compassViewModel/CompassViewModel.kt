package com.acode.composecompass.compassViewModel

import android.app.Application
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.round

class CompassViewModel(application: Application) : AndroidViewModel(application),
    SensorEventListener {

    //Compass values
    private var _degreesV = MutableStateFlow<Double>(0.0)
    val degreesV = _degreesV.asStateFlow()

    private var _angleV = MutableStateFlow<Double>(0.0)
    val angleV = _angleV.asStateFlow()

    private var _directionV = MutableStateFlow<String>("")
    val directionV = _directionV.asStateFlow()

//    //Geo Co-ordinates
//    private var _longitude = MutableStateFlow<Double>(0.0)
//    val longitude = _longitude.asStateFlow()
//
//    private var _latitude = MutableStateFlow<Double>(0.0)
//    val latitude = _latitude.asStateFlow()



    private val context = getApplication<Application>()

    private lateinit var sensorManager: SensorManager
//    private lateinit var locationManager : LocationManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    init {
        compass()
        updateOrientationAngles()
    }

    companion object{
        val KEY_ANGLE = "angle"
        val KEY_DIRECTION ="direction"
        val KEY_BACKGROUNG = "background"
        val KEY_NOTIFICATION_ID = "notificationId"
        val KEY_ON_SENSOR_CHANGED_ACTION = "com.raywenderlich.android.locaty.ON_SENSOR_CHANGED"
        val KEY_NOTIFICATION_STOP_ACTION = "com.raywenderlich.android.locaty.NOTIFICATION_STOP"
    }


    fun compass() {
        //1
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        //2
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )

        }
        //3
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        //1
        if (event == null) {
            return
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            //3
            System.arraycopy(
                event.values,
                0,
                accelerometerReading,
                0,
                accelerometerReading.size
            )
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(
                event.values,
                0,
                magnetometerReading,
                0,
                magnetometerReading.size
            )
        }
        updateOrientationAngles()

    }

    fun updateOrientationAngles() {
        //1
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        //2
        val orientation = SensorManager.getOrientation(
            rotationMatrix,
            orientationAngles
        )
        //3
        val degrees = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
        _degreesV.value = degrees
        //4
        val angle = round(degrees * 100) / 100
        _angleV.value = angle

        val direction = getDirection(degrees)
        _directionV.value = direction


    }

    private fun getDirection(angle: Double) : String{
        var direction = ""

        if (angle >= 350 || angle <= 10)
            direction = "N"
        if (angle < 350 && angle > 280)
            direction = "NW"
        if (angle <= 280 && angle > 260)
            direction = "W"
        if (angle <= 260 && angle > 190)
            direction = "SW"
        if (angle <= 190 && angle > 170)
            direction = "S"
        if (angle <= 170 && angle > 100)
            direction = "SE"
        if (angle <= 100 && angle > 80)
            direction = "E"
        if (angle <= 80 && angle > 10)
            direction = "NE"

        return direction
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("OnAccuracyChange","Accuracy Changed")
    }
    


}