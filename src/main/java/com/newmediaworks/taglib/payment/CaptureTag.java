/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
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

import com.aoapps.lang.Strings;
import com.aoapps.payments.AuthorizationResult;
import com.aoapps.payments.CaptureResult;
import com.aoapps.payments.MerchantServicesProvider;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Captures the funds from a previous authorization-only transaction (from {@link PaymentTag} with {@link PaymentTag#setCapture(boolean) capture=false}).
 *
 * @see  MerchantServicesProvider#capture(com.aoapps.payments.AuthorizationResult)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class CaptureTag extends BodyTagSupport implements TryCatchFinally {

	public static final String TAG_NAME = "<payment:capture>";

	/**
	 * The name of the request-scope attribute containing the current capture tag.
	 */
	private static final String REQUEST_ATTRIBUTE_NAME = CaptureTag.class.getName();

	// Java 9: module-private
	public static Optional<CaptureTag> getCurrent(ServletRequest request) {
		return Optional.ofNullable((CaptureTag)request.getAttribute(REQUEST_ATTRIBUTE_NAME));
	}
	// Java 9: module-private
	public static CaptureTag requireCurrent(String fromName, ServletRequest request) throws JspException {
		return getCurrent(request).orElseThrow(
			() -> new JspTagException(fromName + " must be within " + TAG_NAME)
		);
	}

	public CaptureTag() {
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
	private transient CaptureResult captureResult;
	CaptureResult getCaptureResult() {
		return captureResult;
	}
	// </editor-fold>

	private void init() {
		requestAttributeSet = false;
		// Attributes
		transactionId = null;
		// The result of the processing
		captureResult = null;
	}

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)request.getAttribute(Constants.processor);
		if(processor == null) throw new JspTagException("processor not set, please set processor with " + UseProcessorTag.TAG_NAME + " first");
		// Store this on the request
		if(getCurrent(request).isPresent()) throw new JspTagException(TAG_NAME + " may not be nested within " + TAG_NAME);
		request.setAttribute(REQUEST_ATTRIBUTE_NAME, this);
		requestAttributeSet = true;
		return EVAL_BODY_INCLUDE;
	}

	void process() throws JspException {
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
		if(processor == null) throw new JspTagException("processor not set, please set processor with " + UseProcessorTag.TAG_NAME + " first");

		captureResult = processor.capture(
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
			pageContext.getRequest().removeAttribute(REQUEST_ATTRIBUTE_NAME);
		}
		init();
	}
}
