package com.github.ferransogas.walk_up.model

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs
import kotlin.math.sqrt

class WalkDetector(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val linearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    private var filteredAccelValues = FloatArray(3)
    private var filteredGyroValues = FloatArray(3)
    private var filteredLinearAccelValues = FloatArray(3)
    private var stepCount = 0
    private var lastStepTime = 0L
    private val stepTimings = ArrayDeque<Long>()
    private var verticalMovementSum = 0f
    private var consistentMovementCounter = 0

    private var lastUpdateTime = 0L
    private var currentWalkingState = false
    private val _walkProgress = MutableStateFlow(0f)
    val walkProgress: StateFlow<Float> = _walkProgress.asStateFlow()

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> processAccelerometerData(event.values)
                Sensor.TYPE_GYROSCOPE -> processGyroscopeData(event.values)
                Sensor.TYPE_LINEAR_ACCELERATION -> processLinearAccelerationData(event.values)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun isWalking(): Boolean {
        val (x, y, z) = filteredGyroValues
        val gyroConsistent = x in GYRO_X_RANGE && y in GYRO_Y_RANGE && z in GYRO_Z_RANGE

        val (lx, ly, lz) = filteredLinearAccelValues
        val linearConsistent = lx in LINEAR_ACCEL_X_RANGE && ly in LINEAR_ACCEL_Y_RANGE && lz in LINEAR_ACCEL_Z_RANGE

        return gyroConsistent && linearConsistent
    }

    fun startListening() {
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(listener, gyroscope, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(listener, linearAcceleration, SensorManager.SENSOR_DELAY_GAME)
    }

    fun stopListening() {
        sensorManager.unregisterListener(listener)
    }

    private fun processAccelerometerData(values: FloatArray) {
        filteredAccelValues = lowPassFilter(values, filteredAccelValues)
        updateVerticalMovement(filteredAccelValues[1] - SensorManager.GRAVITY_EARTH)
        val isStep = detectStep(filteredAccelValues, ACCEL_THRESHOLD)
        updateWalkingState(isStep)
    }

    private fun processGyroscopeData(values: FloatArray) {
        filteredGyroValues = lowPassFilter(values, filteredGyroValues)
    }

    private fun processLinearAccelerationData(values: FloatArray) {
        filteredLinearAccelValues = lowPassFilter(values, filteredLinearAccelValues)
        val isStep = detectStep(filteredLinearAccelValues, LINEAR_ACCEL_THRESHOLD)
        updateWalkingState(isStep)
    }

    private fun lowPassFilter(input: FloatArray, output: FloatArray): FloatArray {
        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }
        return output
    }

    private fun calculateMagnitude(values: FloatArray): Float {
        return sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
    }

    private fun detectStep(values: FloatArray, threshold: Float): Boolean {
        val currentTime = System.currentTimeMillis()
        val magnitude = calculateMagnitude(values)

        if (magnitude > threshold && (currentTime - lastStepTime) > MIN_STEP_INTERVAL) {
            stepTimings.addLast(currentTime)
            if (stepTimings.size > STEP_TIMING_WINDOW) stepTimings.removeFirst()
            stepCount++
            lastStepTime = currentTime
            return true
        }
        return false
    }

    private fun updateVerticalMovement(verticalAcceleration: Float) {
        verticalMovementSum += abs(verticalAcceleration)
        if (verticalMovementSum > VERTICAL_MOVEMENT_THRESHOLD) {
            consistentMovementCounter++
            verticalMovementSum = 0f
        } else {
            consistentMovementCounter = 0
        }
    }

    private fun updateWalkingState(isStep: Boolean) {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastStep = currentTime - lastStepTime
        var newWalkingState = currentWalkingState

        if (isStep && isStepTimingConsistent() && consistentMovementCounter >= CONSISTENT_MOVEMENT_THRESHOLD) {
            if (stepCount >= MIN_STEPS_FOR_WALKING && isWalking()) {
                newWalkingState = true
            }
        } else if (currentWalkingState && timeSinceLastStep > QUICK_STOP_INTERVAL) {
            newWalkingState = false
            resetWalkingState()
        }

        // Update progress based on time elapsed
        if (lastUpdateTime != 0L) {
            val deltaSeconds = (currentTime - lastUpdateTime) / 1000f
            var newProgress = _walkProgress.value + (if (currentWalkingState) deltaSeconds else -deltaSeconds)
            newProgress = newProgress.coerceIn(0f, 60f)
            _walkProgress.value = newProgress
        }

        lastUpdateTime = currentTime
        currentWalkingState = newWalkingState

        if (timeSinceLastStep > RESET_INTERVAL) resetWalkingState()
    }

    private fun isStepTimingConsistent(): Boolean {
        if (stepTimings.size < STEP_TIMING_WINDOW) return false
        val intervals = stepTimings.zipWithNext { a, b -> b - a }
        val avgInterval = intervals.average()
        return intervals.all { abs(it - avgInterval) < MAX_INTERVAL_DEVIATION }
    }

    private fun resetWalkingState() {
        stepCount = 0
        stepTimings.clear()
        consistentMovementCounter = 0
    }

    companion object {
        private const val ALPHA = 0.2f
        private const val ACCEL_THRESHOLD = 11.0f
        private const val LINEAR_ACCEL_THRESHOLD = 0.6f
        private const val MIN_STEP_INTERVAL = 100L
        private const val MIN_STEPS_FOR_WALKING = 2
        private const val QUICK_STOP_INTERVAL = 600L
        private const val RESET_INTERVAL = 1500L
        private const val STEP_TIMING_WINDOW = 4
        private const val MAX_INTERVAL_DEVIATION = 150L
        private const val VERTICAL_MOVEMENT_THRESHOLD = 1.5f
        private const val CONSISTENT_MOVEMENT_THRESHOLD = 3

        private val LINEAR_ACCEL_X_RANGE = -0.6f..0.6f
        private val LINEAR_ACCEL_Y_RANGE = -0.6f..0.6f
        private val LINEAR_ACCEL_Z_RANGE = -0.6f..0.6f

        private val GYRO_X_RANGE = -0.6f..0.6f
        private val GYRO_Y_RANGE = -0.6f..0.6f
        private val GYRO_Z_RANGE = -0.6f..0.6f
    }
}