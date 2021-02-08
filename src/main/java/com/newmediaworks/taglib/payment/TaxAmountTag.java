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

import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.math.BigDecimal;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides the tax amount of the payment to a {@link PaymentTag}.
 *
 * @see  TransactionRequest#setTaxAmount(java.math.BigDecimal)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class TaxAmountTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:taxAmount>";

	public TaxAmountTag() {
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		String taxAmountString = getBodyContent().getString().trim();
		BigDecimal taxAmount;
		try {
			taxAmount = CurrencyUtil.parseCurrency(taxAmountString);
		} catch(NumberFormatException e) {
			throw new JspTagException("Invalid taxAmount: "+taxAmountString, e);
		}
		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class)
			.setTaxAmount(taxAmount);

		return EVAL_PAGE;
	}
}
