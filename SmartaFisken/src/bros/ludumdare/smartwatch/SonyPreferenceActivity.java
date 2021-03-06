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

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class SonyPreferenceActivity extends Activity {
	
	
	public static final int intKey = 0;
	
	private int                   	totalScore = 0;
    public boolean                  getPoint;
    private int                     noDuplicates = 0;
   
    private GridView                mGrid;
    private FishGridAdapter 		mAdapter;
    private ArrayList<Fish> 		mFishes = new ArrayList<Fish>();
    private int                     mGridItem = R.layout.griditem_fish;
    private Context                 mContext;
    private int                     mArrayValue;

    
    @Override
    protected void onStart() {
            super.onStart();
            mGrid = (GridView) findViewById(R.id.fishGridView);
            mAdapter = new FishGridAdapter(getApplicationContext(), mGridItem, mFishes);
            mGrid.setAdapter(mAdapter);
    }
    
    public void updateList() {
        mFishes.add(new Fish(Static.GOOD_HUNTING_MESSAGES[mArrayValue], Static.FISH_ARRAY[mArrayValue]));
        mGrid.setAdapter(mAdapter);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	
        int intentArrayValue = intent.getIntExtra("gotFish", intKey);
        mArrayValue = intentArrayValue;
        
        Log.d("sonypreferenceactivity", "got intent!");

        updateList();

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.layout.main_menu, menu);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId()==R.id.about){
    		Dialog dialog = new Dialog(this);
			dialog.setTitle("Om");
			dialog.setContentView(R.layout.dialog);
			dialog.show();
    		return true;
    	}
    	return false;
    }
    
    
    public void loadBitmap() {
    	
    	
    }
    

}
