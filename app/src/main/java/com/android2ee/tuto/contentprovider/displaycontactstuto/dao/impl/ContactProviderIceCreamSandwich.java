/**<ul>
 * <li>DisplayContactsTuto</li>
 * <li>com.android2ee.tuto.contentprovider.displaycontactstuto.dao</li>
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
package com.android2ee.tuto.contentprovider.displaycontactstuto.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Instances;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;

import com.android2ee.tuto.contentprovider.displaycontactstuto.CursorTutoApplication;
import com.android2ee.tuto.contentprovider.displaycontactstuto.R;
import com.android2ee.tuto.contentprovider.displaycontactstuto.dao.ContentProviderIntf;
import com.android2ee.tuto.contentprovider.displaycontactstuto.model.SimpleContact;
import com.android2ee.tuto.contentprovider.displaycontactstuto.model.SimpleContact.SimplePhone;
import com.android2ee.tuto.contentprovider.displaycontactstuto.view.MainActivity;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to show how to use ContactsContrat with two exemples, :
 *        <ul>
 *        <li>the first one retrieve all the contact</li>
 *        <li>the second one retrieve only the contact with phone number</li>
 *        </ul>
 */
public enum ContactProviderIceCreamSandwich implements ContentProviderIntf {
	// make this class an singleton
	instance;

	/******************************************************************************************/
	/** find all the contacts and load them all **/
	/******************************************************************************************/

	/**
	 * Retrieve all the contacts of the phone
	 * 
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public List<SimpleContact> getAllContact() {
		// define the projection for retrieving contact
		String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER,
				ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID,
				ContactsContract.Contacts.PHOTO_THUMBNAIL_URI };

		// define the cursor that will retrieve the data
		Cursor contactCursor = CursorTutoApplication.getInstance().getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
		// define all the column index to retrieve the values
		int idColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
		int nameColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
		int hasPhoneNumColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int photoIdColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_ID);
		int photoThumbColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
		// Build a StringBuilder to log those elements
		String id, photoId, name, hasPhoneNum, photoThumb;
		// create the return list
		List<SimpleContact> result = new ArrayList<SimpleContact>(contactCursor.getCount());
		// the current contact
		SimpleContact current;
		// the log string
		String pString = CursorTutoApplication.getInstance().getResources().getString(R.string.param_message);

		while (contactCursor.moveToNext()) {
			// retrieve the values
			id = contactCursor.getString(idColIndex);
			photoId = contactCursor.getString(photoIdColIndex);
			// type=contactCursor.getString(typeColIndex);
			name = contactCursor.getString(nameColIndex);
			hasPhoneNum = contactCursor.getString(hasPhoneNumColIndex);
			photoThumb = contactCursor.getString(photoThumbColIndex);
			// create the simple contact
			current = new SimpleContact(id, "type", name, hasPhoneNum, photoId, photoThumb);
			// add it to the return list
			result.add(current);
			// make a log
			Log.v("ContactProvider", String.format(pString, id, name, "type", hasPhoneNum, photoId));
		}
		contactCursor.close();
		return result;
	}

	/******************************************************************************************/
	/** Hard implementation to find all the contacts with a telephone number and load them all **/
	/******************************************************************************************/

