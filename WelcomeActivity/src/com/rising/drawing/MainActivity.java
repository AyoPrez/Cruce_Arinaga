package com.rising.drawing;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.Toast;

public class MainActivity extends Activity{

    ScreenThread myScreenThread;
    SurfaceHolder holder;
	Canvas canvas;
	Screen s;
	private Dialog MDialog;
	private ImageButton playButton;
	private NumberPicker metronome_speed;
	private EditText editText_metronome;
	private int tempo = 120;
	String score;
	private boolean play;
	private boolean stop = false;
	private Config config = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	
		Bundle b = this.getIntent().getExtras();
		score = b.getString("score");

		ActionBar aBar = getActionBar();	
		aBar.setTitle(R.string.score);	
		aBar.setIcon(R.drawable.ic_menu);
		aBar.setDisplayHomeAsUpEnabled(true);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		s = new Screen(this, score, dm.widthPixels, dm.densityDpi);
		if (s.isValidScreen()) {
			myScreenThread = new ScreenThread(holder, s);
			config = s.getConfig();
		}
		
		setContentView(s);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_score, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case R.id.metronome_button:
	    		metronome_options(tempo);
	    		return true;
	    	
	    	case android.R.id.home:
	    		s.Metronome_Stop();
	    		finish();
	    		return true;
	    		
	    	default:
	    		return true;
	    }
	}

	@Override
	public void onActionModeFinished (ActionMode mode) {
		s.Metronome_Stop();
	}
	
	//  Método que controla el dialog de las opciones del metrónomo
	private void metronome_options(int value){
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		int screenWith = screenSize.x;
		int screenHeight = screenSize.y;
		Log.i("Windowrrr", screenWith + ", " + screenHeight);
		
		MDialog = new Dialog(MainActivity.this,  R.style.cust_dialog);	
		MDialog.setContentView(R.layout.metronome_dialog);
		MDialog.setTitle(R.string.metronome);	
		MDialog.getWindow().setLayout(config.getAnchoDialogBpm(), config.getAltoDialogBpm());

		editText_metronome = (EditText)MDialog.findViewById(R.id.eT_metronome);

		metronome_speed = (NumberPicker)MDialog.findViewById(R.id.nm_metronome);
		metronome_speed.setMaxValue(300);
		metronome_speed.setMinValue(1);
		metronome_speed.setValue(value);
		metronome_speed.setWrapSelectorWheel(true);
		metronome_speed.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		metronome_speed.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChange(NumberPicker arg0, int arg1) {
				// TODO Auto-generated method stub
				editText_metronome.setText(arg0.getValue() + "");
			}
		});
					
		playButton = (ImageButton)MDialog.findViewById(R.id.playButton1);
		playButton.setOnClickListener(new OnClickListener(){
 
			@Override
			public void onClick(View v) {
				if (!editText_metronome.getText().toString().equals("")) {
					tempo = Integer.parseInt(editText_metronome.getText().toString());
				}
				else {
					tempo = metronome_speed.getValue();
				}
				
				if ( (tempo > 0) && (tempo < 301) ) {
					MainActivity.this.startActionMode(new ActionBarCallBack());
					
					play = true;
					stop = false;
					s.Metronome_Back();
					s.Metronome_Play(tempo);
					MDialog.dismiss();
				}
				else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
				                    R.string.speed_allowed, Toast.LENGTH_SHORT);
				    toast1.show();
				}
			}
		});
		
		MDialog.show();
	}

	//  Cambia el icono entre el pause y el play dependiendo del estado del metrónomo
	private void PlayButton_Status(MenuItem item){
		if (play) {
			play = false;
			stop = false;
		}
		else
			play = true;
		
		if (!play){
    		item.setIcon(R.drawable.play_button);
    		s.Metronome_Pause();    		
    	}else{
    		item.setIcon(R.drawable.pause_button);
    		
    		if (stop)
    			s.Metronome_Play(tempo);
    		else
    			s.Metronome_Pause();
    	}
	}

	private void StopButton_Status(ActionMode m){
		stop = true;
		play = false;
		
		s.Metronome_Stop();
		
		Menu menu = m.getMenu();
        menu.getItem(2).setIcon(R.drawable.play_button);
	}
	
	//  Habilita o deshabilita elementos según esté o no activado el metrónomo
	private void PlayItemsControl(ActionMode m){
		Menu menu = m.getMenu();
		
		if(stop && !play){
			menu.getItem(0).setEnabled(true);
			menu.getItem(1).setEnabled(true);
			menu.getItem(4).setEnabled(true);
		}else{
			menu.getItem(0).setEnabled(false);
			menu.getItem(1).setEnabled(false);
			menu.getItem(4).setEnabled(false);
		}
	}
	
	//  Esto abre el ActionBar contextual
	class ActionBarCallBack implements ActionMode.Callback {
		  
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        	switch(item.getItemId()){
        		case R.id.close_metronome:
        			s.Metronome_Stop();
        			mode.finish();
        			tempo = 120;
        			break;
        		
        		case R.id.metronome_menu_back:    
        			s.Metronome_Back();
        			break;
        			
        		case R.id.metronome_menu_pause:
        			PlayButton_Status(item);
        			PlayItemsControl(mode);
        			
        			Log.e("Data", "Item: " + item.toString() + ", Play: " + play + ", Stop: " + stop);
        			break;
        		
        		case R.id.metronome_menu_stop:
        			StopButton_Status(mode);
        			PlayItemsControl(mode);
                	   
                	Log.e("Data", "Item: " + item.toString() + ", Play: " + play + ", Stop: " + stop);  			       			        			
        			break;

        		case R.id.metronome_tempo:
        			metronome_options(tempo);
        			break;
        			
        		default:
        			break;
        	}
        	        	
            return true;
        }
  
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //mode.setTitle(R.string.metronome);
            mode.getMenuInflater().inflate(R.menu.metronome_menu, menu);
                        
        	MenuItem item = menu.findItem(R.id.metronome_tempo);
            item.setTitle("" + tempo);

            return true;
        }
  
        @Override
        public void onDestroyActionMode(ActionMode mode) {}
  
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        	if (stop)
        		return true;
        	else {
	        	play = true;
	        	stop = false;
	        	PlayItemsControl(mode);
	            return true;
        	}
        }
    }
}