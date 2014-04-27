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

 * Neither the name of the Sony Ericsson Mobile Communications AB / Sony Mobile
 Communications AB nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

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

import com.sonymobile.smartconnect.extension.sensorsample.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SonyPreferenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    
	@Override
	protected void onNewIntent(Intent intent) {
//		once = true;
		
		String x = intent.getStringExtra("x-value");
		String y = intent.getStringExtra("y-value");
		String z = intent.getStringExtra("z-value");
		
		TextView xValue = (TextView) findViewById(R.id.textView_xvalue);
		TextView yValue = (TextView) findViewById(R.id.textView_yvalue);
		TextView zValue = (TextView) findViewById(R.id.textView_zvalue);
		TextView totalScoreTv = (TextView) findViewById(R.id.textView_score);
		
		xValue.setText(x);
		yValue.setText(y);
		zValue.setText(z);

	}

}