	/**
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public List<SimpleContact> getPhoneContact(boolean phoneContactOnly) {
		// first build the list with only the phone contact using getAll but filtering on
		// hasPhoneNum

		// first part:Find contact with phone number
		// ------------------------------------------
		// define the projection for retrieving contact
		String[] projectionInit = { ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER,
				ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID,
				ContactsContract.Contacts.PHOTO_THUMBNAIL_URI };
		// when you make a where clause you need to have the column in the projection
		String whereInit = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1' ";
		String sort = ContactsContract.Contacts.DISPLAY_NAME;
		// define the cursor that will retrieve the data
		Cursor contactCursor = CursorTutoApplication.getInstance().getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI, projectionInit, whereInit, null, sort);

		// then define the data:
		// The column index of the element to retrieve
		int nameColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
		int hasPhoneNumColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int photoIdColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_ID);
		int photoThumbColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
		int idColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
		// the data to retrieve
		String id, photoId, name, hasPhoneNum, photoThumb;

		// Second part:Find phone data associated to an user
		// ------------------------------------------
		// then make the query to retrieve phone numbers
		String[] projectionPhoneNumber = { ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL };
		// depending on the phoneContactOnly define the where close
		String whereId;
		if (phoneContactOnly) {
			whereId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE
					+ "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
		} else {
			whereId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? ";
		}
		// the define the string args
		String[] whereIdArg = new String[1];
		// the cursor
		Cursor phoneCursor = null;
		// the phone data
		int phoneColumnIndex, phoneTypeColIndex, phoneLabelColIndex;
		String phone = null, phoneType = null, phoneLabel = null;

		// Third part:Find account type associated to an user
		// ------------------------------------------
		// then find the type of the contact:
		String[] projectionAccount = { ContactsContract.Data.MIMETYPE };
		Cursor accountCursor = null;
		int accountColIndex;
		// the list of account of the user
		List<String> account = null;
		// the list of phone number of the user
		List<SimplePhone> phoneAndType = new ArrayList<SimpleContact.SimplePhone>();
		// create the return list
		List<SimpleContact> result = new ArrayList<SimpleContact>(contactCursor.getCount());
		// the current contact
		SimpleContact current;
		SimplePhone currentPhone;
		List<SimplePhone> curentPhones;
		// The string for the log
		// String pString =
		// CursorTutoApplication.getInstance().getResources().getString(R.string.param_message);
		// StringBuilder strb = new StringBuilder();
		// String eol = CursorTutoApplication.getInstance().getResources().getString(R.string.eol);
		// find the tumbnail

		while (contactCursor.moveToNext()) {
			// retrieve the values
			id = contactCursor.getString(idColIndex);
			photoId = contactCursor.getString(photoIdColIndex);
			// type=contactCursor.getString(typeColIndex);
			name = contactCursor.getString(nameColIndex);
			hasPhoneNum = contactCursor.getString(hasPhoneNumColIndex);
			photoThumb = contactCursor.getString(photoThumbColIndex);

			// then create the contact and add it to the list
			current = new SimpleContact(id, "type", name, hasPhoneNum, photoId, photoThumb);

			// then build the query to retrieve phone number
			// ----------------------------------------------
			whereIdArg[0] = "" + id;
			phoneCursor = CursorTutoApplication
					.getInstance()
					.getContentResolver()
					.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projectionPhoneNumber, whereId,
							whereIdArg, null);
			phoneColumnIndex = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
			phoneTypeColIndex = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE);
			phoneLabelColIndex = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL);
			phoneAndType.clear();
			curentPhones = new ArrayList<SimpleContact.SimplePhone>(phoneCursor.getCount());
			while (phoneCursor.moveToNext()) {
				phone = phoneCursor.getString(phoneColumnIndex);
				phoneType = getPhoneType(phoneCursor.getString(phoneTypeColIndex));
				phoneLabel = phoneCursor.getString(phoneLabelColIndex);
				// strb.append("phone: " + phone + ", type: " + getPhoneType(phoneType) +
				// ", label: " + phoneLabel + eol);
				currentPhone = current.new SimplePhone(phone, phoneType, phoneLabel);
				curentPhones.add(currentPhone);
			}
			phoneCursor.close();
			current.phoneAndType = curentPhones;
			// then build the query to retrieve account
			// ----------------------------------------------
			accountCursor = CursorTutoApplication.getInstance().getContentResolver()
					.query(ContactsContract.Data.CONTENT_URI, projectionAccount, whereId, whereIdArg, null);
			accountColIndex = accountCursor.getColumnIndex(ContactsContract.Data.MIMETYPE);

			account = new ArrayList<String>(accountCursor.getCount());
			while (accountCursor.moveToNext()) {
				account.add(accountCursor.getString(accountColIndex));
				// strb.append("account: " + accountCursor.getString(accountColIndex) + eol);
			}
			accountCursor.close();
			current.accounts = account;
			// Add the result to the list
			result.add(current);
			// make a log
			// Log.v("ContactProvider", String.format(pString, id, name, "type", hasPhoneNum,
			// photoId)
			// + "Num of phone numbers " + phoneAndType.size() + strb.toString());
			// strb = new StringBuilder();
		}
		// close cursors
		contactCursor.close();
		if (null != phoneCursor) {
			phoneCursor.close();
		}
		// then return the list
		return result;
	}

	/******************************************************************************************/
	/** Another implementation to find all the contacts with a telephone number and load them all **/
	/******************************************************************************************/

