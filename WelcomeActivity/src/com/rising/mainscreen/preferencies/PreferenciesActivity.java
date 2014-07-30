package com.rising.mainscreen.preferencies;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rising.drawing.R;
import com.rising.login.Configuration;
import com.rising.login.Login_Utils;
import com.rising.login.SessionManager;
import com.rising.mainscreen.MainScreenActivity;
import com.rising.mainscreen.preferencies.EraseAccount.OnTaskCompleted;

public class PreferenciesActivity extends Activity{

	private Button Logout, ChangePass, Terms_Conditions, Terms_Purchase, Delete_Account;
	private TextView Name, Mail, Credit;
	
	private Context ctx = this;		
	private int fid;
	private Dialog MDialog;
	private Preferencies_Utils UTILS;
	
	//Clases usadas
	private Configuration CONF;
	private SessionManager SESSION;
	
	//  Recibir la señal del proceso que elimina la cuenta
	private OnTaskCompleted listener = new OnTaskCompleted() {
	    public void onTaskCompleted(int details) {       
			switch (details) {
				case 1: {
					Toast.makeText(getApplicationContext(), 
							R.string.cuenta_eliminada, Toast.LENGTH_LONG).show();
					
					SESSION.LogOutUser();
					finish();
					break;
				}
				case 2: {
					Toast.makeText(getApplicationContext(), 
							R.string.error_eliminar_cuenta_fallo_verif, Toast.LENGTH_LONG).show();
					break;
				}
				case 3: {
					Toast.makeText(getApplicationContext(), 
							R.string.error_eliminar_cuenta_identidad, Toast.LENGTH_LONG).show();
					break;
				}
				default:
					Toast.makeText(getApplicationContext(), 
						R.string.error_eliminar_cuenta, Toast.LENGTH_LONG).show();
			}
			
			MDialog.dismiss();
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.preferencies_preferencieslayout);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ActionBar ABar = getActionBar();
    	ABar.setDisplayHomeAsUpEnabled(true);
		
		this.UTILS = new Preferencies_Utils(this);
		this.CONF = new Configuration(ctx);
		this.SESSION = new SessionManager(getApplicationContext());
    	
		Name = (TextView) findViewById(R.id.name_preferencies);
		Mail = (TextView) findViewById(R.id.mail_preferencies);
		Credit = (TextView) findViewById(R.id.credit_preferencies);
		Logout = (Button) findViewById(R.id.logout_button_preferencies);
		ChangePass = (Button) findViewById(R.id.changepass_preferencies);
		Terms_Conditions = (Button) findViewById(R.id.term_conditions_preferencies);
		Terms_Purchase = (Button) findViewById(R.id.term_purchase_preferencies);
		Delete_Account = (Button) findViewById(R.id.delete_account_preferencies);

		fid = SESSION.getFacebookId();
		
		ShowUser_Data();
		
		Logout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 LogoutButton_Actions();
 			}
		});

		ChangePass.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ChangePassButton_Actions();
			}
	
		});

		Terms_Conditions.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				UTILS.Legal_Displays(getString(R.string.terminos));
			}
			
		});

		Terms_Purchase.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				UTILS.Legal_Displays(getString(R.string.condiciones));
			}
			
		});

		Delete_Account.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				DeleteAccountButton_Actions();				
			}
			
		});
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			
		switch(item.getItemId()){
		
			case android.R.id.home:
				Intent i = new Intent(ctx, MainScreenActivity.class);
				startActivity(i);
				finish(); 
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void ShowUser_Data(){		
		Name.setText(Name.getText() + " " + CONF.getUserName());
		Mail.setText(Mail.getText() + " " + CONF.getUserEmail());
		Credit.setText(Credit.getText() + " " + CONF.getUserMoney());
		Credit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.money_ico, 0);
		
		if(fid != -1){
			ChangePass.setVisibility(View.GONE);
		}
	}
	
	private void LogoutButton_Actions(){
		
		if(fid > -1){
			SESSION.LogOutFacebook();	    			
		}else{
			SESSION.LogOutUser();
		}
		CONF.setUserEmail("");
		CONF.setUserId("");
		CONF.setUserMoney(0);
		CONF.setUserName("");
    	finish();
	}
	
	private void ChangePassButton_Actions(){
			        				    		
		new Login_Utils(ctx).Open_Fragment(ChangePassword_Fragment.class);

	}

	private void DeleteAccountButton_Actions(){
		MDialog = new Dialog(PreferenciesActivity.this, R.style.cust_dialog);
		MDialog.setContentView(R.layout.eliminar_cuenta);
		MDialog.setTitle(R.string.eliminar_cuenta);
		MDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		MDialog.show();
		
		final EditText clave = (EditText) MDialog.findViewById(R.id.claveEliminarCuenta);
		if (fid > -1) {
			clave.setVisibility(View.INVISIBLE);
			
			TextView texto = (TextView) MDialog.findViewById(R.id.textoEliminarCuenta);
			texto.setText(R.string.esta_seguro_facebook);
		}
		
		Button botonEliminarCuenta = (Button)MDialog.findViewById(R.id.botonEliminarCuenta);
	    botonEliminarCuenta.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(new Login_Utils(ctx).isOnline()){
					String mail = SESSION.getMail();
					
					//  Usuario normal
					if (clave.getText().length() > 0) {
						new EraseAccount(PreferenciesActivity.this, listener).execute(
								mail, clave.getText().toString());    
	        			clave.setText("");
					}
					else {
						
						//  Usuario de facebook
						if (fid > -1) {
							new EraseAccount(PreferenciesActivity.this, listener).execute(
									mail, fid + "");    
		        			clave.setText("");
						}
						
						else {
							Toast.makeText(getApplicationContext(), 
								R.string.err_campos_vacios, Toast.LENGTH_LONG).show();
						}
					}
				}else{
					Toast.makeText(ctx, R.string.connection_err, Toast.LENGTH_LONG).show();	
				}
			}
	    	
	    });
	}
	
}