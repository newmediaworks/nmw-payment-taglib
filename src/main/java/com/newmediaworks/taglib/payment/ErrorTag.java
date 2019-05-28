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

import com.aoindustries.creditcards.TransactionResult;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The body of this tag will be processed when there was any type of error processing the
 * {@link PaymentTag},
 * {@link CaptureTag},
 * or {@link VoidTag}.
 *
 * @see  AuthorizationResult#getCommunicationResult()
 * @see  TransactionResult.CommunicationResult#LOCAL_ERROR
 * @see  TransactionResult.CommunicationResult#IO_ERROR
 * @see  TransactionResult.CommunicationResult#GATEWAY_ERROR
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ErrorTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	public ErrorTag() {
	}

	/**
	 * Gets the transaction result from one of the outer payment, capture, or void tags.
	 */
	TransactionResult getTransactionResult() throws JspException {
		PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(this, PaymentTag.class);
		if(paymentTag!=null) return paymentTag.getAuthorizationResult();

		CaptureTag captureTag = (CaptureTag)findAncestorWithClass(this, CaptureTag.class);
		if(captureTag!=null) return captureTag.getCaptureResult();

		VoidTag voidTag = (VoidTag)findAncestorWithClass(this, VoidTag.class);
		if(voidTag!=null) return voidTag.getVoidResult();

		throw new JspException("error tag must be within a payment, capture, or void tag");
	}

	@Override
	public int doStartTag() throws JspException {
		TransactionResult transactionResult = getTransactionResult();

		switch(transactionResult.getCommunicationResult()) {
			case LOCAL_ERROR:
			case IO_ERROR:
			case GATEWAY_ERROR:
				return EVAL_BODY_INCLUDE;
			case SUCCESS:
				return SKIP_BODY;
			default:
				throw new JspException("Unexpected communication result: "+transactionResult.getCommunicationResult());
		}
	}
}
