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
import com.aoindustries.encoding.MediaType;
import com.aoindustries.encoding.taglib.EncodingBufferedTag;
import com.aoindustries.io.buffer.BufferResult;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Provides the expiration month and year to either a {@link StoreCreditCardTag}
 * or {@link CreditCardTag}.
 *
 * @see  CreditCard#setExpirationMonth(byte)
 * @see  CreditCard#setExpirationYear(short)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ExpirationDateTag extends EncodingBufferedTag {

/* SimpleTag only: */
	public static final String TAG_NAME = "<payment:expirationDate>";
/**/

	@Override
	public MediaType getContentType() {
		return MediaType.TEXT;
	}

	@Override
	public MediaType getOutputType() {
		return null;
	}

/* BodyTag only:
	private static final long serialVersionUID = 1L;
/**/

	@Override
/* BodyTag only:
	protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
/* SimpleTag only: */
	protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
		String expirationDateString = capturedBody.trim().toString();
		int slashPos = expirationDateString.indexOf('/');
		if(slashPos==-1) throw new JspTagException("Invalid expirationDate, unable to find / : "+expirationDateString);

		String expirationMonthString = expirationDateString.substring(0, slashPos).trim();
		byte expirationMonth;
		try {
			expirationMonth = Byte.parseByte(expirationMonthString);
		} catch(NumberFormatException err) {
			throw new JspTagException("Invalid expirationMonth: "+expirationMonthString, err);
		}
		if(expirationMonth<1 || expirationMonth>12) throw new JspTagException("Invalid expirationMonth, must be between 1 and 12 inclusive: "+expirationMonth);

		String expirationYearString = expirationDateString.substring(slashPos+1).trim();
		short expirationYear;
		try {
			expirationYear = Short.parseShort(expirationYearString);
		} catch(NumberFormatException err) {
			throw new JspTagException("Invalid expirationYear: "+expirationYearString, err);
		}

		// Java 9: ifPresentOrElse
		Optional<StoreCreditCardTag> storeCreditCardTag = JspTagUtils.findAncestor(this, StoreCreditCardTag.class);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardTag.get().setExpirationMonth(expirationMonth);
			storeCreditCardTag.get().setExpirationYear(expirationYear);
		} else {
			CreditCardTag creditCardTag = JspTagUtils.requireAncestor(TAG_NAME, this, StoreCreditCardTag.TAG_NAME + " or " + CreditCardTag.TAG_NAME, CreditCardTag.class);
			PaymentTag paymentTag = JspTagUtils.requireAncestor(CreditCardTag.TAG_NAME, creditCardTag, PaymentTag.TAG_NAME, PaymentTag.class);
			paymentTag.setCreditCardExpirationMonth(expirationMonth);
			paymentTag.setCreditCardExpirationYear(expirationYear);
		}
/* BodyTag only:
		return EVAL_PAGE;
/**/
	}
}