	/**
	 * Another implementation to find all the contacts with a telephone number and load them all
	 * 
	 * @param phoneContactOnly
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public List<SimpleContact> getPhoneContactFaster(boolean phoneContactOnly) {
		// first build the list with only the phone contact using getAll but filtering on
		// hasPhoneNum

		// first part:Find contact with phone number
		// ------------------------------------------
		// define the projection for retrieving contact
		// For postICS
		String[] projectionInit = { ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER,
				ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID,
				ContactsContract.Contacts.PHOTO_THUMBNAIL_URI };

		// when you make a where clause you need to have the column in the projection
		String whereInit = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1' ";
		String sort = ContactsContract.Contacts.DISPLAY_NAME;
		// define the cursor that will retrieve the data
		Cursor contactCursor = CursorTutoApplication.getInstance().getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI, projectionInit, whereInit, null, sort);

		// then define the data:
		// The column index of the element to retrieve
		int nameColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
		int hasPhoneNumColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int photoIdColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_ID);

		// For postICS
		int photoThumbColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
		int idColIndex = contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
		// the data to retrieve
		String id, photoId, name, hasPhoneNum, photoThumb;
		// create the return list
		List<SimpleContact> result = new ArrayList<SimpleContact>(contactCursor.getCount());
		// the current contact
		SimpleContact current;
		while (contactCursor.moveToNext()) {
			// retrieve the values
			id = contactCursor.getString(idColIndex);
			photoId = contactCursor.getString(photoIdColIndex);
			// type=contactCursor.getString(typeColIndex);
			name = contactCursor.getString(nameColIndex);
			hasPhoneNum = contactCursor.getString(hasPhoneNumColIndex);
			photoThumb = contactCursor.getString(photoThumbColIndex);
			// create the simple contact
			current = new SimpleContact(id, "type", name, hasPhoneNum, photoId, photoThumb);
			// add it to the return list
			result.add(current);
		}
		contactCursor.close();
		// then find the phone data
		findNumberTypeAndLabel(result, phoneContactOnly);
		// then find the type
		findType(result);
		// then return the list
		return result;
	}

	/**
	 * The list of Simple contact contains only SimpleContact(id, "type", name, hasPhoneNum,
	 * photoId, photoThumb);
	 * Now we want this method to fill the SimpleContact.SimplePhone(phone, phoneType, phoneLabel);
	 * 
	 * @param result
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void findNumberTypeAndLabel(List<SimpleContact> result, boolean phoneContactOnly) {
		// Second part:Find phone data associated to an user
		// ------------------------------------------
		// then make the query to retrieve phone numbers
		String[] projectionPhoneNumber = { ContactsContract.CommonDataKinds.Phone.NUMBER,// 0
				ContactsContract.CommonDataKinds.Phone.TYPE, // 1
				ContactsContract.CommonDataKinds.Phone.LABEL // 2
		};
		// depending on the phoneContactOnly define the where close
		String whereId;
		if (phoneContactOnly) {
			whereId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE
					+ "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
		} else {
			whereId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? ";
		}
		// the define the string args
		String[] whereIdArg = new String[1];
		// the cursor
		Cursor phoneCursor = null;
		// the phone data
		int phoneColumnIndex = 0, phoneTypeColIndex = 1, phoneLabelColIndex = 2;
		String phone = null, phoneType = null, phoneLabel = null;
		// the data to retrieve
		ArrayList<SimpleContact.SimplePhone> curentPhones;
		// then build the query to retrieve phone number
		// ----------------------------------------------
		for (SimpleContact simpleContact : result) {
			whereIdArg[0] = "" + simpleContact.id;
			phoneCursor = CursorTutoApplication
					.getInstance()
					.getContentResolver()
					.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projectionPhoneNumber, whereId,
							whereIdArg, null);
			curentPhones = new ArrayList<SimpleContact.SimplePhone>(phoneCursor.getCount());
			while (phoneCursor.moveToNext()) {
				phone = phoneCursor.getString(phoneColumnIndex);
				phoneType = getPhoneType(phoneCursor.getString(phoneTypeColIndex));
				phoneLabel = phoneCursor.getString(phoneLabelColIndex);
				// strb.append("phone: " + phone + ", type: " + getPhoneType(phoneType) +
				// ", label: " + phoneLabel + eol);
				curentPhones.add(simpleContact.new SimplePhone(phone, phoneType, phoneLabel));
				simpleContact.phoneAndType = curentPhones;
			}
		}
		if (phoneCursor != null) {
			phoneCursor.close();
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void findType(List<SimpleContact> result) {
		// Third part:Find account type associated to an user
		// ------------------------------------------
		// then find the type of the contact:
		String[] projectionAccount = { ContactsContract.Data.MIMETYPE };
		Cursor accountCursor = null;
		int accountColIndex;
		// the list of account of the user
		List<String> account = null;
		String whereId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? ";
		String[] whereIdArg = new String[1];
		for (SimpleContact simpleContact : result) {
			whereIdArg[0] = "" + simpleContact.id;
			// then build the query to retrieve account
			// ----------------------------------------------
			accountCursor = CursorTutoApplication.getInstance().getContentResolver()
					.query(ContactsContract.Data.CONTENT_URI, projectionAccount, whereId, whereIdArg, null);
			accountColIndex = accountCursor.getColumnIndex(ContactsContract.Data.MIMETYPE);

			account = new ArrayList<String>(accountCursor.getCount());
			while (accountCursor.moveToNext()) {
				account.add(accountCursor.getString(accountColIndex));
				// strb.append("account: " + accountCursor.getString(accountColIndex) + eol);
			}
			simpleContact.accounts = account;
		}
		if (accountCursor != null) {
			accountCursor.close();
		}

	}

	private String getPhoneType(String type) {
		int labelRes = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(Integer.parseInt(type));
		return CursorTutoApplication.getInstance().getResources().getString(labelRes);
	}

	/******************************************************************************************/
	/** Others usefull methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * This method Browse you contact data base and send an email to store that list as a text file
	 * 
	 * @param ctx
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void FindNamePhoneAndEmails(Context ctx) {
		StringBuilder strB = new StringBuilder("Sauvegarde des contacts\r\n");
		// Define the projection
		String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER,
				ContactsContract.Contacts.DISPLAY_NAME };
		// Make the query
		Cursor cursor = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null,
				null);
		// Find index column of data
		int indexId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		int indexHasPhoneNumber = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int indexName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		// So columnIndex for phone will be 0
		String[] projectionPhone = { ContactsContract.CommonDataKinds.Phone.NUMBER };
		// So columnIndex for phone will be 0
		String[] projectionEmail = { ContactsContract.CommonDataKinds.Email.DATA };
		// The data you want to retrieve
		String contactId, name, hasPhone, phoneNumber, emailAddress;
		// the cursor
		Cursor phones, emails;
		// Just to count
		int phoneNumberCount = 0, emailCount = 0, contactCount = 0;
		// Browse contacts data base
		while (cursor.moveToNext()) {
			contactCount++;
			// Find the id, the name and if it has a phone number
			contactId = cursor.getString(indexId);
			hasPhone = cursor.getString(indexHasPhoneNumber);
			name = cursor.getString(indexName);
			// If there is a phone number
			if (hasPhone.equalsIgnoreCase("1")) {
				// You know there is the number so now query it like this
				phones = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						projectionPhone, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null,
						null);
				// Browse the numbers of the contact
				while (phones.moveToNext()) {
					phoneNumber = phones.getString(0);
					phoneNumberCount++;
					strB.append(name);
					strB.append(" tel = ");
					strB.append(phoneNumber);
					strB.append("\r\n");
				}
				// Be sure to close Cursor
				phones.close();
			}
			// Query the contact’s emails
			emails = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
					projectionEmail, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
			// Browse them
			while (emails.moveToNext()) {
				// This would allow you get several email addresses
				emailAddress = emails.getString(0);
				// strB.append(name);strB.append(" mail= ");strB.append(emailAddress);strB.append("\r\n");
				emailCount++;
			}
			// Be sure to close Cursor
			emails.close();
		}
		// Be sure to close Cursor
		cursor.close();
		Log.e("ContactProvider", "telNumberCount==" + phoneNumberCount + " mailCount==" + emailCount
				+ " contactCount = " + contactCount);
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "mathias.seguy@android2ee.com" });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sauvegarde Telephone des contacts");
		emailIntent.putExtra(Intent.EXTRA_TEXT, strB.toString());
		ctx.startActivity(emailIntent);
	}

	/**
	 * Find the name of a specific number
	 * 
	 * @param ctx
	 * @param phoneNumber
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public String findNameFromNumber(Context ctx, String phoneNumber) {
		// First create the URI for the PhoneLookup
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

		// Define the query to retrieve its name
		Cursor people = ctx.getContentResolver().query(lookupUri, null, null, null, null);
		// find your column index for PhoneLookup Name
		int nameFieldColumnIndex = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
		// The name of the contact
		String contact = null;
		int phoneNumberCount = 0;
		// Browse your content
		while (people.moveToNext()) {
			contact = people.getString(nameFieldColumnIndex);
			Log.e("ContactProvider", "number==" + contact);
			phoneNumberCount++;
		}
		// Close your cursor
		people.close();
		Log.e("ContactProvider", "name ==" + contact + "telNumberCount==" + phoneNumberCount);
		return contact;
	}

	/**
	 * Find a note associated with an id of contacts
	 * 
	 * @param ctx
	 *            The context
	 * @param contactId
	 *            The contact id
	 * @return the note if exists
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private String getNote(Context ctx, long contactId) {
		// The data you want
		String note = null;
		// The projection
		String[] projection = new String[] { ContactsContract.CommonDataKinds.Note.NOTE };
		// The where clause
		String where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		// The where parameters
		String[] whereParameters = new String[] { Long.toString(contactId),
				ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
		// The query
		Cursor contacts = ctx.getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, where,
				whereParameters, null);
		// Browse the result
		if (contacts.moveToFirst()) {
			note = contacts.getString(0);
		}
		// Close the cursor
		contacts.close();
		return note;
	}

	/******************************************************************************************/
	/** MediaStorage test **************************************************************************/
	/******************************************************************************************/

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void browseMedia(Activity ctx) {
		// Define the URI for the external audio files
		Uri media = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		// Define the data you want to gather
		String[] projection = { MediaStore.Audio.Media._ID, // 0
				MediaStore.Audio.Media.ARTIST, // 1
				MediaStore.Audio.Media.TITLE, // 2
				MediaStore.Audio.Media.ALBUM }; // 3
		// Define the where clause (which audio files do you want)
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		// make the query
		Cursor cursor = ctx.getContentResolver().query(media, projection, selection, null, null);
		StringBuilder strbuilder = new StringBuilder();
		// browse the result of the query and do something with it:
		while (cursor.moveToNext()) {
			strbuilder.append("A new song from ");
			strbuilder.append(cursor.getString(1));
			strbuilder.append(" called ");
			strbuilder.append(cursor.getString(2));
			strbuilder.append(" in the Album ");
			strbuilder.append(cursor.getString(3));
			strbuilder.append("\r\n");
		}
		// Close the cursor always close the cursor
		cursor.close();
		// Display that list
		if (strbuilder.length() != 0) {
			Bundle mediaList = new Bundle();
			mediaList.putString(MainActivity.MEDIA, strbuilder.toString());
			ctx.showDialog(MainActivity.MEDIA_DIALOG, mediaList);
		}

	}

