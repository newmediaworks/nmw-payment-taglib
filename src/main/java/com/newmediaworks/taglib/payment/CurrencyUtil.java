/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
 * Copyright (C) 2013, 2019, 2021  New Media Works
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

import com.aoindustries.lang.Strings;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Utilities for working with {@link Currency}.
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
// Java 9: module-private
public final class CurrencyUtil {

	/**
	 * Parsing for a currency.  Strips all <code>'$'</code> or <code>','</code>
	 * then parses with {@link BigDecimal#BigDecimal(java.lang.String)}.
	 * <p>
	 * TODO: This is a US-locale specific implementation.
	 * Should probably not make public again until a more locale-aware implementation is provided.
	 * </p>
	 *
	 * @param  value  When {@code null} or empty (after trimming), returns {@code null}
	 */
	// Java 9: module-private
	public static BigDecimal parseCurrency(String value) {
		value = Strings.trimNullIfEmpty(value);
		if(value == null) return null;
		return new BigDecimal(value.replace("$", "").replace(",", ""));
	}

	/**
	 * Make no instances.
	 */
	private CurrencyUtil() {
	}
}
