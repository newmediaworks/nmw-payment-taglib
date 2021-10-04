/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
 * Copyright (C) 2021  New Media Works
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

import com.aoapps.payments.AuthorizationResult;
import com.aoapps.payments.AuthorizationResult.ApprovalResult;
import com.aoapps.payments.CreditCard;
import com.aoapps.payments.TransactionResult;
import com.aoapps.payments.TransactionResult.CommunicationResult;
import static com.aoapps.servlet.filter.FunctionContext.getRequest;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public final class Functions {

	private static final Logger logger = Logger.getLogger(Functions.class.getName());

	private Functions() {
	}

	/**
	 * @see  MaskCardNumberTag
	 * @see  CreditCard#maskCreditCardNumber(java.lang.String)
	 */
	public static String maskCardNumber(String cardNumber) {
		return CreditCard.maskCreditCardNumber(cardNumber);
	}

	/**
	 * Gets the transaction result from one of the outer payment, capture, or void tags.
	 */
	private static TransactionResult getTransactionResult(String fromName, ServletRequest request) throws JspException {
		Optional<PaymentTag> paymentTag = PaymentTag.getCurrent(request);
		if(paymentTag.isPresent()) return paymentTag.get().getAuthorizationResult();

		Optional<CaptureTag> captureTag = CaptureTag.getCurrent(request);
		if(captureTag.isPresent()) return captureTag.get().getCaptureResult();

		Optional<VoidTag> voidTag = VoidTag.getCurrent(request);
		if(voidTag.isPresent()) return voidTag.get().getVoidResult();

		throw new JspTagException(fromName + " must be within " + PaymentTag.TAG_NAME + ", " + CaptureTag.TAG_NAME + ", or " + VoidTag.TAG_NAME);
	}

	static boolean isError(String fromName, ServletRequest request) throws JspException {
		TransactionResult transactionResult = getTransactionResult(fromName, request);

		switch(transactionResult.getCommunicationResult()) {
			case LOCAL_ERROR:
			case IO_ERROR:
			case GATEWAY_ERROR:
				return true;
			case SUCCESS:
				return false;
			default:
				throw new JspTagException("Unexpected communication result: " + transactionResult.getCommunicationResult());
		}
	}

	/**
	 * Determines if there was any type of error processing the
	 * {@link PaymentTag},
	 * {@link CaptureTag},
	 * or {@link VoidTag}.
	 *
	 * @see  ErrorTag
	 * @see  AuthorizationResult#getCommunicationResult()
	 * @see  CommunicationResult#LOCAL_ERROR
	 * @see  CommunicationResult#IO_ERROR
	 * @see  CommunicationResult#GATEWAY_ERROR
	 */
	public static boolean isError() throws JspException {
		return isError("${payment:getTransactionResult()}", getRequest());
	}

	// Java 9: module-private
	public static String getErrorReason(String fromName, ServletRequest request) throws JspException {
		TransactionResult result = Functions.getTransactionResult(fromName, request);
		if(result != null) {
			if(logger.isLoggable(Level.SEVERE)) {
				logger.severe(
					"Error from credit card transaction:" + System.lineSeparator()
					+ "    providerUniqueId=" + result.getProviderUniqueId() + System.lineSeparator()
					+ "    providerErrorCode=" + result.getProviderErrorCode() + System.lineSeparator()
					+ "    providerErrorMessage=" + result.getProviderErrorMessage() + System.lineSeparator()
					+ "    errorCode=" + result.getErrorCode()
				);
			}
			return result.getErrorCode().toString();
		} else {
			return null;
		}
	}

	/**
	 * Gets the reason for a processing error.
	 *
	 * @see  GetErrorReasonTag
	 * @see  TransactionResult#getProviderErrorCode()
	 * @see  TransactionResult#getProviderErrorMessage()
	 * @see  TransactionResult#getErrorCode()
	 */
	public static String getErrorReason() throws JspException {
		return getErrorReason("${payment:getErrorReason()}", getRequest());
	}

	static boolean isAccepted(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();
		if(authorizationResult == null) return false;
		switch(authorizationResult.getCommunicationResult()) {
			case LOCAL_ERROR:
			case IO_ERROR:
			case GATEWAY_ERROR:
				return false;
			case SUCCESS:
				switch(authorizationResult.getApprovalResult()) {
					case DECLINED:
					case HOLD:
						return false;
					case APPROVED:
						return true;
					default:
						throw new JspTagException("Unexpected approval result: " + authorizationResult.getApprovalResult());
				}
			default:
				throw new JspTagException("Unexpected communication result: " + authorizationResult.getCommunicationResult());
		}
	}

	/**
	 * Determines if the payment was accepted (approved).
	 *
	 * @see  AcceptedTag
	 * @see  AuthorizationResult#getCommunicationResult()
	 * @see  CommunicationResult#SUCCESS
	 * @see  AuthorizationResult#getApprovalResult()
	 * @see  ApprovalResult#APPROVED
	 */
	public static boolean isAccepted() throws JspException {
		return isAccepted("${payment:isAccepted()}", getRequest());
	}

	// Java 9: module-private
	public static String getAuthorizationCode(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();
		return (authorizationResult == null) ? null : authorizationResult.getApprovalCode();
	}

	/**
	 * Gets the authorization code to display to the customer.
	 *
	 * @see  GetAuthorizationCodeTag
	 * @see  AuthorizationResult#getApprovalCode()
	 */
	public static String getAuthorizationCode() throws JspException {
		return getAuthorizationCode("${payment:getAuthorizationCode()}", getRequest());
	}

	// Java 9: module-private
	public static String getTransactionId(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();
		return (authorizationResult == null) ? null : authorizationResult.getProviderUniqueId();
	}

	/**
	 * Gets the per-processor unique transaction ID.
	 *
	 * @see  GetTransactionIdTag
	 * @see  AuthorizationResult#getProviderUniqueId()
	 */
	public static String getTransactionId() throws JspException {
		return getTransactionId("${payment:getTransactionId()}", getRequest());
	}

	static boolean isHeld(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();
		if(authorizationResult == null) return false;
		switch(authorizationResult.getCommunicationResult()) {
			case LOCAL_ERROR:
			case IO_ERROR:
			case GATEWAY_ERROR:
				return false;
			case SUCCESS:
				switch(authorizationResult.getApprovalResult()) {
					case DECLINED:
					case APPROVED:
						return false;
					case HOLD:
						return true;
					default:
						throw new JspTagException("Unexpected approval result: " + authorizationResult.getApprovalResult());
				}
			default:
				throw new JspTagException("Unexpected communication result: " + authorizationResult.getCommunicationResult());
		}
	}

	/**
	 * Determines if the payment was held (pending some sort of review).
	 *
	 * @see  HeldTag
	 * @see  AuthorizationResult#getCommunicationResult()
	 * @see  CommunicationResult#SUCCESS
	 * @see  AuthorizationResult#getApprovalResult()
	 * @see  ApprovalResult#HOLD
	 */
	public static boolean isHeld() throws JspException {
		return isHeld("${payment:isHeld()}", getRequest());
	}

	// Java 9: module-private
	public static String getReviewReason(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();
		AuthorizationResult.ReviewReason reviewReason = (authorizationResult == null) ? null : authorizationResult.getReviewReason();
		return (reviewReason == null) ? null : reviewReason.toString();
	}

	/**
	 * Gets the review reason for a transaction that has been placed on hold.
	 *
	 * @see  GetReviewReasonTag
	 * @see  AuthorizationResult#getReviewReason()
	 */
	public static String getReviewReason() throws JspException {
		return getReviewReason("${payment:getReviewReason()}", getRequest());
	}

	static boolean isRejected(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();
		if(authorizationResult == null) return false;
		switch(authorizationResult.getCommunicationResult()) {
			case LOCAL_ERROR:
			case IO_ERROR:
			case GATEWAY_ERROR:
				return false;
			case SUCCESS:
				switch(authorizationResult.getApprovalResult()) {
					case DECLINED:
						return true;
					case HOLD:
					case APPROVED:
						return false;
					default:
						throw new JspTagException("Unexpected approval result: " + authorizationResult.getApprovalResult());
				}
			default:
				throw new JspTagException("Unexpected communication result: " + authorizationResult.getCommunicationResult());
		}
	}

	/**
	 * Determines if the payment was rejected (declined).
	 *
	 * @see  RejectedTag
	 * @see  AuthorizationResult#getCommunicationResult()
	 * @see  CommunicationResult#SUCCESS
	 * @see  AuthorizationResult#getApprovalResult()
	 * @see  ApprovalResult#DECLINED
	 */
	public static boolean isRejected() throws JspException {
		return isRejected("${payment:isRejected()}", getRequest());
	}

	// Java 9: module-private
	public static String getRejectedReason(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();
		AuthorizationResult.DeclineReason declineReason = (authorizationResult == null) ? null : authorizationResult.getDeclineReason();
		return (declineReason == null) ? null : declineReason.toString();
	}

	/**
	 * Gets the rejected reason for a payment attempt.
	 *
	 * @see  GetRejectedReasonTag
	 * @see  AuthorizationResult#getDeclineReason()
	 */
	public static String getRejectedReason() throws JspException {
		return getRejectedReason("${payment:getRejectedReason()}", getRequest());
	}

	// Java 9: module-private
	public static String getCvvResult(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult.CvvResult cvvResult = paymentTag.getAuthorizationResult().getCvvResult();
		return (cvvResult == null) ? null : cvvResult.toString();
	}

	/**
	 * Gets the CVV2 (card security code) verification result.
	 *
	 * @see  GetCvvResultTag
	 * @see  AuthorizationResult#getCvvResult()
	 */
	public static String getCvvResult() throws JspException {
		return getCvvResult("${payment:getCvvResult()}", getRequest());
	}

	// Java 9: module-private
	public static String getAvsResult(String fromName, ServletRequest request) throws JspException {
		PaymentTag paymentTag = PaymentTag.requireCurrent(fromName, request);
		AuthorizationResult.AvsResult avsResult = paymentTag.getAuthorizationResult().getAvsResult();
		return (avsResult == null) ? null : avsResult.toString();
	}

	/**
	 * Gets the AVS (address verification system) result.
	 *
	 * @see  GetAvsResultTag
	 * @see  AuthorizationResult#getAvsResult()
	 */
	public static String getAvsResult() throws JspException {
		return getAvsResult("${payment:getAvsResult()}", getRequest());
	}
}
