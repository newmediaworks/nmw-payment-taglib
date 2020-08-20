/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019  New Media Works
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
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Processes a credit card transaction.
 *
 * @see  MerchantServicesProvider#sale(com.aoindustries.creditcards.TransactionRequest, com.aoindustries.creditcards.CreditCard)
 * @see  MerchantServicesProvider#authorize(com.aoindustries.creditcards.TransactionRequest, com.aoindustries.creditcards.CreditCard)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class PaymentTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	// Attributes
	private boolean capture;
	private boolean test;

	// Set by nested tags
	private String orderNumber;
	private String currencyCode;
	private BigDecimal amount;
	private BigDecimal taxAmount;
	private boolean taxExempt;
	private BigDecimal shippingAmount;
	private BigDecimal dutyAmount;
	private String merchantEmail;
	private String invoiceNumber;
	private String purchaseOrderNumber;
	private String comment;
	private String creditCardCardNumber;
	private String creditCardMaskedCardNumber;
	private byte creditCardExpirationMonth;
	private short creditCardExpirationYear;
	private String creditCardCardCode;
	private String creditCardGUID;
	private String creditCardFirstName;
	private String creditCardLastName;
	private String creditCardCompanyName;
	private String creditCardStreetAddress1;
	private String creditCardStreetAddress2;
	private String creditCardCity;
	private String creditCardCountryCode;
	private String creditCardEmail;
	private String creditCardPhone;
	private String creditCardFax;
	private String creditCardCustomerId;
	private String creditCardCustomerTaxId;
	private String creditCardState;
	private String creditCardPostalCode;
	private String creditCardComment;
	private String shippingAddressFirstName;
	private String shippingAddressLastName;
	private String shippingAddressCompanyName;
	private String shippingAddressStreetAddress1;
	private String shippingAddressStreetAddress2;
	private String shippingAddressCity;
	private String shippingAddressState;
	private String shippingAddressPostalCode;
	private String shippingAddressCountryCode;

	/** The result of the processing. */
	private AuthorizationResult authorizationResult;

	public PaymentTag() {
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
		capture = true;
		test = false;
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
		authorizationResult = null;
	}

	public boolean getCapture() {
		return capture;
	}

	public void setCapture(boolean capture) {
		this.capture = capture;
	}

	public boolean getTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	void setTaxExempt(boolean taxExempt) {
		this.taxExempt = taxExempt;
	}

	void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	void setDutyAmount(BigDecimal dutyAmount) {
		this.dutyAmount = dutyAmount;
	}

	void setMerchantEmail(String merchantEmail) {
		this.merchantEmail = merchantEmail;
	}

	void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	void setComment(String comment) {
		this.comment = comment;
	}

	void setCreditCardCardNumber(String creditCardCardNumber) {
		this.creditCardCardNumber = creditCardCardNumber;
	}

	void setCreditCardMaskedCardNumber(String creditCardMaskedCardNumber) {
		this.creditCardMaskedCardNumber = creditCardMaskedCardNumber;
	}

	void setCreditCardExpirationMonth(byte creditCardExpirationMonth) {
		this.creditCardExpirationMonth = creditCardExpirationMonth;
	}

	void setCreditCardExpirationYear(short creditCardExpirationYear) {
		this.creditCardExpirationYear = creditCardExpirationYear;
	}

	void setCreditCardCardCode(String creditCardCardCode) {
		this.creditCardCardCode = creditCardCardCode;
	}

	void setCreditCardGUID(String creditCardGUID) {
		this.creditCardGUID = creditCardGUID;
	}

	void setCreditCardFirstName(String creditCardFirstName) {
		this.creditCardFirstName = creditCardFirstName;
	}

	void setCreditCardLastName(String creditCardLastName) {
		this.creditCardLastName = creditCardLastName;
	}

	void setCreditCardCompanyName(String creditCardCompanyName) {
		this.creditCardCompanyName = creditCardCompanyName;
	}

	void setCreditCardStreetAddress1(String creditCardStreetAddress1) {
		this.creditCardStreetAddress1 = creditCardStreetAddress1;
	}

	void setCreditCardStreetAddress2(String creditCardStreetAddress2) {
		this.creditCardStreetAddress2 = creditCardStreetAddress2;
	}

	void setCreditCardCity(String creditCardCity) {
		this.creditCardCity = creditCardCity;
	}

	void setCreditCardCountryCode(String creditCardCountryCode) {
		this.creditCardCountryCode = creditCardCountryCode;
	}

	void setCreditCardEmail(String creditCardEmail) {
		this.creditCardEmail = creditCardEmail;
	}

	void setCreditCardPhone(String creditCardPhone) {
		this.creditCardPhone = creditCardPhone;
	}

	void setCreditCardFax(String creditCardFax) {
		this.creditCardFax = creditCardFax;
	}

	void setCreditCardCustomerId(String creditCardCustomerId) {
		this.creditCardCustomerId = creditCardCustomerId;
	}

	void setCreditCardCustomerTaxId(String creditCardCustomerTaxId) {
		this.creditCardCustomerTaxId = creditCardCustomerTaxId;
	}

	void setCreditCardState(String creditCardState) {
		this.creditCardState = creditCardState;
	}

	void setCreditCardPostalCode(String creditCardPostalCode) {
		this.creditCardPostalCode = creditCardPostalCode;
	}

	void setCreditCardComment(String creditCardComment) {
		this.creditCardComment = creditCardComment;
	}

	void setShippingAddressFirstName(String shippingAddressFirstName) {
		this.shippingAddressFirstName = shippingAddressFirstName;
	}

	void setShippingAddressLastName(String shippingAddressLastName) {
		this.shippingAddressLastName = shippingAddressLastName;
	}

	void setShippingAddressCompanyName(String shippingAddressCompanyName) {
		this.shippingAddressCompanyName = shippingAddressCompanyName;
	}

	void setShippingAddressStreetAddress1(String shippingAddressStreetAddress1) {
		this.shippingAddressStreetAddress1 = shippingAddressStreetAddress1;
	}

	void setShippingAddressStreetAddress2(String shippingAddressStreetAddress2) {
		this.shippingAddressStreetAddress2 = shippingAddressStreetAddress2;
	}

	void setShippingAddressCity(String shippingAddressCity) {
		this.shippingAddressCity = shippingAddressCity;
	}

	void setShippingAddressState(String shippingAddressState) {
		this.shippingAddressState = shippingAddressState;
	}

	void setShippingAddressPostalCode(String shippingAddressPostalCode) {
		this.shippingAddressPostalCode = shippingAddressPostalCode;
	}

	void setShippingAddressCountryCode(String shippingAddressCountryCode) {
		this.shippingAddressCountryCode = shippingAddressCountryCode;
	}

	void process() throws JspException {
		try {
			// Make sure the processor is set
			MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
			if(processor==null) throw new JspException("processor not set, please set processor with the useProcessor tag first");

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
		} catch(IllegalArgumentException err) {
			throw new JspException(err);
		}
	}

	AuthorizationResult getAuthorizationResult() {
		return authorizationResult;
	}
}
