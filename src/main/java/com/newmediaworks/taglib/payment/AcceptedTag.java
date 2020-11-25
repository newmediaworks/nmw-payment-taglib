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

import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.creditcards.AuthorizationResult.ApprovalResult;
import com.aoindustries.creditcards.TransactionResult.CommunicationResult;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The body of this tag will be processed when the payment was accepted (approved).
 *
 * @see  AuthorizationResult#getCommunicationResult()
 * @see  CommunicationResult#SUCCESS
 * @see  AuthorizationResult#getApprovalResult()
 * @see  ApprovalResult#APPROVED
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class AcceptedTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:accepted>";

	private static final long serialVersionUID = 1L;

	public AcceptedTag() {
	}

	@Override
	public int doStartTag() throws JspException {
		PaymentTag paymentTag = JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();

		switch(authorizationResult.getCommunicationResult()) {
			case LOCAL_ERROR:
			case IO_ERROR:
			case GATEWAY_ERROR:
				return SKIP_BODY;
			case SUCCESS:
				switch(authorizationResult.getApprovalResult()) {
					case DECLINED:
					case HOLD:
						return SKIP_BODY;
					case APPROVED:
						return EVAL_BODY_INCLUDE;
					default:
						throw new JspTagException("Unexpected approval result: "+authorizationResult.getApprovalResult());
				}
			default:
				throw new JspTagException("Unexpected communication result: "+authorizationResult.getCommunicationResult());
		}
	}
}
