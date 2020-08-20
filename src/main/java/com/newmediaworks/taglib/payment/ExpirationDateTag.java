/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019  New Media Works
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
 * Provides the expiration month and year to either a {@link StoreCreditCardTag}
 * or {@link CreditCardTag}.
 *
 * @see  CreditCard#setExpirationMonth(byte)
 * @see  CreditCard#setExpirationYear(short)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ExpirationDateTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	public ExpirationDateTag() {
	}

	@Override
	public int doStartTag() {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		String expirationDateString = getBodyContent().getString().trim();
		int slashPos = expirationDateString.indexOf('/');
		if(slashPos==-1) throw new JspException("Invalid expirationDate, unable to find / : "+expirationDateString);

		String expirationMonthString = expirationDateString.substring(0, slashPos).trim();
		byte expirationMonth;
		try {
			expirationMonth = Byte.parseByte(expirationMonthString);
		} catch(NumberFormatException err) {
			throw new JspException("Invalid expirationMonth: "+expirationMonthString, err);
		}
		if(expirationMonth<1 || expirationMonth>12) throw new JspException("Invalid expirationMonth, must be between 1 and 12 inclusive: "+expirationMonth);

		String expirationYearString = expirationDateString.substring(slashPos+1).trim();
		short expirationYear;
		try {
			expirationYear = Short.parseShort(expirationYearString);
		} catch(NumberFormatException err) {
			throw new JspException("Invalid expirationYear: "+expirationYearString, err);
		}

		StoreCreditCardTag storeCreditCardTag = (StoreCreditCardTag)findAncestorWithClass(this, StoreCreditCardTag.class);
		if(storeCreditCardTag!=null) {
			storeCreditCardTag.setExpirationMonth(expirationMonth);
			storeCreditCardTag.setExpirationYear(expirationYear);
		} else {
			CreditCardTag creditCardTag = (CreditCardTag)findAncestorWithClass(this, CreditCardTag.class);
			if(creditCardTag!=null) {
				PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(creditCardTag, PaymentTag.class);
				if(paymentTag==null) throw new JspException("creditCard tag must be within payment tag");
				paymentTag.setCreditCardExpirationMonth(expirationMonth);
				paymentTag.setCreditCardExpirationYear(expirationYear);
			} else {
				throw new JspException("expirationDate tag must be within either a storeCreditCard tag or a creditCard tag");
			}
		}

		return EVAL_PAGE;
	}
}
