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
import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.VoidResult;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The void tag voids the funds from a transaction.  All values are set through nested tags.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class VoidTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    // Set by nested tags
    private String transactionId;

    /** The result of the processing. */
    private VoidResult voidResult;

    public VoidTag() {
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
        voidResult = null;
    }
    
    void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    void process() throws JspException {
        // Make sure the processor is set
        MerchantServicesProvider processor = (MerchantServicesProvider)pageContext.getRequest().getAttribute(Constants.processor);
        if(processor==null) throw new JspException("processor not set, please set processor with the useProcessor tag first");

        voidResult = processor.voidTransaction(
            new Transaction(
                processor.getProviderId(),
                null,
                null,
                null,
                null,
                0,
                null,
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
                ),
                0,
                null,
                null,
                0,
                null,
                null,
                null
            )
        );
    }
    
    VoidResult getVoidResult() {
        return voidResult;
    }
}
