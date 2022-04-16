/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019, 2020, 2021, 2022  New Media Works
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
 * along with nmw-payment-taglib.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.newmediaworks.taglib.payment;

import com.aoapps.payments.AuthorizationResult;
import com.aoapps.payments.AuthorizationResult.ApprovalResult;
import com.aoapps.payments.TransactionResult.CommunicationResult;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The body of this tag will be processed when the payment was accepted (approved).
 *
 * @see  Functions#isAccepted()
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

	@Override
	public int doStartTag() throws JspException {
		return Functions.isAccepted(TAG_NAME, pageContext.getRequest()) ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
}
