/**<ul>
 * <li>DisplayContactsTuto</li>
 * <li>com.android2ee.tuto.contentprovider.displaycontactstuto.view.adapters</li>
 * <li>15 sept. 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.tuto.contentprovider.displaycontactstuto.view.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android2ee.tuto.contentprovider.displaycontactstuto.BuildConfig;
import com.android2ee.tuto.contentprovider.displaycontactstuto.R;
import com.android2ee.tuto.contentprovider.displaycontactstuto.model.SimpleContact;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class ContactsAdapter extends ArrayAdapter<SimpleContact> {
	/******************************************************************************************/
	/** Holder **************************************************************************/
	/******************************************************************************************/

	/**
	 * @author Mathias Seguy (Android2EE)
	 * @goals
	 *        This class aims to optimize memory
	 *        static to save the reference to the outer class and to avoid access to
	 *        any members of the containing class
	 */
	static class ViewHolder {
		/**
		 * 
		 */
		public View associatedView;
		/**
		 * 
		 */
		public TextView name = null;
		/**
		 * 
		 */
		public TextView accounts = null;
		/**
		 * 
		 */
		public TextView phoneNumbers = null;
		/**
		 * 
		 */
		public ImageView picture = null;

		/**
		 * @param associatedView
		 */
		public ViewHolder(View associatedView) {
			super();
			this.associatedView = associatedView;
		}
	}

	/******************************************************************************************/
	/** ArrayAdpater attributes **************************************************************************/
	/******************************************************************************************/
	LayoutInflater mInflater;

	/******************************************************************************************/
	/** ArrayAdpater methods **************************************************************************/
	/******************************************************************************************/

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ContactsAdapter(Context context, List<SimpleContact> objects) {
		super(context, R.layout.contacts_adapter, objects);
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(context);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary calls
		// to findViewById() on each row.
		final SimpleContact contact = getItem(position);
		View rowView = convertView;
		// if convertView==null built it an initialize the associated holder
		if (null == rowView) {
			rowView = mInflater.inflate(R.layout.contacts_adapter, null);
			// create and link the holder with the view
			ViewHolder viewHolder = new ViewHolder(rowView);
			// instanciate elements of the holder
			viewHolder.name = (TextView) rowView.findViewById(R.id.contact_name);
			viewHolder.accounts = (TextView) rowView.findViewById(R.id.contact_account);
			viewHolder.phoneNumbers = (TextView) rowView.findViewById(R.id.contact_number);
			viewHolder.picture = (ImageView) rowView.findViewById(R.id.contact_pict);
			// link the view with its holder
			rowView.setTag(viewHolder);
		}
		// find the Holder associated with the view
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		// set the name
		holder.name.setText(contact.name);
		holder.accounts.setText(contact.getAccounts());
		holder.phoneNumbers.setText(contact.getNumbers());
		Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact.id);
		// only for api level 14
		InputStream is=null;
		if(Build.VERSION.SDK_INT>Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		 is = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(),
				contactUri, false);
		}
		
		// else ?o? something like that have to be tried but it should be done in the provider
		// ImageView imageView = (ImageView) view.findViewById(R.id.contact_image);
		//
		// int id = _cursor.getColumnIndex(People._ID);
		// Uri uri = ContentUris.withAppendedId(People.CONTENT_URI, _cursor.getLong(id));
		//
		// Bitmap bitmap = People.loadContactPhoto(_context, uri, R.drawable.icon, null);
		//
		// imageView.setImageBitmap(bitmap);
		//
		// super.bindView(view, context, cursor);
		// set the picture
		if (is != null) {
			Bitmap speakerBitmap = BitmapFactory.decodeStream(is);
			holder.picture.setImageBitmap(speakerBitmap);
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			is = null;
		} else {
			holder.picture.setImageResource(R.drawable.ic_john_do);
		}

		return rowView;
	}

}
