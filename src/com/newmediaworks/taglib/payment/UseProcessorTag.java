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

import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.authorizeNet.AuthorizeNet;
import com.aoindustries.creditcards.payflowPro.PayflowPro;
import com.aoindustries.creditcards.sagePayments.SagePayments;
import com.aoindustries.creditcards.stripe.Stripe;
import com.aoindustries.creditcards.test.TestMerchantServicesProvider;
import com.aoindustries.creditcards.usaepay.USAePay;
import com.aoindustries.lang.ObjectUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Sets the processor.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class UseProcessorTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    /** The previously created processors are reused. */
    private final static List<SagePayments> sagePayments = new ArrayList<SagePayments>();
    private static SagePayments getSagePayments(String merchantID, String merchantKey) {
        synchronized(sagePayments) {
            for(SagePayments sp : sagePayments) {
                if(
                    sp.getMerchantId().equals(merchantID)
                    && sp.getMerchantKey().equals(merchantKey)
                ) return sp;
            }
            SagePayments sp = new SagePayments("Sage", merchantID, merchantKey);
            sagePayments.add(sp);
            return sp;
        }
    }

    /** The previously created processors are reused. */
    private final static List<TestMerchantServicesProvider> testMerchantServicesProviders = new ArrayList<TestMerchantServicesProvider>();
    private static TestMerchantServicesProvider getTestMerchantServicesProvider(byte errorChance, byte rejectionChance) {
        synchronized(testMerchantServicesProviders) {
            for(TestMerchantServicesProvider tmsp : testMerchantServicesProviders) {
                if(
                    tmsp.getErrorChance()==errorChance
                    && tmsp.getDeclineChance()==rejectionChance
                ) return tmsp;
            }
            TestMerchantServicesProvider tmsp = new TestMerchantServicesProvider("Test", errorChance, rejectionChance);
            testMerchantServicesProviders.add(tmsp);
            return tmsp;
        }
    }

    /** The previously created processors are reused. */
    private final static List<PayflowPro> payflowPros = new ArrayList<PayflowPro>();
    private static PayflowPro getPayflowPro(String user, String vendor, String partner, String password, ServletContext servletContext) {
        synchronized(payflowPros) {
            for(PayflowPro payflowPro : payflowPros) {
                if(
                    ObjectUtils.equals(payflowPro.getUser(), user)
                    && ObjectUtils.equals(payflowPro.getVendor(), vendor)
                    && ObjectUtils.equals(payflowPro.getPartner(), partner)
                    && ObjectUtils.equals(payflowPro.getPassword(), password)
                ) return payflowPro;
            }
            PayflowPro payflowPro = new PayflowPro("Payflow Pro", user, vendor, partner, password);
            payflowPros.add(payflowPro);
            return payflowPro;
        }
    }

    /** The previously created processors are reused. */
    private final static List<USAePay> usaePays = new ArrayList<USAePay>();
    private static USAePay getUSAePay(String postUrl, String key, String pin) {
        synchronized(usaePays) {
            for(USAePay usaePay : usaePays) {
                if(
                    ObjectUtils.equals(usaePay.getPostUrl(), postUrl)
                    && ObjectUtils.equals(usaePay.getKey(), key)
                    && ObjectUtils.equals(usaePay.getPin(), pin)
                ) return usaePay;
            }
            USAePay usaePay = new USAePay("USAePay", postUrl, key, pin);
            usaePays.add(usaePay);
            return usaePay;
        }
    }

    /** The previously created processors are reused. */
    private final static List<AuthorizeNet> authorizeNets = new ArrayList<AuthorizeNet>();
    private static AuthorizeNet getAuthorizeNet(String x_login, String x_tran_key) {
        synchronized(authorizeNets) {
            for(AuthorizeNet authorizeNet : authorizeNets) {
                if(
                    ObjectUtils.equals(authorizeNet.getX_login(), x_login)
                    && ObjectUtils.equals(authorizeNet.getX_tran_key(), x_tran_key)
                ) return authorizeNet;
            }
            AuthorizeNet authorizeNet = new AuthorizeNet("Authorize.Net", x_login, x_tran_key);
            authorizeNets.add(authorizeNet);
            return authorizeNet;
        }
    }

    /** The previously created processors are reused. */
    private final static List<Stripe> stripes = new ArrayList<Stripe>();
    private static Stripe getStripe(String apiKey) {
        synchronized(stripes) {
            for(Stripe stripe : stripes) {
                if(
                    ObjectUtils.equals(stripe.getApiKey(), apiKey)
                ) return stripe;
            }
            Stripe stripe = new Stripe("Stripe", apiKey);
            stripes.add(stripe);
            return stripe;
        }
    }

	// Set by nested tags
    private String connectorName;
    final private Map<String,String> parameters = new HashMap<String,String>();

    public UseProcessorTag() {
        init();
    }

    /**
     * Sets the processor at end tag because connector name and parameters are set in nested tags.
     */
    @Override
    public int doEndTag() throws JspException {
        if(connectorName==null) throw new JspException("connectorName not set");

        MerchantServicesProvider provider;
        if("Sage".equalsIgnoreCase(connectorName)) {
            String merchantID = parameters.get("merchantID");
            if(merchantID==null || merchantID.length()==0) throw new JspException("merchantID parameter required");
            String merchantKey = parameters.get("merchantKey");
            if(merchantKey==null || merchantKey.length()==0) throw new JspException("merchantKey parameter required");
            provider = getSagePayments(merchantID, merchantKey);
        } else if("Test".equalsIgnoreCase(connectorName)) {
            String errorChance = parameters.get("errorChance");
            String rejectionChance = parameters.get("rejectionChance");
            provider = getTestMerchantServicesProvider(
                errorChance==null || errorChance.length()==0 ? (byte)10 : Byte.parseByte(errorChance),
                rejectionChance==null || rejectionChance.length()==0 ? (byte)20 : Byte.parseByte(rejectionChance)
            );
        } else if("Payflow Pro".equalsIgnoreCase(connectorName)) {
            provider = getPayflowPro(
                parameters.get("user"),
                parameters.get("vendor"),
                parameters.get("partner"),
                parameters.get("password"),
                pageContext.getServletContext()
            );
        } else if("USAePay".equalsIgnoreCase(connectorName)) {
            provider = getUSAePay(
                parameters.get("postUrl"),
                parameters.get("key"),
                parameters.get("pin")
            );
        } else if("Authorize.Net".equalsIgnoreCase(connectorName)) {
            provider = getAuthorizeNet(
                parameters.get("x_login"),
                parameters.get("x_tran_key")
            );
        } else if("Stripe".equalsIgnoreCase(connectorName)) {
            provider = getStripe(
                parameters.get("apiKey")
            );
        } else {
            throw new JspException("Unsupported connectorName: "+connectorName);
        }

        // Set in the request attributes
        pageContext.getRequest().setAttribute(Constants.processor, provider);

        // Get ready for the next iteration
        init();
        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    private void init() {
        connectorName = null;
        parameters.clear();
    }
    
    /**
     * Sets the connector name.
     * 
     * @throws JspException if name already set.
     */
    void setConnectorName(String connectorName) throws JspException {
        if(this.connectorName!=null) throw new JspException("connectorName already set");
        this.connectorName = connectorName;
    }
    
    /**
     * Adds a parameter.
     * 
     * @throws JspException if name already set.
     */
    void addParameter(String name, String value) throws JspException {
        if(this.parameters.containsKey(name)) throw new JspException("parameter already set: "+name);
        this.parameters.put(name, value);
    }
}
