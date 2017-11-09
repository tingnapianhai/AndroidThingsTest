package com.example.androidthings.test

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException


/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class HomeActivity : Activity() {

  private val TAG = "HomeActivity"

  private val BUTTON_PIN_NAME = "GPIO_174"

  // GPIO connection to button input
  private var mButtonGpio: Gpio? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val service = PeripheralManagerService()
    Log.d(TAG, "Available GPIO: " + service.gpioList)

    try {
      // Create GPIO connection.
      mButtonGpio = service.openGpio(BUTTON_PIN_NAME)

      // Configure as an input, trigger events on every change.
      mButtonGpio?.setDirection(Gpio.DIRECTION_IN)
      mButtonGpio?.setEdgeTriggerType(Gpio.EDGE_BOTH)
      // Value is true when the pin is LOW
      mButtonGpio?.setActiveType(Gpio.ACTIVE_LOW)

      // Register the event callback.
      mButtonGpio?.registerGpioCallback(mCallback);

    } catch (e: IOException) {
      Log.w(TAG, "Error opening GPIO", e)
    }

  }

  private val mCallback = object : GpioCallback() {
    override fun onGpioEdge(gpio: Gpio?): Boolean {
      try {
        Log.i(TAG, "GPIO changed, button " + gpio!!.value)
      } catch (e: IOException) {
        Log.w(TAG, "Error reading GPIO")
      }
      // Return true to keep callback active.
      return true
    }
  }

}