	/**
	 * Braowse the events of the user between 1 Januray 2014 and 31 December 2014
	 * 
	 * @param ctx
	 *            The context
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void browseCalendar(Context ctx) {
		String DEBUG_TAG = "ContactProvider Calendar Lookup";
		SparseArray<String> calendarsDescription = browseCalendarList(ctx);
		String[] INSTANCE_PROJECTION = new String[] { Instances.EVENT_ID, // 0
				Instances.BEGIN, // 1
				Instances.TITLE, // 2
				Instances.CALENDAR_ID // 3
		};

		// The indices for the projection array above.
		int PROJECTION_ID_INDEX = 0;
		int PROJECTION_BEGIN_INDEX = 1;
		int PROJECTION_TITLE_INDEX = 2;
		int CALENDAR_ID = 3;

		// Specify the date range you want to search for recurring
		// event instances
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2014, Calendar.JANUARY, 1, 8, 0);
		long startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(2014, Calendar.DECEMBER, 31, 8, 0);
		long endMillis = endTime.getTimeInMillis();

		Cursor cur = null;
		ContentResolver cr = ctx.getContentResolver();

		// The ID of the recurring event whose instances you are searching
		// for in the Instances table
		// String selection = Instances.EVENT_ID + " = ?";
		// String[] selectionArgs = new String[] { "207" };

		// Construct the query with the desired date range (buildUpon = Constructs a new builder,
		// copying the attributes from this Uri.).
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		// Submit the query
		cur = cr.query(builder.build(), INSTANCE_PROJECTION, null, null, null);

		String title = null;
		long eventID = 0;
		long beginVal = 0;
		int calendarId = 0;
		int eventCount = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		while (cur.moveToNext()) {
			// Get the field values
			eventID = cur.getLong(PROJECTION_ID_INDEX);
			beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
			title = cur.getString(PROJECTION_TITLE_INDEX);
			calendarId = cur.getInt(CALENDAR_ID);
			// Do something with the values.
			Log.e(DEBUG_TAG, "Event:  " + title + " from " + calendarsDescription.get(calendarId));
			calendar.setTimeInMillis(beginVal);
			Log.e(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));
			eventCount++;
		}
		// Close the cursor always close the cursor
		cur.close();
		Log.e(DEBUG_TAG, "Found " + eventCount);
	}

	/**
	 * Browse the user's calendars
	 * 
	 * @param ctx
	 *            The context
	 * @return An Sparse array that bind Calendar's id with a descrition of the calendar
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private SparseArray<String> browseCalendarList(Context ctx) {
		// The sparse array that link the calendar id and its description
		SparseArray<String> calendarsDescription = new SparseArray<String>();
		// The projection
		String[] EVENT_PROJECTION = new String[] { Calendars._ID, // 0
				Calendars.ACCOUNT_NAME, // 1
				Calendars.CALENDAR_DISPLAY_NAME, // 2
				Calendars.OWNER_ACCOUNT // 3
		};

		// The indices for the projection array above.
		int PROJECTION_ID_INDEX = 0;
		int PROJECTION_ACCOUNT_NAME_INDEX = 1;
		int PROJECTION_DISPLAY_NAME_INDEX = 2;
		int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
		// Run query
		Cursor cur = null;
		ContentResolver cr = ctx.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		// an exemple of selection, not used here
		// String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
		// + Calendars.ACCOUNT_TYPE + " = ?) AND ("
		// + Calendars.OWNER_ACCOUNT + " = ?))";
		// String[] selectionArgs = new String[] {"sampleuser@gmail.com", "com.google",
		// "sampleuser@gmail.com"};
		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
		// the data we want to retrieve
		int id = 0;
		String accountName, name, owner;
		while (cur.moveToNext()) {
			// Get the field values
			id = cur.getInt(PROJECTION_ID_INDEX);
			accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
			name = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
			owner = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

			// Do something with the values.
			calendarsDescription.put(id, "Calendar belongs to " + name + " with account " + accountName
					+ " and the owner is " + owner);
		}
		// Always close the cursor
		cur.close();
		// and return
		return calendarsDescription;
	}

}
