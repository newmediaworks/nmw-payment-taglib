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

import com.aoindustries.creditcards.AuthorizationResult;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Gets the authorization code for a payment attempt.  Must be nested in an accepted tag.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class GetAuthorizationCodeTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    public GetAuthorizationCodeTag() {
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            AcceptedTag acceptedTag = (AcceptedTag)findAncestorWithClass(this, AcceptedTag.class);
            if(acceptedTag==null) throw new JspException("getAuthorizationCode tag must be within an accepted tag");

            PaymentTag paymentTag = (PaymentTag)findAncestorWithClass(acceptedTag, PaymentTag.class);
            if(paymentTag==null) throw new JspException("accepted tag must be within a payment tag");
            AuthorizationResult authorizationResult = paymentTag.getAuthorizationResult();

            pageContext.getOut().write(authorizationResult.getApprovalCode());

            return SKIP_BODY;
        } catch(IOException err) {
            throw new JspException(err);
        }
    }
}
