/*
 * new-payment-taglib - Java taglib encapsulating the ao-credit-cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013  New Media Works
 *     info@newmediaworks.com
 *     PO BOX 853
 *     Napa, CA 94559
 *
 * This file is part of new-payment-taglib.
 *
 * new-payment-taglib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * new-payment-taglib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with new-payment-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.payment;

import com.aoindustries.creditcards.TransactionResult;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Includes the body when the transaction when the payment, capture, or void resulted in an error.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class ErrorTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    public ErrorTag() {
    }

    /**
     * Gets the transaction result from one of the outer payment, capture, or void tags.
     */
    TransactionResult getTransactionResult() throws JspException {
        PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(this, PaymentTag.class);
        if(paymentTag!=null) return paymentTag.getAuthorizationResult();

        CaptureTag captureTag = (CaptureTag)findAncestorWithClass(this, CaptureTag.class);
        if(captureTag!=null) return captureTag.getCaptureResult();

        VoidTag voidTag = (VoidTag)findAncestorWithClass(this, VoidTag.class);
        if(voidTag!=null) return voidTag.getVoidResult();

        throw new JspException("error tag must be within a payment, capture, or void tag");
    }

    @Override
    public int doStartTag() throws JspException {
        TransactionResult transactionResult = getTransactionResult();

        switch(transactionResult.getCommunicationResult()) {
            case LOCAL_ERROR:
            case IO_ERROR:
            case GATEWAY_ERROR:
                return EVAL_BODY_INCLUDE;
            case SUCCESS:
                return SKIP_BODY;
            default:
                throw new JspException("Unexpected communication result: "+transactionResult.getCommunicationResult());
        }
    }
}
