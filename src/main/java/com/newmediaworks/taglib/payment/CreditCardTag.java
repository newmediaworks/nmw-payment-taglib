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
import com.aoindustries.lang.Strings;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides the credit card details to a {@link PaymentTag}.
 *
 * @see  CreditCard
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class CreditCardTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:creditCard>";

	private static final long serialVersionUID = 2L;

	public void setCardNumber(String cardNumber) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setCardNumber(Strings.trimNullIfEmpty(cardNumber));
	}

	public void setMaskedCardNumber(String maskedCardNumber) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setMaskedCardNumber(Strings.trimNullIfEmpty(maskedCardNumber));
	}

	public void setExpirationMonth(byte expirationMonth) throws IllegalArgumentException, JspException {
		CreditCard.validateExpirationMonth(expirationMonth, false);
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setExpirationMonth(expirationMonth);
	}

	public void setExpirationYear(short expirationYear) throws IllegalArgumentException, JspException {
		CreditCard.validateExpirationYear(expirationYear, false);
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setExpirationYear(expirationYear);
	}

	public void setExpirationDate(String expirationDate) throws IllegalArgumentException, JspException {
		expirationDate = Strings.trimNullIfEmpty(expirationDate);
		if(expirationDate == null) {
			CreditCard creditCard = JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard();
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

			CreditCard creditCard = JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard();
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
		}
	}

	public void setCardCode(String cardCode) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setCardCode(Strings.trimNullIfEmpty(cardCode));
	}

	public void setCreditCardGUID(String creditCardGUID) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setProviderUniqueId(Strings.trimNullIfEmpty(creditCardGUID));
	}

	public void setFirstName(String firstName) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setFirstName(Strings.trimNullIfEmpty(firstName));
	}

	public void setLastName(String lastName) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setLastName(Strings.trimNullIfEmpty(lastName));
	}

	public void setCompanyName(String companyName) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setCompanyName(Strings.trimNullIfEmpty(companyName));
	}

	public void setEmail(String email) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setEmail(Strings.trimNullIfEmpty(email));
	}

	public void setPhone(String phone) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setPhone(Strings.trimNullIfEmpty(phone));
	}

	public void setFax(String fax) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setFax(Strings.trimNullIfEmpty(fax));
	}

	public void setCustomerId(String customerId) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setCustomerId(Strings.trimNullIfEmpty(customerId));
	}

	public void setCustomerTaxId(String customerTaxId) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setCustomerTaxId(Strings.trimNullIfEmpty(customerTaxId));
	}

	public void setStreetAddress1(String streetAddress1) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setStreetAddress1(Strings.trimNullIfEmpty(streetAddress1));
	}

	public void setStreetAddress2(String streetAddress2) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setStreetAddress2(Strings.trimNullIfEmpty(streetAddress2));
	}

	public void setCity(String city) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setCity(Strings.trimNullIfEmpty(city));
	}

	public void setState(String state) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setState(Strings.trimNullIfEmpty(state));
	}

	public void setPostalCode(String postalCode) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setPostalCode(Strings.trimNullIfEmpty(postalCode));
	}

	public void setCountryCode(String countryCode) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setCountryCode(Strings.trimNullIfEmpty(countryCode));
	}

	public void setComment(String comment) throws JspException {
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class).getCreditCard()
			.setComments(Strings.trimNullIfEmpty(comment));
	}

	@Override
	public int doStartTag() throws JspException {
		// Make sure nested in payment tag
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class);

		return EVAL_BODY_INCLUDE;
	}
}
