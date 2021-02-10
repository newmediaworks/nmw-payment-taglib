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

import com.aoindustries.creditcards.MerchantServicesProvider;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This is the point where the bank is contacted and the
 * {@link PaymentTag},
 * {@link CaptureTag},
 * {@link VoidTag} is processed.
 *
 * @see  MerchantServicesProvider#sale(com.aoindustries.creditcards.TransactionRequest, com.aoindustries.creditcards.CreditCard)
 * @see  MerchantServicesProvider#authorize(com.aoindustries.creditcards.TransactionRequest, com.aoindustries.creditcards.CreditCard)
 * @see  MerchantServicesProvider#capture(com.aoindustries.creditcards.AuthorizationResult)
 * @see  MerchantServicesProvider#voidTransaction(com.aoindustries.creditcards.Transaction)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ProcessTag extends TagSupport {

	public static final String TAG_NAME = "<payment:process>";

	public ProcessTag() {
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		// Java 9: ifPresentOfElse
		Optional<PaymentTag> paymentTag = PaymentTag.getCurrent(request);
		if(paymentTag.isPresent()) {
			paymentTag.get().process();
		} else {
			Optional<CaptureTag> captureTag = CaptureTag.getCurrent(request);
			if(captureTag.isPresent()) {
				captureTag.get().process();
			} else {
				Optional<VoidTag> voidTag = VoidTag.getCurrent(request);
				if(voidTag.isPresent()) {
					voidTag.get().process();
				} else {
					throw new JspTagException(TAG_NAME + " must be within " + PaymentTag.TAG_NAME + ", " + CaptureTag.TAG_NAME + ", or " + VoidTag.TAG_NAME);
				}
			}
		}
		return SKIP_BODY;
	}
}
