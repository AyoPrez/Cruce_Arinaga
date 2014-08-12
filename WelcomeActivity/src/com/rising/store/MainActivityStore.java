package com.rising.store;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.rising.drawing.R;
import com.rising.login.Configuration;
import com.rising.mainscreen.MainScreenActivity;
import com.rising.money.MoneyUpdateConnectionNetwork;
import com.rising.money.MoneyUpdateConnectionNetwork.OnFailMoney;
import com.rising.money.MoneyUpdateConnectionNetwork.OnUpdateMoney;
import com.rising.store.instruments.InstrumentFragment;
import com.rising.store.purchases.MyPurchases;
import com.rising.store.search.SearchStoreActivity;

//Clase principal de la tienda. Sirve de contenedor para el fragment de los instrumentos
public class MainActivityStore extends FragmentActivity implements OnQueryTextListener{

	//Variables
	public Context ctx;
	private ActionBar ABar;
	private String fragment_Complete;
	private String fragment_Name;
	public MenuItem item;	
	
	//Clases usadas
	private MoneyUpdateConnectionNetwork MONEY_ASYNCTASK;
	private Configuration CONF;
			
	private OnUpdateMoney MoneyUpdateSuccess = new OnUpdateMoney(){

		@Override
		public void onUpdateMoney() {							
			CONF.setUserMoney(MONEY_ASYNCTASK.devolverDatos());
			invalidateOptionsMenu();
		}
	};
	
	private OnFailMoney MoneyUpdateFail = new OnFailMoney(){

		@Override
		public void onFailMoney() {		
			Toast.makeText(ctx, getString(R.string.errcredit), Toast.LENGTH_LONG).show();
		}		
	};
				
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);				
		setContentView(R.layout.activity_main_store);		
		
		ctx = this;
		this.CONF = new Configuration(this);
		
		StartMoneyUpdate(CONF.getUserEmail());
    	
		ABar = getActionBar();
    	ABar.setIcon(R.drawable.ic_menu);
    	ABar.setTitle(R.string.store);
    	ABar.setDisplayHomeAsUpEnabled(true);
    	ABar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
    	     	    	
       	ABar.addTab(ABar.newTab().setText(R.string.piano).setTabListener(new TabListener(new InstrumentFragment(0))));
    	ABar.addTab(ABar.newTab().setText(R.string.guitar).setTabListener(new TabListener(new InstrumentFragment(1))));
    	ABar.addTab(ABar.newTab().setText(R.string.free).setTabListener(new TabListener(new InstrumentFragment(2))));
		
		ImageOptions();
   	}
	
	public void StartMoneyUpdate(String user){
		MONEY_ASYNCTASK = new MoneyUpdateConnectionNetwork(MoneyUpdateSuccess, MoneyUpdateFail, ctx);	
		MONEY_ASYNCTASK.execute(user);
	}
	
	@Override
	public void onBackPressed() {
	   Intent setIntent = new Intent(this, MainScreenActivity.class);
	   startActivity(setIntent);
	   finish();
	}
	
	public void ImageOptions(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.cover)
        .showImageForEmptyUri(R.drawable.cover)
        .showImageOnFail(R.drawable.cover)
        .cacheInMemory(true).considerExifParams(true)
        .displayer(new RoundedBitmapDisplayer(10)).build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.
		Builder(this).defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }   
	
	public void getFragment(Fragment fragment){
		this.fragment_Complete = fragment.toString();
		int i = fragment_Complete.indexOf('{');
		fragment_Name = fragment_Complete.substring(0, i);
	}
    	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		item = menu.findItem(R.id.money);
		String s = String.valueOf(CONF.getUserMoney());
		item.setTitle(s);
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_store, menu);
		
		//Crea el buscador
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search_store).getActionView();
	    searchView.setOnQueryTextListener(this);
	    	  	
	    //Crea y muestra el saldo del usuario
	    item = menu.findItem(R.id.money);    
	    item.setTitle("" + CONF.getUserMoney());
	    item.setIcon(R.drawable.money_ico);
	    
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	/*case R.id.money:
	    		Intent i = new Intent(this, MoneyActivity.class);
	    	    startActivity(i);
	    	    return true;*/
    	    
	    	case R.id.update_store:
	    		    		
	    		if(fragment_Name.equals("PianoFragment")){
	    			getFragmentManager().beginTransaction().replace(R.id.fragment_container, new InstrumentFragment(0)).commit();
	    		}else{
	    			if(fragment_Name.equals("GuitarFragment")){
	    				getFragmentManager().beginTransaction().replace(R.id.fragment_container, new InstrumentFragment(1)).commit();
	    			}else{
	    				if(fragment_Complete.equals("FreeFragment")){
	    					getFragmentManager().beginTransaction().replace(R.id.fragment_container, new InstrumentFragment(2)).commit();
	    				}else{
	    					Log.e("Error actualizar", "Falló al actualizar");
	    					Toast.makeText(getApplicationContext(), "Hubo un error en la actualización", Toast.LENGTH_SHORT).show();
	    				}
	    			}
	    		}
	    			    		
	    		return true;
	    		
	    	case R.id.my_purchases:
	    		Intent intent = new Intent(this, MyPurchases.class);
	    		startActivity(intent);
	    		finish();
	    		return true;
	        
	    	case android.R.id.home:
	    		Intent in = new Intent(this, MainScreenActivity.class);
	    		startActivity(in);
	    		finish();
	    	
	    	default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/*********************************Bloque de métodos de busqueda*****************************************/
	@Override
	public boolean onQueryTextSubmit(String text) {
		 
		Intent i = new Intent(this, SearchStoreActivity.class);
		Bundle b = new Bundle();
		b.putString("SearchText", text);
		i.putExtras(b);
		startActivity(i);		
		return false;
	}
		
	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
	/**************************************************************************/	
}