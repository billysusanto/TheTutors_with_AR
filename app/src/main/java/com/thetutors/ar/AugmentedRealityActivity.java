package com.thetutors.ar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.thetutors.controller.VariabelConfig;
import com.thetutors.view.MainActivity;
import com.thetutors.view.TestDescription;

import org.rajawali3d.util.RajLog;
import org.rajawali3d.vuforia.RajawaliVuforiaActivity;

public class AugmentedRealityActivity extends RajawaliVuforiaActivity {
	private AugmentedRealityRenderer mRenderer;
	private RajawaliVuforiaActivity mUILayout;

	private ImageButton prev, next;
	private Button test;

	VariabelConfig publicVar = VariabelConfig.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//useCloudRecognition(true);
		//setCloudRecoDatabase("a75960aa97c3b72a76eb997f9e40d210d5e40bf2",
		//		"aac883379f691a2550e80767ccd445ffbaa520ca");
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		
		addContentView(ll, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		startVuforia();

		//enterScanningModeNative();
	}

	@Override
	protected void setupTracker() {
		int result = initTracker(TRACKER_TYPE_MARKER);
		if (result == 1) {
			result = initTracker(TRACKER_TYPE_IMAGE);
			if (result == 1) {
				super.setupTracker();
			} else {
				RajLog.e("Couldn't initialize image tracker.");
			}
		} else {
			RajLog.e("Couldn't initialize marker tracker.");
		}
	}

	@Override
	protected void initApplicationAR() {
		super.initApplicationAR();

		createFrameMarker(1, "Marker1", 50, 50);
		createFrameMarker(2, "Marker2", 50, 50);

		createImageMarker("StonesAndChips.xml");
	}

	@Override
	protected void initRajawali() {
        mRenderer = new AugmentedRealityRenderer(this);
        setRenderer(mRenderer);
		super.initRajawali();

		prev = new ImageButton(getApplicationContext());
		prev.setBackgroundResource(android.R.drawable.ic_media_previous);
		prev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(publicVar.getCount() > 0) {
					publicVar.setCount(publicVar.getCount() - 1);
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					AugmentedRealityActivity.this.finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "This is the First Page", Toast.LENGTH_LONG).show();
				}
			}
		});

		next = new ImageButton(getApplicationContext());
		next.setBackgroundResource(android.R.drawable.ic_media_next);
		next.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(publicVar.getCount() < publicVar.getTutorLength()-1) {
					publicVar.setCount(publicVar.getCount() + 1);
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					AugmentedRealityActivity.this.finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "You have reach the last tutorial page", Toast.LENGTH_LONG).show();
					test.setVisibility(View.VISIBLE);
				}
			}
		});

		test = new Button(getApplicationContext());
		test.setText("Take a Test");
		test.setVisibility(View.INVISIBLE);
		test.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent j = new Intent(AugmentedRealityActivity.this, TestDescription.class);
				startActivity(j);
				AugmentedRealityActivity.this.finish();
			}
		});

		mUILayout = this;
		LinearLayout ll = new LinearLayout(this);

		ll.addView(prev);
		ll.addView(test);
		ll.addView(next);

		//NOT WORKING
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.BOTTOM;
		params.gravity = Gravity.CENTER_HORIZONTAL;
		ll.setLayoutParams(params);

		mUILayout.addContentView(ll, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
}
