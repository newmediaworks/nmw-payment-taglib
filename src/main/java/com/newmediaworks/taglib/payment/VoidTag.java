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
import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.VoidResult;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Voids a previous transaction (from {@link PaymentTag}).
 *
 * @see  MerchantServicesProvider#voidTransaction(com.aoindustries.creditcards.Transaction)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class VoidTag extends BodyTagSupport {

	static final String TAG_NAME = "<payment:void>";

	private static final long serialVersionUID = 1L;

	// Set by nested tags
	private String transactionId;

	/** The result of the processing. */
	private VoidResult voidResult;

	public VoidTag() {
		init();
	}

	@Override
	public int doStartTag() throws JspException {
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
		if(processor==null) throw new JspException("processor not set, please set processor with the useProcessor tag first");
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() {
		init();
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		init();
	}

	private void init() {
		transactionId = null;
		voidResult = null;
	}

	void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	void process() throws JspException {
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
		if(processor==null) throw new JspException("processor not set, please set processor with the useProcessor tag first");

		voidResult = processor.voidTransaction(
			new Transaction(
				processor.getProviderId(),
				null,
				null,
				null,
				null,
				0,
				null,
				new AuthorizationResult(
					processor.getProviderId(),
					null,
					null,
					null,
					null,
					transactionId,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null
				),
				0,
				null,
				null,
				0,
				null,
				null,
				null
			)
		);
	}

	VoidResult getVoidResult() {
		return voidResult;
	}
}
