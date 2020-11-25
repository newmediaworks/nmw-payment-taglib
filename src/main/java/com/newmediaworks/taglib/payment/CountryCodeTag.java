/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019, 2020  New Media Works
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
import com.aoindustries.creditcards.TransactionRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides a two-digit <a href="https://wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166-1 alpha-2</a> country code to a
 * {@link StoreCreditCardTag},
 * {@link CreditCardTag},
 * or {@link ShippingAddressTag}.
 *
 * @see  CreditCard#setCountryCode(java.lang.String)
 * @see  TransactionRequest#setShippingCountryCode(java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class CountryCodeTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:countryCode>";

	private static final long serialVersionUID = 1L;

	public CountryCodeTag() {
	}

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		PropertyHelper.setAddressProperty(
			getBodyContent().getString().trim(),
			TAG_NAME,
			this,
			StoreCreditCardTag::setCountryCode,
			PaymentTag::setCreditCardCountryCode,
			PaymentTag::setShippingAddressCountryCode
		);
		return EVAL_PAGE;
	}
}
