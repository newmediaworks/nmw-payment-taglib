/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019, 2021, 2022, 2024  New Media Works
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

import com.aoapps.payments.MerchantServicesProvider;
import com.aoapps.servlet.attribute.ScopeEE;

/**
 * Attribute keys used in the payment taglib.
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public final class Constants {

  /** Make no instances. */
  private Constants() {
    throw new AssertionError();
  }

  /**
   * The request attribute key for the current credit card processor.
   *
   * <p>Stores an implementation of {@link MerchantServicesProvider}.</p>
   *
   * @see  UseProcessorTag
   *
   * @deprecated  Please use {@link #PROCESSOR} instead.
   */
  @Deprecated(forRemoval = true)
  public static final String processor = "PROCESSOR";

  /**
   * The request attribute key for the current credit card processor.
   *
   * <p>Stores an implementation of {@link MerchantServicesProvider}.</p>
   *
   * @see  UseProcessorTag
   */
  public static final ScopeEE.Request.Attribute<MerchantServicesProvider> PROCESSOR =
      ScopeEE.REQUEST.attribute(processor);
}
