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

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.registration.Registration.SensorTypeValue;
import com.sonyericsson.extras.liveware.aef.sensor.Sensor;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfoHelper;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEvent;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEventListener;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorException;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The sample sensor control handles the accelerometer sensor on an accessory.
 * This class exists in one instance for every supported host application that
 * we have registered to
 */
class SonySensorControl extends ControlExtension {
	
	int rndm;
	
	int delay;
	long startTime;
	long currentTime;
	
	boolean ryck = false;

	Timer fiskTimer;

    private int mWidth = 220;
    private int mHeight = 176;

    private AccessorySensor mSensor;

    private final AccessorySensorEventListener mListener = new AccessorySensorEventListener() {

        @Override
        public void onSensorEvent(AccessorySensorEvent sensorEvent) {
            float[] data = sensorEvent.getSensorValues();
            float x = data[0];
            float y = data[1];
            float z = data[2];
    	    
    	    currentTime = System.currentTimeMillis() - startTime;
    	    
    	    
    	    if(y > 9 && currentTime > (delay + 200) && currentTime < (delay + 750)) {
    	    
    	    	Log.d("times", "currentTime: " + currentTime + " delay: " + delay + " y: "+ y + "");
    	    	
                Intent intent = new Intent(mContext, SonyPreferenceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    		intent.putExtra("gotFish", rndm);
    	    	mContext.startActivity(intent);
    	    	unregister();
    	    	ryck = true;
    	    	showLayout(R.layout.sensor, null);
    	    } else if (currentTime > (delay + 750) && ryck == false) {
    	    	ryck = false;
    	    	onResume();
    	    }   	    
        }
    };
    

    SonySensorControl(final String hostAppPackageName, final Context context) {
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
    public void onStart() {
    	super.onStart();
        showLayout(R.layout.sensor2, null);
        setScreenState(Control.Intents.SCREEN_STATE_ON);
    }
    

    @Override
    public void onResume() {
    	
        register();
        showLayout(R.layout.sensor2, null);
    	ryck = false;
    	rndm = Static.randomInt(0, 9);
        startTime = System.currentTimeMillis();
        
        delay = Static.ON_START_WAIT_TIME[rndm];

        fiskTimer = new Timer();
        TimerTask fiskTask = new TimerTask() {
        	public void run() {
        		startVibrator(50, 1, 1);
        		showLayout(R.layout.napp, null);
        	}
        };
        fiskTimer.schedule(fiskTask, delay); 
        
    }

    
    @Override
    public void onPause() {
        // Stop sensor
        unregister();
    }

    @Override
    public void onDestroy() {
        unregisterAndDestroy();
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

 
    public static boolean isWidthSupported(Context context, int width) {
        return width == context.getResources().getDimensionPixelSize(
                R.dimen.smart_watch_2_control_width)
                || width == context.getResources().getDimensionPixelSize(
                        R.dimen.smart_watch_control_width);
    }


    public static boolean isHeightSupported(Context context, int height) {
        return height == context.getResources().getDimensionPixelSize(
                R.dimen.smart_watch_2_control_height)
                || height == context.getResources().getDimensionPixelSize(
                        R.dimen.smart_watch_control_height);
    }

    private void determineSize(Context context, String hostAppPackageName) {
        Log.d(SonyExtensionService.LOG_TAG, "Now determine screen size.");

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
        Log.d(SonyExtensionService.LOG_TAG, "Register listener");
        if (mSensor != null) {
            try {
                if (mSensor.isInterruptModeSupported()) {
                    mSensor.registerInterruptListener(mListener);
                } else {
                    mSensor.registerFixedRateListener(mListener,
                            Sensor.SensorRates.SENSOR_DELAY_GAME);
                }
            } catch (AccessorySensorException e) {
                Log.d(SonyExtensionService.LOG_TAG, "Failed to register listener", e);
            }
        }
    }
    
    @Override
    public void onTouch(ControlTouchEvent event) {
        super.onTouch(event);
        if (event.getAction() == Control.Intents.TOUCH_ACTION_RELEASE) {
            fiskTimer.cancel();
            onResume();
        }
    }
}
