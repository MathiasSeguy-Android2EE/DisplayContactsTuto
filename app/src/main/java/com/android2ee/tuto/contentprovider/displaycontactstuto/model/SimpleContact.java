/**<ul>
 * <li>DisplayContactsTuto</li>
 * <li>com.android2ee.tuto.contentprovider.displaycontactstuto.model</li>
 * <li>12 sept. 2012</li>
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
package com.android2ee.tuto.contentprovider.displaycontactstuto.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.ContactsContract;

import com.android2ee.tuto.contentprovider.displaycontactstuto.CursorTutoApplication;
import com.android2ee.tuto.contentprovider.displaycontactstuto.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class SimpleContact {
	public int id;
	public String type;
	public String name;
	public Boolean hasPhoneNumber;
	public int photoId;
	public URI thumbnailUri;
	public Uri realPicture;
	public List<SimplePhone> phoneAndType;
	public List<String> accounts;
	String eol = CursorTutoApplication.getInstance().getResources().getString(R.string.eol);

	public SimpleContact(String cId, String cType, String cName, String cHasPhone, String cPhotoId, String cThum) {
		id = Integer.parseInt(cId);
		type = cType;
		name = cName;
		hasPhoneNumber = Boolean.parseBoolean(cHasPhone);
		if (null != cPhotoId) {
			photoId = Integer.parseInt(cPhotoId);
			try {
				thumbnailUri = new URI(cThum);
				Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, photoId);
				realPicture = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getAccounts() {
		StringBuilder strb = new StringBuilder();
		if (accounts != null) {
			for (String account : accounts) {
				strb.append(account.split("/")[1]);
				strb.append(",");
			}
		}
		return strb.toString();
	}

	public String getNumbers() {
		StringBuilder strb = new StringBuilder();
		if (phoneAndType != null) {
			for (SimplePhone sphone : phoneAndType) {
				strb.append(sphone.number);
				strb.append("[");
				strb.append(sphone.type);
				if (sphone.label != null) {
					strb.append(",");
					strb.append(sphone.label);
				}
				strb.append("]");
				strb.append(eol);
			}
		}
		return strb.toString();
	}

	public class SimplePhone {
		public String number;
		public String type;
		public String label;

		/**
		 * @param number
		 * @param type
		 * @param label
		 */
		public SimplePhone(String number, String type, String label) {
			super();
			this.number = number;
			this.type = type;
			this.label = label;
		}
	}

}
