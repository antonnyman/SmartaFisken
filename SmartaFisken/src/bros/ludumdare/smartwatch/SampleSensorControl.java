/*
 Copyright (c) 2011, Sony Ericsson Mobile Communications AB
 Copyright (c) 2011-2013, Sony Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB nor the names
 of its contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package bros.ludumdare.smartwatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.aef.registration.Registration.SensorTypeValue;
import com.sonyericsson.extras.liveware.aef.sensor.Sensor;
import com.sonyericsson.extras.liveware.aef.sensor.Sensor.SensorAccuracy;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfoHelper;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEvent;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEventListener;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorException;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorManager;
import com.sonymobile.smartconnect.extension.sensorsample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The sample sensor control handles the accelerometer sensor on an accessory.
 * This class exists in one instance for every supported host application that
 * we have registered to
 */
class SampleSensorControl extends ControlExtension {

    private int mWidth = 220;
    private int mHeight = 176;

    private AccessorySensor mSensor;

    private final AccessorySensorEventListener mListener = new AccessorySensorEventListener() {

    	
        @Override
        public void onSensorEvent(AccessorySensorEvent sensorEvent) {
            Log.d(SampleExtensionService.LOG_TAG, "Listener: OnSensorEvent");
            float[] data = sensorEvent.getSensorValues();
            float x = data[0];
            float y = data[1];
            float z = data[2];
            
            Log.d("sensor x", String.valueOf(x));
            
            Intent intent = new Intent(mContext, SamplePreferenceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	    intent.putExtra("values", Float.toString(x));
    	    mContext.startActivity(intent);
        }
    };
    
    protected void sendText(int layoutReference, String text) {
//    	
    };


    SampleSensorControl(final String hostAppPackageName, final Context context) {
        super(context, hostAppPackageName);

        AccessorySensorManager manager = new AccessorySensorManager(context, hostAppPackageName);
        // Add accelerometer, if supported
        if (DeviceInfoHelper.isSensorSupported(context, hostAppPackageName,
                SensorTypeValue.ACCELEROMETER)) {
            mSensor = manager.getSensor(SensorTypeValue.ACCELEROMETER);
        }
        // Determine screen size
        determineSize(context, hostAppPackageName);
    }

    @Override
    public void onResume() {
        Log.d(SampleExtensionService.LOG_TAG, "Starting control");

        showLayout(R.layout.sensor, null);
        
        // Note: Setting the screen to be always on will drain the accessory
        // battery. It is done here solely for demonstration purposes
        setScreenState(Control.Intents.SCREEN_STATE_DIM);

        // Start listening for sensor updates.
        register();
    }

    @Override
    public void onPause() {
        // Stop sensor
        unregister();
    }

    @Override
    public void onDestroy() {
        // Stop sensor
        unregisterAndDestroy();
    }

    /**
     * Check if control supports the given width
     * 
     * @param context The context.
     * @param int width The width.
     * @return true if the control supports the given width
     */
    public static boolean isWidthSupported(Context context, int width) {
        return width == context.getResources().getDimensionPixelSize(
                R.dimen.smart_watch_2_control_width)
                || width == context.getResources().getDimensionPixelSize(
                        R.dimen.smart_watch_control_width);
    }

    /**
     * Check if control supports the given height
     * 
     * @param context The context.
     * @param int height The height.
     * @return true if the control supports the given height
     */
    public static boolean isHeightSupported(Context context, int height) {
        return height == context.getResources().getDimensionPixelSize(
                R.dimen.smart_watch_2_control_height)
                || height == context.getResources().getDimensionPixelSize(
                        R.dimen.smart_watch_control_height);
    }

    private void determineSize(Context context, String hostAppPackageName) {
        Log.d(SampleExtensionService.LOG_TAG, "Now determine screen size.");

        boolean smartWatch2Supported = DeviceInfoHelper.isSmartWatch2ApiAndScreenDetected(context,
                hostAppPackageName);
        if (smartWatch2Supported) {
            mWidth = context.getResources().getDimensionPixelSize(
                    R.dimen.smart_watch_2_control_width);
            mHeight = context.getResources().getDimensionPixelSize(
                    R.dimen.smart_watch_2_control_height);
        }
        else {
            mWidth = context.getResources()
                    .getDimensionPixelSize(R.dimen.smart_watch_control_width);
            mHeight = context.getResources().getDimensionPixelSize(
                    R.dimen.smart_watch_control_height);
        }
    }


    private void register() {
        Log.d(SampleExtensionService.LOG_TAG, "Register listener");
        if (mSensor != null) {
            try {
                if (mSensor.isInterruptModeSupported()) {
                    mSensor.registerInterruptListener(mListener);
                } else {
                    mSensor.registerFixedRateListener(mListener,
                            Sensor.SensorRates.SENSOR_DELAY_UI);
                }
            } catch (AccessorySensorException e) {
                Log.d(SampleExtensionService.LOG_TAG, "Failed to register listener", e);
            }
        }
    }

    private void unregister() {
        if (mSensor != null) {
            mSensor.unregisterListener();
        }
    }

    private void unregisterAndDestroy() {
        unregister();
        mSensor = null;
    }

    @SuppressLint("DefaultLocale")
    private String getAccuracyText(int accuracy) {

        switch (accuracy) {
            case SensorAccuracy.SENSOR_STATUS_UNRELIABLE:
                return mContext.getString(R.string.accuracy_unreliable);
            case SensorAccuracy.SENSOR_STATUS_ACCURACY_LOW:
                return mContext.getString(R.string.accuracy_low);
            case SensorAccuracy.SENSOR_STATUS_ACCURACY_MEDIUM:
                return mContext.getString(R.string.accuracy_medium);
            case SensorAccuracy.SENSOR_STATUS_ACCURACY_HIGH:
                return mContext.getString(R.string.accuracy_high);
            default:
                return String.format("%d", accuracy);
        }
    }
}
