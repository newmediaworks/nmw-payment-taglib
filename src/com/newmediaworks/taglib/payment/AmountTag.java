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

import java.math.BigDecimal;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides an amount to a <code>PaymentTag</code>.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class AmountTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    public AmountTag() {
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        String amountString = getBodyContent().getString().trim();
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountString);
        } catch(NumberFormatException err) {
            throw new JspException("Invalid amount: "+amountString, err);
        }
        PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(this, PaymentTag.class);
        if(paymentTag!=null) {
            paymentTag.setAmount(amount);
        } else {
            throw new JspException("amount tag must be within a payment tag");
        }

        return EVAL_PAGE;
    }
}
