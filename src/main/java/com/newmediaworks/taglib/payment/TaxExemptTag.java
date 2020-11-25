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

import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides the tax exempt flag to a {@link PaymentTag}.
 *
 * @see  TransactionRequest#setTaxExempt(boolean)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class TaxExemptTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:taxExempt>";

	private static final long serialVersionUID = 1L;

	public TaxExemptTag() {
	}

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		String taxExemptString = getBodyContent().getString().trim();
		boolean taxExempt;
		if("true".equals(taxExemptString)) taxExempt = true; // TODO: case-insensitive here and review other places
		else if("false".equals(taxExemptString)) taxExempt = false;
		else throw new JspTagException("Invalid value for taxExempt, should be either true or false: "+taxExemptString); // TODO: TAG_NAME in this message, too?  Review others if so

		JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class)
			.setTaxExempt(taxExempt);

		return EVAL_PAGE;
	}
}
