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
import com.aoapps.payments.CreditCard;
import com.aoapps.payments.MerchantServicesProvider;
import com.aoapps.payments.TransactionRequest;
import java.util.Currency;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Processes a credit card transaction.
 *
 * @see  MerchantServicesProvider#sale(com.aoapps.payments.TransactionRequest, com.aoapps.payments.CreditCard)
 * @see  MerchantServicesProvider#authorize(com.aoapps.payments.TransactionRequest, com.aoapps.payments.CreditCard)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class PaymentTag extends BodyTagSupport implements TryCatchFinally {

	public static final String TAG_NAME = "<payment:payment>";

	/**
	 * The name of the request-scope attribute containing the current payment tag.
	 */
	private static final String REQUEST_ATTRIBUTE_NAME = PaymentTag.class.getName();

	// Java 9: module-private
	public static Optional<PaymentTag> getCurrent(ServletRequest request) {
		return Optional.ofNullable((PaymentTag)request.getAttribute(REQUEST_ATTRIBUTE_NAME));
	}
	// Java 9: module-private
	public static PaymentTag requireCurrent(String fromName, ServletRequest request) throws JspException {
		return getCurrent(request).orElseThrow(
			() -> new JspTagException(fromName + " must be within " + TAG_NAME)
		);
	}

	public PaymentTag() {
		init();
	}

	private static final long serialVersionUID = 2L;

	private transient boolean requestAttributeSet;

	private transient CreditCard creditCard;
	CreditCard getCreditCard() {
		if(creditCard == null) throw new AssertionError("creditCard is null");
		return creditCard;
	}

	private transient TransactionRequest transactionRequest;
	TransactionRequest getTransactionRequest() {
		if(creditCard == null) throw new AssertionError("transactionRequest is null");
		return transactionRequest;
	}

	// <editor-fold desc="Attributes">
	private boolean capture;
	public void setCapture(boolean capture) {
		this.capture = capture;
	}

	public void setTest(boolean test) {
		transactionRequest.setTestMode(test);
	}

	public void setOrderNumber(String orderNumber) {
		transactionRequest.setOrderNumber(Strings.trimNullIfEmpty(orderNumber));
	}

	public void setCurrencyCode(String currencyCode) {
		currencyCode = Strings.trimNullIfEmpty(currencyCode);
		if(currencyCode != null) transactionRequest.setCurrency(Currency.getInstance(currencyCode));
	}

	public void setAmount(String amount) throws IllegalArgumentException {
		try {
			transactionRequest.setAmount(CurrencyUtil.parseCurrency(amount));
		} catch(NumberFormatException err) {
			throw new IllegalArgumentException("Invalid amount: " + amount, err);
		}
	}

	public void setTaxAmount(String taxAmount) throws IllegalArgumentException {
		try {
			transactionRequest.setTaxAmount(CurrencyUtil.parseCurrency(taxAmount));
		} catch(NumberFormatException err) {
			throw new IllegalArgumentException("Invalid taxAmount: " + taxAmount, err);
		}
	}

	public void setTaxExempt(boolean taxExempt) {
		transactionRequest.setTaxExempt(taxExempt);
	}

	public void setShippingAmount(String shippingAmount) throws IllegalArgumentException {
		try {
			transactionRequest.setShippingAmount(CurrencyUtil.parseCurrency(shippingAmount));
		} catch(NumberFormatException err) {
			throw new IllegalArgumentException("Invalid shippingAmount: " + shippingAmount, err);
		}
	}

	public void setDutyAmount(String dutyAmount) throws IllegalArgumentException {
		try {
			transactionRequest.setDutyAmount(CurrencyUtil.parseCurrency(dutyAmount));
		} catch(NumberFormatException err) {
			throw new IllegalArgumentException("Invalid dutyAmount: " + dutyAmount, err);
		}
	}

	public void setMerchantEmail(String merchantEmail) {
		transactionRequest.setMerchantEmail(Strings.trimNullIfEmpty(merchantEmail));
	}

	public void setInvoiceNumber(String invoiceNumber) {
		transactionRequest.setInvoiceNumber(Strings.trimNullIfEmpty(invoiceNumber));
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		transactionRequest.setPurchaseOrderNumber(Strings.trimNullIfEmpty(purchaseOrderNumber));
	}

	public void setComment(String comment) {
		transactionRequest.setDescription(Strings.trimNullIfEmpty(comment));
	}
	// </editor-fold>

	// <editor-fold desc="The result of the processing">
	private transient AuthorizationResult authorizationResult;
	// Java 9: module-private
	public AuthorizationResult getAuthorizationResult() {
		return authorizationResult;
	}
	// </editor-fold>

	private void init() {
		requestAttributeSet = false;
		// Used by nested tags
		transactionRequest = new TransactionRequest();
		creditCard = new CreditCard();
		// Attributes
		capture = true;
		// The result of the processing
		authorizationResult = null;
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
		ServletRequest request = pageContext.getRequest();
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)request.getAttribute(Constants.processor);
		if(processor == null) throw new JspTagException("processor not set, please set processor with " + UseProcessorTag.TAG_NAME + " first");

		transactionRequest.setCustomerIp(request.getRemoteAddr());
		if(creditCard.getProviderId() == null) {
			creditCard.setProviderId(processor.getProviderId());
		}

		if(capture) {
			this.authorizationResult = processor.sale(transactionRequest, creditCard).getAuthorizationResult();
		} else {
			this.authorizationResult = processor.authorize(transactionRequest, creditCard);
		}
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
