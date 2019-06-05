package com.android2ee.tuto.contentprovider.displaycontactstuto.view;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.android2ee.tuto.contentprovider.displaycontactstuto.R;
import com.android2ee.tuto.contentprovider.displaycontactstuto.dao.DaoFactory;
import com.android2ee.tuto.contentprovider.displaycontactstuto.model.SimpleContact;
import com.android2ee.tuto.contentprovider.displaycontactstuto.view.adapters.ContactsAdapter;

public class MainActivity extends Activity {
	/**
	 * To display the alertdialog that displays all the media of the device
	 */
	public final static int MEDIA_DIALOG=131274;
	/**
	 * To display the alertdialog that displays all the media of the device
	 */
	public final static String MEDIA="MEDIA_LIST";
	/**
	 * The list that contains all the contact
	 */
	List<SimpleContact> contactsList;
	ContactsAdapter contactAdapter;
	ListView contactsLV;
	TextView txvHello;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txvHello=(TextView) findViewById(R.id.txvHello);	
		//see media
		DaoFactory.getContactProvider(this).browseMedia(this);
		//see calendar events
		DaoFactory.getContactProvider(this).browseCalendar(this);
		//load data
		txvHello.setText(txvHello.getText()+"\r\n"+" Try to load data");
		(new MyAsyncTask(this)).execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int, android.os.Bundle)
	 */
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		if(id==MEDIA_DIALOG) {
			return buildMediaDialog(args);
		}
		return super.onCreateDialog(id, args);
	}
	
	/**
	 * Build the AlertDialog that displays the list returned by the 
	 * @param args
	 * @return
	 */
	private AlertDialog buildMediaDialog(Bundle args) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Media Store list of elements:");
		alertDialog.setMessage(args.getString(MEDIA, "empty"));
		alertDialog.setPositiveButton("Close !!", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return alertDialog.create();
	}

	/**
	 * @author Mathias Seguy (Android2EE)
	 * @goals
	 * This class aims to make the call in database asynchronous
	 */
	public class MyAsyncTask extends AsyncTask<Void, Integer, List<SimpleContact>> {
		/******************************************************************************************/
		/** Attributes **************************************************************************/
		/******************************************************************************************/


		/**
		 * The calling activity
		 */
		private MainActivity activity;
		

		/**
		 * The logCat's tag
		 */
		private String tag = "MyAsyncTask";
		

		/******************************************************************************************/
		/** Constructors **************************************************************************/
		/******************************************************************************************/

		/**
		 * @param activity
		 */
		public MyAsyncTask(MainActivity activity) {
			super();
			this.activity = activity;
		}

		/******************************************************************************************/
		/** Threatment method **************************************************************************/
		/******************************************************************************************/

		// override of the method doInBackground (the one which is running in a separate thread)
		@Override
		protected List<SimpleContact> doInBackground(Void... unused) {
			
			//and load the contacts elements
			return DaoFactory.getContactProvider(activity).getPhoneContactFaster(true);
		}

		// override of the onProgressUpdate method (runs in the GUI thread)
		@Override
		protected void onProgressUpdate(Integer... diff) {
			
		}

		// override of the onPostExecute method (runs in the GUI thread)
		@Override
		protected void onPostExecute(List<SimpleContact> message) {

			txvHello.setText(txvHello.getText()+"\r\n"+" Data loaded contact size :"+message.size());
			
			contactsList=message;
			

			contactAdapter=new ContactsAdapter(activity, contactsList);
			contactsLV=(ListView)findViewById(R.id.contactslist);
			contactsLV.setAdapter(contactAdapter);
			
		}
	}
}
