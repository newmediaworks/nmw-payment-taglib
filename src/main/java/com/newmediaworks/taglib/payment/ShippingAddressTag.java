/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
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

import com.aoapps.lang.Strings;
import com.aoapps.payments.TransactionRequest;
import com.aoapps.servlet.attribute.ScopeEE;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Provides the shipping details to a {@link PaymentTag}.
 *
 * @see  TransactionRequest#setShippingFirstName(java.lang.String)
 * @see  TransactionRequest#setShippingLastName(java.lang.String)
 * @see  TransactionRequest#setShippingCompanyName(java.lang.String)
 * @see  TransactionRequest#setShippingStreetAddress1(java.lang.String)
 * @see  TransactionRequest#setShippingStreetAddress2(java.lang.String)
 * @see  TransactionRequest#setShippingCity(java.lang.String)
 * @see  TransactionRequest#setShippingState(java.lang.String)
 * @see  TransactionRequest#setShippingPostalCode(java.lang.String)
 * @see  TransactionRequest#setShippingCountryCode(java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ShippingAddressTag extends BodyTagSupport implements TryCatchFinally {

	public static final String TAG_NAME = "<payment:shippingAddress>";

	/**
	 * The name of the request-scope attribute containing the current shipping address tag.
	 */
	private static final ScopeEE.Request.Attribute<ShippingAddressTag> REQUEST_ATTRIBUTE_NAME =
		ScopeEE.REQUEST.attribute(ShippingAddressTag.class.getName());

	// Java 9: module-private
	public static Optional<ShippingAddressTag> getCurrent(ServletRequest request) {
		return Optional.ofNullable(REQUEST_ATTRIBUTE_NAME.context(request).get());
	}
	// Java 9: module-private
	public static ShippingAddressTag requireCurrent(String fromName, ServletRequest request) throws JspException {
		return getCurrent(request).orElseThrow(
			() -> new JspTagException(fromName + " must be within " + TAG_NAME)
		);
	}

	public ShippingAddressTag() {
		init();
	}

	private static final long serialVersionUID = 1L;

	private transient boolean requestAttributeSet;

	public void setFirstName(String firstName) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingFirstName(Strings.trimNullIfEmpty(firstName));
	}

	public void setLastName(String lastName) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingLastName(Strings.trimNullIfEmpty(lastName));
	}

	public void setCompanyName(String companyName) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingCompanyName(Strings.trimNullIfEmpty(companyName));
	}

	public void setStreetAddress1(String streetAddress1) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingStreetAddress1(Strings.trimNullIfEmpty(streetAddress1));
	}

	public void setStreetAddress2(String streetAddress2) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingStreetAddress2(Strings.trimNullIfEmpty(streetAddress2));
	}

	public void setCity(String city) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingCity(Strings.trimNullIfEmpty(city));
	}

	public void setState(String state) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingState(Strings.trimNullIfEmpty(state));
	}

	public void setPostalCode(String postalCode) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingPostalCode(Strings.trimNullIfEmpty(postalCode));
	}

	public void setCountryCode(String countryCode) throws JspException {
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getTransactionRequest()
			.setShippingCountryCode(Strings.trimNullIfEmpty(countryCode));
	}

	private void init() {
		requestAttributeSet = false;
	}

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		// Make sure nested in payment tag
		PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest());
		// Store this on the request
		if(getCurrent(request).isPresent()) throw new JspTagException(TAG_NAME + " may not be nested within " + TAG_NAME);
		REQUEST_ATTRIBUTE_NAME.context(request).set(this);
		requestAttributeSet = true;
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	@Override
	public void doFinally() {
		if(requestAttributeSet) {
			REQUEST_ATTRIBUTE_NAME.context(pageContext.getRequest()).remove();
		}
		init();
	}
}
