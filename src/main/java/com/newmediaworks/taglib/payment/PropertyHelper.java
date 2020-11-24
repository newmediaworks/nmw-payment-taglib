/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2020  New Media Works
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

import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspTag;

/**
 * Provides utilities to assign properties to the correct tags when the properties exist in multiple places.
 * This is to reduce code duplication.
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
class PropertyHelper {

	private PropertyHelper() {}

	/**
	 * Sets a value on a {@link StoreCreditCardTag} or {@link CreditCardTag} while verifying correct tag nesting.
	 */
	static <V> void setCardProperty(
		V value,
		String fromName,
		JspTag from,
		BiConsumer<? super StoreCreditCardTag, ? super V> storeCreditCardSetter,
		BiConsumer<? super PaymentTag, ? super V> creditCardSetter
	) throws JspTagException {
		// Java 9: ifPresentOrElse
		Optional<StoreCreditCardTag> storeCreditCardTag = JspTagUtils.findAncestor(from, StoreCreditCardTag.class);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardSetter.accept(storeCreditCardTag.get(), value);
		} else {
			CreditCardTag creditCardTag = JspTagUtils.requireAncestor(fromName, from, StoreCreditCardTag.TAG_NAME + " or " + CreditCardTag.TAG_NAME, CreditCardTag.class);
			PaymentTag paymentTag = JspTagUtils.requireAncestor(CreditCardTag.TAG_NAME, creditCardTag, PaymentTag.TAG_NAME, PaymentTag.class);
			creditCardSetter.accept(paymentTag, value);
		}
	}

	/**
	 * Sets a value on a {@link StoreCreditCardTag}, {@link CreditCardTag}, or {@link ShippingAddressTag} while
	 * verifying correct tag nesting.
	 */
	static <V> void setAddressProperty(
		V value,
		String fromName,
		JspTag from,
		BiConsumer<? super StoreCreditCardTag, ? super V> storeCreditCardSetter,
		BiConsumer<? super PaymentTag, ? super V> creditCardSetter,
		BiConsumer<? super PaymentTag, ? super V> shippingAddressSetter
	) throws JspTagException {
		// Java 9: ifPresentOrElse
		Optional<StoreCreditCardTag> storeCreditCardTag = JspTagUtils.findAncestor(from, StoreCreditCardTag.class);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardSetter.accept(storeCreditCardTag.get(), value);
		} else {
			Optional<CreditCardTag> creditCardTag = JspTagUtils.findAncestor(from, CreditCardTag.class);
			if(creditCardTag.isPresent()) {
				PaymentTag paymentTag = JspTagUtils.requireAncestor(CreditCardTag.TAG_NAME, creditCardTag.get(), PaymentTag.TAG_NAME, PaymentTag.class);
				creditCardSetter.accept(paymentTag, value);
			} else {
				ShippingAddressTag shippingAddressTag = JspTagUtils.requireAncestor(fromName, from, StoreCreditCardTag.TAG_NAME + ", " + CreditCardTag.TAG_NAME + ", or " + ShippingAddressTag.TAG_NAME, ShippingAddressTag.class);
				PaymentTag paymentTag = JspTagUtils.requireAncestor(ShippingAddressTag.TAG_NAME, shippingAddressTag, PaymentTag.TAG_NAME, PaymentTag.class);
				shippingAddressSetter.accept(paymentTag, value);
			}
		}
	}
}
