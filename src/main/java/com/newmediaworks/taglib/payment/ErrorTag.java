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

import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.creditcards.TransactionResult.CommunicationResult;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The body of this tag will be processed when there was any type of error processing the
 * {@link PaymentTag},
 * {@link CaptureTag},
 * or {@link VoidTag}.
 *
 * @see  Functions#isError()
 * @see  AuthorizationResult#getCommunicationResult()
 * @see  CommunicationResult#LOCAL_ERROR
 * @see  CommunicationResult#IO_ERROR
 * @see  CommunicationResult#GATEWAY_ERROR
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ErrorTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:error>";

	public ErrorTag() {
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		return Functions.isError(TAG_NAME, pageContext.getRequest()) ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
}
