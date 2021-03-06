package com.rising.mainscreen.preferencies;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.rising.drawing.R;
import com.rising.login.SessionManager;
import com.rising.mainscreen.MainScreenActivity;

//Clase que maneja los errores en la pantalla de preferencia
public class Preferencies_Errors {
	
	private Context ctx;
	private Dialog EDialog;
	
	public Preferencies_Errors(Context context){
		this.ctx = context;
	}
	
	public void errChangePass(int code){
    	
    	EDialog = new Dialog(ctx, R.style.cust_dialog);
    	EDialog.getWindow();
        EDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		EDialog.setContentView(R.layout.error_errordialog);
		EDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		TextView tv_E = (TextView)EDialog.findViewById(R.id.error_tV);
				
		switch (code) {
			case 1: {
				tv_E.setText(R.string.mis_datos_clave_cambiada);
				break;
			}
			case 2: {
				tv_E.setText(R.string.mis_datos_error_verif);
				break;
			}
			case 3: {
				tv_E.setText(R.string.mis_datos_clave_erronea);
				break;
			}
			case 4: {
				tv_E.setText(R.string.err_login_unknown);
				break;
			}
			case 5: 
				tv_E.setText(R.string.err_campos_vacios);
				break;
			case 6:
				tv_E.setText(R.string.err_pass);
				break;
			case 7:
				tv_E.setText(R.string.connection_err);
				break;
			default:
				tv_E.setText(R.string.mis_datos_clave_cambiada);
		}
		
		Button  Login_Error_Close_Button = (Button)EDialog.findViewById(R.id.error_button);
		
		Login_Error_Close_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {		
				EDialog.dismiss();				
			}
		});
    	
		EDialog.show();	
    }

	public void errDeleteAccount(int code){
		
    	EDialog = new Dialog(ctx, R.style.cust_dialog);
    	EDialog.getWindow();
        EDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		EDialog.setContentView(R.layout.error_errordialog);
		EDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		TextView tv_E = (TextView)EDialog.findViewById(R.id.error_tV);
		
		switch (code) {
			case 0:
				tv_E.setText(R.string.err_campos_vacios);
				break;
			
			case 1: {
				tv_E.setText(R.string.cuenta_eliminada);
				 
				new SessionManager(ctx).LogOutUser();
				((Activity)ctx).finish();
				break;
			}
			case 2:
				tv_E.setText(R.string.error_eliminar_cuenta_fallo_verif);
				break;
			case 3: 
				tv_E.setText(R.string.error_eliminar_cuenta_identidad);
				break;
			case 4:
				tv_E.setText(R.string.connection_err);
				break;
			case 5:
				break;
			default:
				tv_E.setText(R.string.error_eliminar_cuenta);
		}
		
		Button  Login_Error_Close_Button = (Button)EDialog.findViewById(R.id.error_button);
		
		Login_Error_Close_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {		
				EDialog.dismiss();				
			}
		});
    	
		EDialog.show();	
	}

	public void errFeedback(final int code){
		
    	EDialog = new Dialog(ctx, R.style.cust_dialog);
    	EDialog.getWindow();
        EDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		EDialog.setContentView(R.layout.error_errordialog);
		EDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		TextView tv_E = (TextView)EDialog.findViewById(R.id.error_tV);
				
		switch(code){
			case 0:
				tv_E.setText(R.string.err_campos_vacios);
				break;
			case 1:
				tv_E.setText(R.string.feedback_enviado);
				break;
			case 2:
				tv_E.setText(R.string.connection_err);
				break;
			default:
				tv_E.setText(R.string.err_login_unknown);
				break;
		}
		
		Button  Login_Error_Close_Button = (Button)EDialog.findViewById(R.id.error_button);
		
		Login_Error_Close_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(code == 1){
					Intent i = new Intent(ctx, MainScreenActivity.class);
					ctx.startActivity(i);
					((Activity)ctx).finish();
				}else{
					EDialog.dismiss();
				}
			}
		});
		
		EDialog.show();	
	}

}
