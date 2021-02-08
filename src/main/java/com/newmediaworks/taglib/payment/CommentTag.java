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
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.util.Optional;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides a comment to a {@link StoreCreditCardTag},
 * {@link CreditCardTag},
 * or {@link PaymentTag}.
 *
 * @see  CreditCard#setComments(java.lang.String)
 * @see  TransactionRequest#setDescription(java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class CommentTag extends BodyTagSupport {

	public static final String TAG_NAME = "<payment:comment>";

	public CommentTag() {
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		String comment = getBodyContent().getString().trim();
		// Java 9: ifPresentOrElse
		Optional<StoreCreditCardTag> storeCreditCardTag = JspTagUtils.findAncestor(this, StoreCreditCardTag.class);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardTag.get().setComment(comment);
		} else {
			Optional<CreditCardTag> creditCardTag = JspTagUtils.findAncestor(this, CreditCardTag.class);
			if(creditCardTag.isPresent()) {
				JspTagUtils.requireAncestor(CreditCardTag.TAG_NAME, creditCardTag.get(), PaymentTag.TAG_NAME, PaymentTag.class)
					.setCreditCardComment(comment);
			} else {
				JspTagUtils.requireAncestor(TAG_NAME, this, StoreCreditCardTag.TAG_NAME + ", " + CreditCardTag.TAG_NAME + ", or " + PaymentTag.TAG_NAME, PaymentTag.class)
					.setComment(comment);
			}
		}

		return EVAL_PAGE;
	}
}
