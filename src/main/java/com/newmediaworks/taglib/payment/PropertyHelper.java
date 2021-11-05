/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
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
 * along with nmw-payment-taglib.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.payment;

import com.aoapps.lang.function.BiConsumerE;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Provides utilities to assign properties to the correct tags when the properties exist in multiple places.
 * This is to reduce code duplication.
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
// Java 9: module-private
public abstract class PropertyHelper {

	/** Make no instances. */
	private PropertyHelper() {throw new AssertionError();}

	/**
	 * Sets a value on a {@link StoreCreditCardTag} or {@link CreditCardTag} while verifying correct tag nesting.
	 */
	// Java 9: module-private
	public static <V> void setCardProperty(
		V value,
		String fromName,
		ServletRequest request,
		BiConsumer<? super StoreCreditCardTag, ? super V> storeCreditCardSetter,
		BiConsumerE<? super CreditCardTag, ? super V, ? extends JspException> creditCardSetter
	) throws JspException {
		Optional<StoreCreditCardTag> storeCreditCardTag = StoreCreditCardTag.getCurrent(request);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardSetter.accept(storeCreditCardTag.get(), value);
		} else {
			Optional<CreditCardTag> creditCardTag = CreditCardTag.getCurrent(request);
			if(creditCardTag.isPresent()) {
				creditCardSetter.accept(creditCardTag.get(), value);
			} else {
				throw new JspTagException(fromName + " must be within " + StoreCreditCardTag.TAG_NAME + " or " + CreditCardTag.TAG_NAME);
			}
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
		ServletRequest request,
		BiConsumer<? super StoreCreditCardTag, ? super V> storeCreditCardSetter,
		BiConsumerE<? super CreditCardTag, ? super V, ? extends JspException> creditCardSetter,
		BiConsumerE<? super ShippingAddressTag, ? super V, ? extends JspException> shippingAddressSetter
	) throws JspException {
		Optional<StoreCreditCardTag> storeCreditCardTag = StoreCreditCardTag.getCurrent(request);
		if(storeCreditCardTag.isPresent()) {
			storeCreditCardSetter.accept(storeCreditCardTag.get(), value);
		} else {
			Optional<CreditCardTag> creditCardTag = CreditCardTag.getCurrent(request);
			if(creditCardTag.isPresent()) {
				creditCardSetter.accept(creditCardTag.get(), value);
			} else {
				Optional<ShippingAddressTag> shippingAddressTag = ShippingAddressTag.getCurrent(request);
				if(shippingAddressTag.isPresent()) {
					shippingAddressSetter.accept(shippingAddressTag.get(), value);
				} else {
					throw new JspTagException(fromName + " must be within " + StoreCreditCardTag.TAG_NAME + ", " + CreditCardTag.TAG_NAME + ", or " + ShippingAddressTag.TAG_NAME);
				}
			}
		}
	}
}
