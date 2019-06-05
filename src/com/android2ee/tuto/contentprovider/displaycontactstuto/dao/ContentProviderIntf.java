/**<ul>
 * <li>DisplayContactsTuto</li>
 * <li>com.android2ee.tuto.contentprovider.displaycontactstuto.dao</li>
 * <li>8 sept. 2014</li>
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
package com.android2ee.tuto.contentprovider.displaycontactstuto.dao;

import java.util.List;

import org.apache.http.MethodNotSupportedException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;

import com.android2ee.tuto.contentprovider.displaycontactstuto.model.SimpleContact;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to declare the methods that canj be called to retrieve contact/media or calendar events
 */
public interface ContentProviderIntf {
	/**
	 * Retrieve all the contacts of the phone
	 * 
	 * @return
	 */
	public List<SimpleContact> getAllContact();

	/**
	 * Retrieve the list of all the contact with or without a phone number
	 * 
	 * @param phoneContactOnly
	 * @return
	 */
	public List<SimpleContact> getPhoneContact(boolean phoneContactOnly);

	/**
	 * Another implementation to find all the contacts with a telephone number and load them all
	 * 
	 * @param phoneContactOnly
	 * @return
	 */
	public List<SimpleContact> getPhoneContactFaster(boolean phoneContactOnly);

	/**
	 * This method Browse you contact data base and send an email to store that list as a text file
	 * 
	 * @param ctx
	 */
	public void FindNamePhoneAndEmails(Context ctx);

	/**
	 * Find the name of a specific number
	 * 
	 * @param ctx
	 * @param phoneNumber
	 */
	public String findNameFromNumber(Context ctx, String phoneNumber);

	/**
	 * Browse the Media of the device
	 * @param Activity
	 */
	public void browseMedia(Activity ctx);

	/**
	 * Browse the events of the user between 1 Januray 2014 and 31 December 2014
	 * 
	 * @param ctx
	 *            The context
	 */
	public void browseCalendar(Context ctx) ;


}
