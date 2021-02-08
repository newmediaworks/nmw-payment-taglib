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
import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.TransactionRequest;
import java.math.BigDecimal;
import java.util.Currency;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Processes a credit card transaction.
 *
 * @see  MerchantServicesProvider#sale(com.aoindustries.creditcards.TransactionRequest, com.aoindustries.creditcards.CreditCard)
 * @see  MerchantServicesProvider#authorize(com.aoindustries.creditcards.TransactionRequest, com.aoindustries.creditcards.CreditCard)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class PaymentTag extends BodyTagSupport implements TryCatchFinally {

	public static final String TAG_NAME = "<payment:payment>";

	public PaymentTag() {
		init();
	}

	private static final long serialVersionUID = 2L;

	// <editor-fold desc="Attributes">
	private boolean capture;
	public boolean getCapture() {
		return capture;
	}
	public void setCapture(boolean capture) {
		this.capture = capture;
	}

	private boolean test;
	public boolean getTest() {
		return test;
	}
	public void setTest(boolean test) {
		this.test = test;
	}
	// </editor-fold>

	// <editor-fold desc="Set by nested tags">
	private transient String orderNumber;
	void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	private transient String currencyCode;
	void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	private transient BigDecimal amount;
	void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	private transient BigDecimal taxAmount;
	void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	private transient boolean taxExempt;
	void setTaxExempt(boolean taxExempt) {
		this.taxExempt = taxExempt;
	}

	private transient BigDecimal shippingAmount;
	void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	private transient BigDecimal dutyAmount;
	void setDutyAmount(BigDecimal dutyAmount) {
		this.dutyAmount = dutyAmount;
	}

	private transient String merchantEmail;
	void setMerchantEmail(String merchantEmail) {
		this.merchantEmail = merchantEmail;
	}

	private transient String invoiceNumber;
	void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	private transient String purchaseOrderNumber;
	void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	private transient String comment;
	void setComment(String comment) {
		this.comment = comment;
	}

	private transient String creditCardCardNumber;
	void setCreditCardCardNumber(String creditCardCardNumber) {
		this.creditCardCardNumber = creditCardCardNumber;
	}

	private transient String creditCardMaskedCardNumber;
	void setCreditCardMaskedCardNumber(String creditCardMaskedCardNumber) {
		this.creditCardMaskedCardNumber = creditCardMaskedCardNumber;
	}

	private transient byte creditCardExpirationMonth;
	void setCreditCardExpirationMonth(byte creditCardExpirationMonth) {
		this.creditCardExpirationMonth = creditCardExpirationMonth;
	}

	private transient short creditCardExpirationYear;
	void setCreditCardExpirationYear(short creditCardExpirationYear) {
		this.creditCardExpirationYear = creditCardExpirationYear;
	}

	private transient String creditCardCardCode;
	void setCreditCardCardCode(String creditCardCardCode) {
		this.creditCardCardCode = creditCardCardCode;
	}

	private transient String creditCardGUID;
	void setCreditCardGUID(String creditCardGUID) {
		this.creditCardGUID = creditCardGUID;
	}

	private transient String creditCardFirstName;
	void setCreditCardFirstName(String creditCardFirstName) {
		this.creditCardFirstName = creditCardFirstName;
	}

	private transient String creditCardLastName;
	void setCreditCardLastName(String creditCardLastName) {
		this.creditCardLastName = creditCardLastName;
	}

	private transient String creditCardCompanyName;
	void setCreditCardCompanyName(String creditCardCompanyName) {
		this.creditCardCompanyName = creditCardCompanyName;
	}

	private transient String creditCardStreetAddress1;
	void setCreditCardStreetAddress1(String creditCardStreetAddress1) {
		this.creditCardStreetAddress1 = creditCardStreetAddress1;
	}

	private transient String creditCardStreetAddress2;
	void setCreditCardStreetAddress2(String creditCardStreetAddress2) {
		this.creditCardStreetAddress2 = creditCardStreetAddress2;
	}

	private transient String creditCardCity;
	void setCreditCardCity(String creditCardCity) {
		this.creditCardCity = creditCardCity;
	}

	private transient String creditCardCountryCode;
	void setCreditCardCountryCode(String creditCardCountryCode) {
		this.creditCardCountryCode = creditCardCountryCode;
	}

	private transient String creditCardEmail;
	void setCreditCardEmail(String creditCardEmail) {
		this.creditCardEmail = creditCardEmail;
	}

	private transient String creditCardPhone;
	void setCreditCardPhone(String creditCardPhone) {
		this.creditCardPhone = creditCardPhone;
	}

	private transient String creditCardFax;
	void setCreditCardFax(String creditCardFax) {
		this.creditCardFax = creditCardFax;
	}

	private transient String creditCardCustomerId;
	void setCreditCardCustomerId(String creditCardCustomerId) {
		this.creditCardCustomerId = creditCardCustomerId;
	}

	private transient String creditCardCustomerTaxId;
	void setCreditCardCustomerTaxId(String creditCardCustomerTaxId) {
		this.creditCardCustomerTaxId = creditCardCustomerTaxId;
	}

	private transient String creditCardState;
	void setCreditCardState(String creditCardState) {
		this.creditCardState = creditCardState;
	}

	private transient String creditCardPostalCode;
	void setCreditCardPostalCode(String creditCardPostalCode) {
		this.creditCardPostalCode = creditCardPostalCode;
	}

	private transient String creditCardComment;
	void setCreditCardComment(String creditCardComment) {
		this.creditCardComment = creditCardComment;
	}

	private transient String shippingAddressFirstName;
	void setShippingAddressFirstName(String shippingAddressFirstName) {
		this.shippingAddressFirstName = shippingAddressFirstName;
	}

	private transient String shippingAddressLastName;
	void setShippingAddressLastName(String shippingAddressLastName) {
		this.shippingAddressLastName = shippingAddressLastName;
	}

	private transient String shippingAddressCompanyName;
	void setShippingAddressCompanyName(String shippingAddressCompanyName) {
		this.shippingAddressCompanyName = shippingAddressCompanyName;
	}

	private transient String shippingAddressStreetAddress1;
	void setShippingAddressStreetAddress1(String shippingAddressStreetAddress1) {
		this.shippingAddressStreetAddress1 = shippingAddressStreetAddress1;
	}

	private transient String shippingAddressStreetAddress2;
	void setShippingAddressStreetAddress2(String shippingAddressStreetAddress2) {
		this.shippingAddressStreetAddress2 = shippingAddressStreetAddress2;
	}

	private transient String shippingAddressCity;
	void setShippingAddressCity(String shippingAddressCity) {
		this.shippingAddressCity = shippingAddressCity;
	}

	private transient String shippingAddressState;
	void setShippingAddressState(String shippingAddressState) {
		this.shippingAddressState = shippingAddressState;
	}

	private transient String shippingAddressPostalCode;
	void setShippingAddressPostalCode(String shippingAddressPostalCode) {
		this.shippingAddressPostalCode = shippingAddressPostalCode;
	}

	private transient String shippingAddressCountryCode;
	void setShippingAddressCountryCode(String shippingAddressCountryCode) {
		this.shippingAddressCountryCode = shippingAddressCountryCode;
	}
	// </editor-fold>

	// <editor-fold desc="The result of the processing">
	private transient AuthorizationResult authorizationResult;
	AuthorizationResult getAuthorizationResult() {
		return authorizationResult;
	}
	// </editor-fold>

	private void init() {
		// Attributes
		capture = true;
		test = false;
		// Set by nested tags
		orderNumber = null;
		currencyCode = null;
		amount = null;
		taxAmount = null;
		taxExempt = false;
		shippingAmount = null;
		dutyAmount = null;
		merchantEmail = null;
		invoiceNumber = null;
		purchaseOrderNumber = null;
		comment = null;
		creditCardCardNumber = null;
		creditCardMaskedCardNumber = null;
		creditCardExpirationMonth = (byte)-1;
		creditCardExpirationYear = (short)-1;
		creditCardCardCode = null;
		creditCardGUID = null;
		creditCardFirstName = null;
		creditCardLastName = null;
		creditCardCompanyName = null;
		creditCardStreetAddress1 = null;
		creditCardStreetAddress2 = null;
		creditCardCity = null;
		creditCardCountryCode = null;
		creditCardEmail = null;
		creditCardPhone = null;
		creditCardFax = null;
		creditCardCustomerId = null;
		creditCardCustomerTaxId = null;
		creditCardState = null;
		creditCardPostalCode = null;
		creditCardComment = null;
		shippingAddressFirstName = null;
		shippingAddressLastName = null;
		shippingAddressCompanyName = null;
		shippingAddressStreetAddress1 = null;
		shippingAddressStreetAddress2 = null;
		shippingAddressCity = null;
		shippingAddressState = null;
		shippingAddressPostalCode = null;
		shippingAddressCountryCode = null;
		// The result of the processing
		authorizationResult = null;
	}

	@Override
	public int doStartTag() throws JspException {
		// Make sure the processor is set
		MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
		if(processor==null) throw new JspTagException("processor not set, please set processor with the useProcessor tag first");
		return EVAL_BODY_INCLUDE;
	}

	void process() throws JspException {
		try {
			// Make sure the processor is set
			MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
			if(processor==null) throw new JspTagException("processor not set, please set processor with the useProcessor tag first");

			TransactionRequest transactionRequest = new TransactionRequest(
				test,
				pageContext.getRequest().getRemoteAddr(),
				120,
				orderNumber,
				currencyCode==null || currencyCode.length()==0 ? null : Currency.getInstance(currencyCode),
				amount,
				taxAmount,
				taxExempt,
				shippingAmount,
				dutyAmount,
				shippingAddressFirstName,
				shippingAddressLastName,
				shippingAddressCompanyName,
				shippingAddressStreetAddress1,
				shippingAddressStreetAddress2,
				shippingAddressCity,
				shippingAddressState,
				shippingAddressPostalCode,
				shippingAddressCountryCode,
				false,
				merchantEmail,
				invoiceNumber,
				purchaseOrderNumber,
				comment
			);
			CreditCard creditCard = new CreditCard(
				null,
				null,
				null,
				creditCardGUID==null || creditCardGUID.length()==0 ? null : processor.getProviderId(),
				creditCardGUID,
				creditCardCardNumber,
				creditCardMaskedCardNumber,
				creditCardExpirationMonth,
				creditCardExpirationYear,
				creditCardCardCode,
				creditCardFirstName,
				creditCardLastName,
				creditCardCompanyName,
				creditCardEmail,
				creditCardPhone,
				creditCardFax,
				creditCardCustomerId,
				creditCardCustomerTaxId,
				creditCardStreetAddress1,
				creditCardStreetAddress2,
				creditCardCity,
				creditCardState,
				creditCardPostalCode,
				creditCardCountryCode,
				creditCardComment
			);

			if(capture) {
				this.authorizationResult = processor.sale(transactionRequest, creditCard).getAuthorizationResult();
			} else {
				this.authorizationResult = processor.authorize(transactionRequest, creditCard);
			}
		} catch(IllegalArgumentException e) {
			throw new JspTagException(e);
		}
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	@Override
	public void doFinally() {
		init();
	}
}
