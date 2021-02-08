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
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Gets the review reason for a transaction that has been placed on hold.
 *
 * @see  AuthorizationResult#getReviewReason()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class GetReviewReasonTag extends TagSupport {

	public static final String TAG_NAME = "<payment:getReviewReason>";

	public GetReviewReasonTag() {
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		try {
			HeldTag heldTag = JspTagUtils.requireAncestor(TAG_NAME, this, HeldTag.TAG_NAME, HeldTag.class);
			PaymentTag paymentTag = JspTagUtils.requireAncestor(HeldTag.TAG_NAME, heldTag, PaymentTag.TAG_NAME, PaymentTag.class);

			AuthorizationResult.ReviewReason reviewReason = paymentTag.getAuthorizationResult().getReviewReason();
			if(reviewReason!=null) pageContext.getOut().write(reviewReason.toString());

			return SKIP_BODY;
		} catch(IOException e) {
			throw new JspTagException(e);
		}
	}
}
