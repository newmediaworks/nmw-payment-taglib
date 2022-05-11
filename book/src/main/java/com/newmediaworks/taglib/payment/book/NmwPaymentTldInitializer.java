/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
 * Copyright (C) 2019, 2020, 2021, 2022  New Media Works
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

package com.newmediaworks.taglib.payment.book;

import com.semanticcms.tagreference.TagReferenceInitializer;
import javax.servlet.ServletContainerInitializer;

/**
 * Initializes a tag reference during {@linkplain ServletContainerInitializer application start-up}.
 */
public class NmwPaymentTldInitializer extends TagReferenceInitializer {

  /**
   * Parses the TLD file.
   */
  public NmwPaymentTldInitializer() {
    super(
        Maven.properties.getProperty("documented.name") + " Reference",
        "Taglib Reference",
        "/payment-taglib",
        "/nmw-payment.tld",
        true,
        Maven.properties.getProperty("documented.javadoc.link.javase"),
        Maven.properties.getProperty("documented.javadoc.link.javaee"),
        // Self
        "com.newmediaworks.taglib.payment", Maven.properties.getProperty("project.url") + "apidocs/com.newmediaworks.taglib.payment/"
    );
  }
}
