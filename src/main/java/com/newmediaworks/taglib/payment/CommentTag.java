/*
 * new-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019  New Media Works
 *     info@newmediaworks.com
 *     703 2nd Street #465
 *     Santa Rosa, CA 95404
 *
 * This file is part of new-payment-taglib.
 *
 * new-payment-taglib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * new-payment-taglib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with new-payment-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.payment;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides a comment to either a <code>StoreCreditCardTag</code>, <code>CreditCardTag</code>, or <code>PaymentTag</code>.
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class CommentTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	public CommentTag() {
	}

	@Override
	public int doStartTag() {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		String comment = getBodyContent().getString().trim();
		StoreCreditCardTag storeCreditCardTag = (StoreCreditCardTag)findAncestorWithClass(this, StoreCreditCardTag.class);
		if(storeCreditCardTag!=null) {
			storeCreditCardTag.setComment(comment);
		} else {
			CreditCardTag creditCardTag = (CreditCardTag)findAncestorWithClass(this, CreditCardTag.class);
			if(creditCardTag!=null) {
				PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(creditCardTag, PaymentTag.class);
				if(paymentTag==null) throw new JspException("creditCard tag must be within payment tag");
				paymentTag.setCreditCardComment(comment);
			} else {
				PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(this, PaymentTag.class);
				if(paymentTag!=null) {
					paymentTag.setComment(comment);
				} else {
					throw new JspException("comment tag must be within a storeCreditCard tag, creditCard tag, or payment tag");
				}
			}
		}

		return EVAL_PAGE;
	}
}