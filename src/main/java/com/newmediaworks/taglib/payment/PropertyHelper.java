/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Credit Cards API.
 * Copyright (C) 2020, 2021  New Media Works
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
import com.aoindustries.util.function.BiConsumerE;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;

/**
 * Provides utilities to assign properties to the correct tags when the properties exist in multiple places.
 * This is to reduce code duplication.
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
// Java 9: module-private
public class PropertyHelper {

	private PropertyHelper() {}

	/**
	 * Sets a value on a {@link StoreCreditCardTag} or {@link CreditCardTag} while verifying correct tag nesting.
	 */
	// Java 9: module-private
	public static <V> void setCardProperty(
		V value,
		String fromName,
		JspTag from,
		BiConsumer<? super StoreCreditCardTag, ? super V> storeCreditCardSetter,
		BiConsumerE<? super CreditCardTag, ? super V, ? extends JspException> creditCardSetter
	) throws JspException {
		// Java 9: ifPresentOrElse
		Optional<StoreCreditCardTag> storeCreditCardTag = JspTagUtils.findAncestor(from, StoreCreditCardTag.class);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardSetter.accept(storeCreditCardTag.get(), value);
		} else {
			CreditCardTag creditCardTag = JspTagUtils.requireAncestor(fromName, from, StoreCreditCardTag.TAG_NAME + " or " + CreditCardTag.TAG_NAME, CreditCardTag.class);
			creditCardSetter.accept(creditCardTag, value);
		}
	}

	/**
	 * Sets a value on a {@link StoreCreditCardTag}, {@link CreditCardTag}, or {@link ShippingAddressTag} while
	 * verifying correct tag nesting.
	 */
	// Java 9: module-private
	public static <V> void setAddressProperty(
		V value,
		String fromName,
		JspTag from,
		BiConsumer<? super StoreCreditCardTag, ? super V> storeCreditCardSetter,
		BiConsumerE<? super CreditCardTag, ? super V, ? extends JspException> creditCardSetter,
		BiConsumerE<? super ShippingAddressTag, ? super V, ? extends JspException> shippingAddressSetter
	) throws JspException {
		// Java 9: ifPresentOrElse
		Optional<StoreCreditCardTag> storeCreditCardTag = JspTagUtils.findAncestor(from, StoreCreditCardTag.class);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardSetter.accept(storeCreditCardTag.get(), value);
		} else {
			Optional<CreditCardTag> creditCardTag = JspTagUtils.findAncestor(from, CreditCardTag.class);
			if(creditCardTag.isPresent()) {
				creditCardSetter.accept(creditCardTag.get(), value);
			} else {
				ShippingAddressTag shippingAddressTag = JspTagUtils.requireAncestor(fromName, from, StoreCreditCardTag.TAG_NAME + ", " + CreditCardTag.TAG_NAME + ", or " + ShippingAddressTag.TAG_NAME, ShippingAddressTag.class);
				shippingAddressSetter.accept(shippingAddressTag, value);
			}
		}
	}
}
