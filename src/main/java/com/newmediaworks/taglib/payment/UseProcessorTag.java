/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019, 2020, 2021  New Media Works
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

import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.authorizeNet.AuthorizeNet;
import com.aoindustries.creditcards.payflowPro.PayflowPro;
import com.aoindustries.creditcards.stripe.Stripe;
import com.aoindustries.creditcards.test.TestMerchantServicesProvider;
import com.aoindustries.creditcards.usaepay.USAePay;
import com.aoindustries.lang.Coercion;
import com.aoindustries.lang.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Selects the processor that will be used for subsequent transactions.
 *
 * @see  MerchantServicesProvider
 * @see  TestMerchantServicesProvider
 * @see  PayflowPro
 * @see  USAePay
 * @see  AuthorizeNet
 * @see  Stripe
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class UseProcessorTag extends BodyTagSupport implements TryCatchFinally, DynamicAttributes {

	public static final String TAG_NAME = "<payment:useProcessor>";

	/**
	 * The prefix for <code>param.*</code> dynamic attributes.
	 */
	private static final String PARAM_ATTRIBUTE_PREFIX = "param.";

	/** The previously created processors are reused. */
	private final static List<TestMerchantServicesProvider> testMerchantServicesProviders = new ArrayList<>();
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
	private final static List<PayflowPro> payflowPros = new ArrayList<>();
	private static PayflowPro getPayflowPro(String user, String vendor, String partner, String password, ServletContext servletContext) {
		synchronized(payflowPros) {
			for(PayflowPro payflowPro : payflowPros) {
				if(
					Objects.equals(payflowPro.getUser(), user)
					&& Objects.equals(payflowPro.getVendor(), vendor)
					&& Objects.equals(payflowPro.getPartner(), partner)
					&& Objects.equals(payflowPro.getPassword(), password)
				) return payflowPro;
			}
			PayflowPro payflowPro = new PayflowPro("Payflow Pro", user, vendor, partner, password);
			payflowPros.add(payflowPro);
			return payflowPro;
		}
	}

	/** The previously created processors are reused. */
	private final static List<USAePay> usaePays = new ArrayList<>();
	private static USAePay getUSAePay(String postUrl, String key, String pin) {
		synchronized(usaePays) {
			for(USAePay usaePay : usaePays) {
				if(
					Objects.equals(usaePay.getPostUrl(), postUrl)
					&& Objects.equals(usaePay.getKey(), key)
					&& Objects.equals(usaePay.getPin(), pin)
				) return usaePay;
			}
			USAePay usaePay = new USAePay("USAePay", postUrl, key, pin);
			usaePays.add(usaePay);
			return usaePay;
		}
	}

	/** The previously created processors are reused. */
	private final static List<AuthorizeNet> authorizeNets = new ArrayList<>();
	private static AuthorizeNet getAuthorizeNet(String x_login, String x_tran_key) {
		synchronized(authorizeNets) {
			for(AuthorizeNet authorizeNet : authorizeNets) {
				if(
					Objects.equals(authorizeNet.getX_login(), x_login)
					&& Objects.equals(authorizeNet.getX_tran_key(), x_tran_key)
				) return authorizeNet;
			}
			AuthorizeNet authorizeNet = new AuthorizeNet("Authorize.Net", x_login, x_tran_key);
			authorizeNets.add(authorizeNet);
			return authorizeNet;
		}
	}

	/** The previously created processors are reused. */
	private final static List<Stripe> stripes = new ArrayList<>();
	private static Stripe getStripe(String apiKey) {
		synchronized(stripes) {
			for(Stripe stripe : stripes) {
				if(
					Objects.equals(stripe.getApiKey(), apiKey)
				) return stripe;
			}
			Stripe stripe = new Stripe("Stripe", apiKey);
			stripes.add(stripe);
			return stripe;
		}
	}

	/**
	 * The name of the request-scope attribute containing the current use processor tag.
	 */
	private static final String REQUEST_ATTRIBUTE_NAME = UseProcessorTag.class.getName();

	// Java 9: module-private
	public static Optional<UseProcessorTag> getCurrent(ServletRequest request) {
		return Optional.ofNullable((UseProcessorTag)request.getAttribute(REQUEST_ATTRIBUTE_NAME));
	}
	// Java 9: module-private
	public static UseProcessorTag requireCurrent(String fromName, ServletRequest request) throws JspException {
		return getCurrent(request).orElseThrow(
			() -> new JspTagException(fromName + " must be within " + TAG_NAME)
		);
	}

	public UseProcessorTag() {
		init();
	}

	private static final long serialVersionUID = 1L;

	private transient boolean requestAttributeSet;

	private String connectorName;
	/**
	 * Sets the connector name.
	 */
	public void setConnectorName(String connectorName) {
		this.connectorName = Strings.trimNullIfEmpty(connectorName);
	}

	private Map<String, String> parameters;
	/**
	 * Adds a parameter.
	 * 
	 * @throws IllegalStateException if name already set.
	 */
	public void addParameter(String name, String value) throws IllegalStateException {
		if(this.parameters.containsKey(name)) throw new IllegalStateException("parameter already set: " + name);
		this.parameters.put(name, value);
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		if(
			uri == null
			&& localName.startsWith(PARAM_ATTRIBUTE_PREFIX)
		) {
			if(value != null) {
				addParameter(
					localName.substring(PARAM_ATTRIBUTE_PREFIX.length()),
					Coercion.toString(value)
				);
			}
		} else {
			throw new JspTagException("Unexpected dynamic attribute for " + TAG_NAME + ": \"" + localName + "\", only expecting \"" + PARAM_ATTRIBUTE_PREFIX + "*\"");
		}
	}

	private void init() {
		requestAttributeSet = false;
		connectorName = null;
		if(parameters == null) {
			parameters = new HashMap<>();
		} else {
			parameters.clear();
		}
	}

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		// Store this on the request
		if(getCurrent(request).isPresent()) throw new JspTagException(TAG_NAME + " may not be nested within " + TAG_NAME);
		request.setAttribute(REQUEST_ATTRIBUTE_NAME, this);
		requestAttributeSet = true;
		return super.doStartTag();
	}

	/**
	 * Sets the processor at end tag because connector name and parameters are set in nested tags.
	 */
	@Override
	public int doEndTag() throws JspException {
		if(connectorName==null) throw new JspTagException("connectorName not set");

		MerchantServicesProvider provider;
		if("Test".equalsIgnoreCase(connectorName)) {
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
			throw new JspTagException("Unsupported connectorName: "+connectorName);
		}

		// Set in the request attributes
		pageContext.getRequest().setAttribute(Constants.processor, provider);

		return EVAL_PAGE;
	}

	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	@Override
	public void doFinally() {
		if(requestAttributeSet) {
			pageContext.getRequest().removeAttribute(REQUEST_ATTRIBUTE_NAME);
		}
		init();
	}
}
