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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides an application-specific customer identifier to either a {@link StoreCreditCardTag}
 * or {@link CreditCardTag}.
 *
 * @see  CreditCard#setCustomerId(java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class CustomerIdTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	public static final String TAG_NAME = "<payment:customerId>";

	public CustomerIdTag() {
	}

	@Override
	public int doStartTag() {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		PropertyHelper.setCardProperty(
			getBodyContent().getString().trim(),
			TAG_NAME,
			this,
			StoreCreditCardTag::setCustomerId,
			PaymentTag::setCreditCardCustomerId
		);
		return EVAL_PAGE;
	}
}
