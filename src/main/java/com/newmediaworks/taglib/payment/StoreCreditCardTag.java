/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019, 2020, 2021  New Media Works
 *     info@newmediaworks.com
 *     703 2nd Street #465
 *     Santa Rosa, CA 95404
 *
 * This file is part of nmw-payment-taglib.
 *
 * nmw-payment-taglib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * nmw-payment-taglib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with nmw-payment-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.payment;

import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.MerchantServicesProvider;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Stores a credit card number to a bank-provided, CISP-compliant storage mechanism.
 *
 * @see  MerchantServicesProvider#storeCreditCard(com.aoindustries.creditcards.CreditCard)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class StoreCreditCardTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:storeCreditCard>";

	private static final long serialVersionUID = 2L;

	// Set by nested tags
	private transient String cardNumber;
	private transient byte expirationMonth;
	private transient short expirationYear;
	private transient String cardCode;
	private transient String firstName;
	private transient String lastName;
	private transient String companyName;
	private transient String email;
	private transient String phone;
	private transient String fax;
	private transient String customerId;
	private transient String customerTaxId;
	private transient String streetAddress1;
	private transient String streetAddress2;
	private transient String city;
	private transient String state;
	private transient String postalCode;
	private transient String countryCode;
	private transient String comment;

	public StoreCreditCardTag() {
		init();
	}

	@Override
	public int doStartTag() throws JspException {
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
		if(processor==null) throw new JspTagException("processor not set, please set processor with the useProcessor tag first");
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Stores the card at end tag because values are provided by nested tags.
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			// Get the current processor
			MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
			if(processor==null) throw new JspTagException("processor not set, please set processor with the useProcessor tag first");

			// Add credit card
			String providerUniqueId = processor.storeCreditCard(
				new CreditCard(
					null,
					null,
					null,
					processor.getProviderId(),
					null,
					cardNumber,
					null,
					expirationMonth,
					expirationYear,
					cardCode,
					firstName,
					lastName,
					companyName,
					email,
					phone,
					fax,
					customerId,
					customerTaxId,
					streetAddress1,
					streetAddress2,
					city,
					state,
					postalCode,
					countryCode,
					comment
				)
			);

			// Write the unique ID
			pageContext.getOut().write(providerUniqueId);

			// Get ready for the next iteration
			return EVAL_PAGE;
		} catch(IOException e) {
			throw new JspTagException(e);
		} finally {
			init();
		}
	}

	@Override
	public void release() {
		super.release();
		init();
	}

	private void init() {
		cardNumber=null;
		expirationMonth=(byte)-1;
		expirationYear=(short)-1;
		cardCode=null;
		firstName=null;
		lastName=null;
		companyName=null;
		email=null;
		phone=null;
		fax=null;
		customerId=null;
		customerTaxId=null;
		streetAddress1=null;
		streetAddress2=null;
		city=null;
		state=null;
		postalCode=null;
		countryCode=null;
		comment=null;
	}

	void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	void setExpirationMonth(byte expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	void setExpirationYear(short expirationYear) {
		this.expirationYear = expirationYear;
	}

	void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	void setLastName(String lastName) {
		this.lastName = lastName;
	}

	void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	void setEmail(String email) {
		this.email = email;
	}

	void setPhone(String phone) {
		this.phone = phone;
	}

	void setFax(String fax) {
		this.fax = fax;
	}

	void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	void setCustomerTaxId(String customerTaxId) {
		this.customerTaxId = customerTaxId;
	}

	void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}

	void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	void setCity(String city) {
		this.city = city;
	}

	void setState(String state) {
		this.state = state;
	}

	void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	void setComment(String comment) {
		this.comment = comment;
	}
}
