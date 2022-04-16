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

import com.aoapps.lang.Strings;
import com.aoapps.payments.AuthorizationResult;
import com.aoapps.payments.MerchantServicesProvider;
import com.aoapps.payments.Transaction;
import com.aoapps.payments.VoidResult;
import com.aoapps.servlet.attribute.ScopeEE;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Voids a previous transaction (from {@link PaymentTag}).
 *
 * @see  MerchantServicesProvider#voidTransaction(com.aoapps.payments.Transaction)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class VoidTag extends BodyTagSupport implements TryCatchFinally {

	public static final String TAG_NAME = "<payment:void>";

	/**
	 * The name of the request-scope attribute containing the current void tag.
	 */
	private static final ScopeEE.Request.Attribute<VoidTag> REQUEST_ATTRIBUTE_NAME =
		ScopeEE.REQUEST.attribute(VoidTag.class.getName());

	// Java 9: module-private
	public static Optional<VoidTag> getCurrent(ServletRequest request) {
		return Optional.ofNullable(REQUEST_ATTRIBUTE_NAME.context(request).get());
	}
	// Java 9: module-private
	public static VoidTag requireCurrent(String fromName, ServletRequest request) throws JspException {
		return getCurrent(request).orElseThrow(
			() -> new JspTagException(fromName + " must be within " + TAG_NAME)
		);
	}

	public VoidTag() {
		init();
	}

	private static final long serialVersionUID = 2L;

	private transient boolean requestAttributeSet;

	// <editor-fold desc="Attributes">
	private String transactionId;
	public void setTransactionId(String transactionId) {
		this.transactionId = Strings.trimNullIfEmpty(transactionId);
	}
	// </editor-fold>

	// <editor-fold desc="The result of the processing">
	private transient VoidResult voidResult;
	VoidResult getVoidResult() {
		return voidResult;
	}
	// </editor-fold>

	private void init() {
		requestAttributeSet = false;
		// Attributes
		transactionId = null;
		// The result of the processing
		voidResult = null;
	}

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		// Make sure the processor is set
		MerchantServicesProvider processor = Constants.PROCESSOR.context(request).get();
		if(processor == null) throw new JspTagException("processor not set, please set processor with " + UseProcessorTag.TAG_NAME + " first");
		// Store this on the request
		if(getCurrent(request).isPresent()) throw new JspTagException(TAG_NAME + " may not be nested within " + TAG_NAME);
		REQUEST_ATTRIBUTE_NAME.context(request).set(this);
		requestAttributeSet = true;
		return EVAL_BODY_INCLUDE;
	}

	void process() throws JspException {
		// Make sure the processor is set
		MerchantServicesProvider processor = Constants.PROCESSOR.context(pageContext.getRequest()).get();
		if(processor == null) throw new JspTagException("processor not set, please set processor with " + UseProcessorTag.TAG_NAME + " first");

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

	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	@Override
	public void doFinally() {
		if(requestAttributeSet) {
			REQUEST_ATTRIBUTE_NAME.context(pageContext.getRequest()).remove();
		}
		init();
	}
}
