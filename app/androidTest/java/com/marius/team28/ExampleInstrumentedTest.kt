package com.marius.team28

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.marius.team28.maps.MapsFragment

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Before
    fun setup(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val mapLocTest = MapsFragment()
        val locationResult  = mapLocTest.getCurrentLocation()
        val locationFromPhone = LatLng(59.9183383, 10.5890117)
        assertEquals(locationFromPhone, locationResult)

    }
}