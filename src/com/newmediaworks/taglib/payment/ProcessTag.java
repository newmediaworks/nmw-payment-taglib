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
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Triggers the processing of a <code>PaymentTag</code>.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class ProcessTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    public ProcessTag() {
    }

    @Override
    public int doStartTag() throws JspException {
        PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(this, PaymentTag.class);
        if(paymentTag!=null) {
            paymentTag.process();
        } else {
            CaptureTag captureTag = (CaptureTag)findAncestorWithClass(this, CaptureTag.class);
            if(captureTag!=null) {
                captureTag.process();
            } else {
                VoidTag voidTag = (VoidTag)findAncestorWithClass(this, VoidTag.class);
                if(voidTag!=null) {
                    voidTag.process();
                } else {
                    throw new JspException("process tag must be within a payment, capture, or void tag");
                }
            }
        }
        return SKIP_BODY;
    }
}
