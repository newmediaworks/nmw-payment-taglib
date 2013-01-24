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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides a customer ID to either a <code>StoreCreditCardTag</code> or <code>CreditCardTag</code>.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class CustomerIdTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    public CustomerIdTag() {
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        String customerId = getBodyContent().getString().trim();
        StoreCreditCardTag storeCreditCardTag = (StoreCreditCardTag)findAncestorWithClass(this, StoreCreditCardTag.class);
        if(storeCreditCardTag!=null) {
            storeCreditCardTag.setCustomerId(customerId);
        } else {
            CreditCardTag creditCardTag = (CreditCardTag)findAncestorWithClass(this, CreditCardTag.class);
            if(creditCardTag!=null) {
                PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(creditCardTag, PaymentTag.class);
                if(paymentTag==null) throw new JspException("creditCard tag must be within payment tag");
                paymentTag.setCreditCardCustomerId(customerId);
            } else {
                throw new JspException("customerId tag must be within either a storeCreditCard tag or a creditCard tag");
            }
        }

        return EVAL_PAGE;
    }
}
