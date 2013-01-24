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
import com.aoindustries.creditcards.CaptureResult;
import com.aoindustries.creditcards.MerchantServicesProvider;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The capture tag captures the funds from a capture=false payment.  All values are set through nested tags.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class CaptureTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    // Set by nested tags
    private String transactionId;

    /** The result of the processing. */
    private CaptureResult captureResult;

    public CaptureTag() {
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
        transactionId = null;
        captureResult = null;
    }
    
    void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    void process() throws JspException {
        // Make sure the processor is set
        MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
        if(processor==null) throw new JspException("processor not set, please set processor with the useProcessor tag first");

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
                null
            )
        );
    }
    
    CaptureResult getCaptureResult() {
        return captureResult;
    }
}
