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
import com.aoindustries.lang.Strings;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Stores a credit card number to a bank-provided, CISP-compliant storage mechanism.
 *
 * @see  MerchantServicesProvider#storeCreditCard(com.aoindustries.creditcards.CreditCard)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class StoreCreditCardTag extends BodyTagSupport implements TryCatchFinally {

	public static final String TAG_NAME = "<payment:storeCreditCard>";

	/**
	 * The name of the request-scope attribute containing the current store credit card tag.
	 */
	private static final String REQUEST_ATTRIBUTE_NAME = StoreCreditCardTag.class.getName();

	// Java 9: module-private
	public static Optional<StoreCreditCardTag> getCurrent(ServletRequest request) {
		return Optional.ofNullable((StoreCreditCardTag)request.getAttribute(REQUEST_ATTRIBUTE_NAME));
	}
	// Java 9: module-private
	public static StoreCreditCardTag requireCurrent(String fromName, ServletRequest request) throws JspException {
		return getCurrent(request).orElseThrow(
			() -> new JspTagException(fromName + " must be within " + TAG_NAME)
		);
	}

	public StoreCreditCardTag() {
		init();
	}

	private static final long serialVersionUID = 2L;

	private transient boolean requestAttributeSet;

	private transient CreditCard creditCard;

	public void setCardNumber(String cardNumber) {
		creditCard.setCardNumber(Strings.trimNullIfEmpty(cardNumber));
	}

	public void setExpirationMonth(byte expirationMonth) {
		CreditCard.validateExpirationMonth(expirationMonth, false);
		creditCard.setExpirationMonth(expirationMonth);
	}

	public void setExpirationYear(short expirationYear) {
		CreditCard.validateExpirationYear(expirationYear, false);
		creditCard.setExpirationYear(expirationYear);
	}

	public void setExpirationDate(String expirationDate) {
		expirationDate = Strings.trimNullIfEmpty(expirationDate);
		if(expirationDate == null) {
			creditCard.setExpirationMonth(CreditCard.UNKNOWN_EXPIRATION_MONTH);
			creditCard.setExpirationYear(CreditCard.UNKNOWN_EXPIRATION_YEAR);
		} else {
			int slashPos = expirationDate.indexOf('/');
			if(slashPos == -1) throw new IllegalArgumentException("Invalid expirationDate, unable to find / : " + expirationDate);

			String expirationMonthString = expirationDate.substring(0, slashPos).trim();
			byte expirationMonth;
			try {
				expirationMonth = Byte.parseByte(expirationMonthString);
			} catch(NumberFormatException err) {
				throw new IllegalArgumentException("Invalid expirationMonth: " + expirationMonthString, err);
			}
			CreditCard.validateExpirationMonth(expirationMonth, false);

			String expirationYearString = expirationDate.substring(slashPos+1).trim();
			short expirationYear;
			try {
				expirationYear = Short.parseShort(expirationYearString);
			} catch(NumberFormatException err) {
				throw new IllegalArgumentException("Invalid expirationYear: " + expirationYearString, err);
			}
			CreditCard.validateExpirationYear(expirationYear, false);

			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
		}
	}

	public void setCardCode(String cardCode) {
		creditCard.setCardCode(Strings.trimNullIfEmpty(cardCode));
	}

	public void setFirstName(String firstName) {
		creditCard.setFirstName(Strings.trimNullIfEmpty(firstName));
	}

	public void setLastName(String lastName) {
		creditCard.setLastName(Strings.trimNullIfEmpty(lastName));
	}

	public void setCompanyName(String companyName) {
		creditCard.setCompanyName(Strings.trimNullIfEmpty(companyName));
	}

	public void setEmail(String email) {
		creditCard.setEmail(Strings.trimNullIfEmpty(email));
	}

	public void setPhone(String phone) {
		creditCard.setPhone(Strings.trimNullIfEmpty(phone));
	}

	public void setFax(String fax) {
		creditCard.setFax(Strings.trimNullIfEmpty(fax));
	}

	public void setCustomerId(String customerId) {
		creditCard.setCustomerId(Strings.trimNullIfEmpty(customerId));
	}

	public void setCustomerTaxId(String customerTaxId) {
		creditCard.setCustomerTaxId(Strings.trimNullIfEmpty(customerTaxId));
	}

	public void setStreetAddress1(String streetAddress1) {
		creditCard.setStreetAddress1(Strings.trimNullIfEmpty(streetAddress1));
	}

	public void setStreetAddress2(String streetAddress2) {
		creditCard.setStreetAddress2(Strings.trimNullIfEmpty(streetAddress2));
	}

	public void setCity(String city) {
		creditCard.setCity(Strings.trimNullIfEmpty(city));
	}

	public void setState(String state) {
		creditCard.setState(Strings.trimNullIfEmpty(state));
	}

	public void setPostalCode(String postalCode) {
		creditCard.setPostalCode(Strings.trimNullIfEmpty(postalCode));
	}

	public void setCountryCode(String countryCode) {
		creditCard.setCountryCode(Strings.trimNullIfEmpty(countryCode));
	}

	public void setComment(String comment) {
		creditCard.setComments(Strings.trimNullIfEmpty(comment));
	}

	private void init() {
		requestAttributeSet = false;
		creditCard = new CreditCard();
	}

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)request.getAttribute(Constants.processor);
		if(processor == null) throw new JspTagException("processor not set, please set processor with " + UseProcessorTag.TAG_NAME + " first");
		// Store this on the request
		if(getCurrent(request).isPresent()) throw new JspTagException(TAG_NAME + " may not be nested within " + TAG_NAME);
		request.setAttribute(REQUEST_ATTRIBUTE_NAME, this);
		requestAttributeSet = true;
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
			if(processor == null) throw new JspTagException("processor not set, please set processor with " + UseProcessorTag.TAG_NAME + " first");

			// Add credit card
			if(creditCard.getProviderId() == null) {
				creditCard.setProviderId(processor.getProviderId());
			}
			String providerUniqueId = processor.storeCreditCard(creditCard);

			// Write the unique ID
			pageContext.getOut().write(providerUniqueId);

			return EVAL_PAGE;
		} catch(IOException e) {
			throw new JspTagException(e);
		}
	}

	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	@Override
	public void doFinally() {
		if(requestAttributeSet) {
			pageContext.getRequest().removeAttribute(REQUEST_ATTRIBUTE_NAME);
		}
		init();
	}
}
