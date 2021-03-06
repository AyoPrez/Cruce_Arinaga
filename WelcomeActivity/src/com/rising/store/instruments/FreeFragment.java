package com.rising.store.instruments;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.rising.drawing.R;
import com.rising.login.Configuration;
import com.rising.login.login.ProgressDialogFragment;
import com.rising.store.CustomAdapter;
import com.rising.store.PartituraTienda;
import com.rising.store.instruments.AsyncTask_Instruments.OnTaskCompleted;
import com.rising.store.instruments.AsyncTask_Instruments.OnTaskUncomplete;
import com.rising.store.purchases.InfoBuyNetworkConnection;
import com.rising.store.purchases.InfoBuyNetworkConnection.OnTaskNoInfo;
import com.rising.store.purchases.InfoCompra;

public class FreeFragment extends Fragment{

	private static ArrayList<PartituraTienda> partiturasPiano = new ArrayList<PartituraTienda>();
	private static ArrayList<InfoCompra> ICompra = new ArrayList<InfoCompra>();
	
	private Context ctx;
	private View rootView;
	private int Instrumento = 2; //Free
	
	//Clases usadas
	private AsyncTask_Instruments ASYNCTASK;
	private Configuration CONF;
	private InfoBuyNetworkConnection INFOBUY;	
	private Store_Instruments_Utils UTILS;
			
	private OnTaskCompleted Success = new OnTaskCompleted() {
	    public void onTaskCompleted() {     	    		
	    	
	    	partiturasPiano = ASYNCTASK.devolverPartituras();
	    	ICompra = INFOBUY.devolverCompra();
	    	
	    	UTILS.MarcaPartituraComoComprada(partiturasPiano, ICompra);
	    	
	    	GridView pianoView = (GridView) rootView.findViewById(R.id.gV_piano_fragment);
	    	
		    pianoView.setAdapter(new CustomAdapter(getActivity(), partiturasPiano));
		    
		    onDestroyProgress();
	    }	
	};
			
	private OnTaskUncomplete Fail = new OnTaskUncomplete(){
		public void onTaskUncomplete(){
			ConnectionExceptionHandle();
		} 
	};
	
	private OnTaskNoInfo NoInfo = new OnTaskNoInfo(){

		@Override
		public void onTaskNoInfo() {
			ConnectionExceptionHandle();			
		}
	};
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return main(inflater, container, savedInstanceState);		
	}
	
	public View main(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
				
		rootView = inflater.inflate(R.layout.store_instruments_instrumentfragment, container, false);
					
		this.ctx = rootView.getContext();
		this.CONF = new Configuration(ctx);
		this.INFOBUY = new InfoBuyNetworkConnection(NoInfo);
		this.UTILS = new Store_Instruments_Utils();
		
		INFOBUY.execute(CONF.getUserId());
		onDestroyProgress();
		return rootView;	
	}
	
	public View errorView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		rootView = inflater.inflate(R.layout.store_errorlayout, container, false);
		
		return rootView;
	}
		
	@Override
	public void onResume() {
		super.onResume();
		this.setRetainInstance(true);	
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("myDialog");
	    if (prev != null) {
	      	ft.remove(prev);
	    }
	    ft.addToBackStack(null);  
	            
	    ProgressDialogFragment dialog = ProgressDialogFragment.newInstance(getString(R.string.pleasewait));
	    dialog.setCancelable(false);
	    dialog.show(ft, "myDialog");
				
		ASYNCTASK = new AsyncTask_Instruments(Fail, Success, Instrumento);
		ASYNCTASK.execute(Locale.getDefault().getDisplayLanguage());
	}
	
	@Override
	public void onPause() {
		super.onPause();	
		ASYNCTASK.cancel(true);
	}

	public void ConnectionExceptionHandle(){
		ASYNCTASK.cancel(true);
		onDestroyProgress();
		UTILS.OpenErrorFragment(getFragmentManager(),"Free");
	}
	
	public void onDestroyProgress() {
		try{
			ProgressDialogFragment dialog = (ProgressDialogFragment) getFragmentManager().findFragmentByTag("myDialog");
	     	
	        if (dialog != null) { 
	            dialog.dismiss();
	        }
		}catch(Exception e){
			Log.e("Exception", "Exception por aquí en FreeF: " + e.getMessage());
		}
	}
		
}