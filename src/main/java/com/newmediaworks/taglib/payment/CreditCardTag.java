/*
 * nmw-payment-taglib - JSP taglib encapsulating the AO Payments API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2019, 2020, 2021, 2022  New Media Works
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

import com.aoapps.lang.Strings;
import com.aoapps.payments.CreditCard;
import com.aoapps.servlet.attribute.ScopeEE;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Provides the credit card details to a {@link PaymentTag}.
 *
 * @see  CreditCard
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class CreditCardTag extends BodyTagSupport implements TryCatchFinally {

  public static final String TAG_NAME = "<payment:creditCard>";

  /**
   * The name of the request-scope attribute containing the current credit card tag.
   */
  private static final ScopeEE.Request.Attribute<CreditCardTag> REQUEST_ATTRIBUTE_NAME =
      ScopeEE.REQUEST.attribute(CreditCardTag.class.getName());

  // Java 9: module-private
  public static Optional<CreditCardTag> getCurrent(ServletRequest request) {
    return Optional.ofNullable(REQUEST_ATTRIBUTE_NAME.context(request).get());
  }

  // Java 9: module-private
  public static CreditCardTag requireCurrent(String fromName, ServletRequest request) throws JspException {
    return getCurrent(request).orElseThrow(
        () -> new JspTagException(fromName + " must be within " + TAG_NAME)
    );
  }

  public CreditCardTag() {
    init();
  }

  private static final long serialVersionUID = 2L;

  private transient boolean requestAttributeSet;

  public void setCardNumber(String cardNumber) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setCardNumber(Strings.trimNullIfEmpty(cardNumber));
  }

  public void setMaskedCardNumber(String maskedCardNumber) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setMaskedCardNumber(Strings.trimNullIfEmpty(maskedCardNumber));
  }

  public void setExpirationMonth(byte expirationMonth) throws IllegalArgumentException, JspException {
    CreditCard.validateExpirationMonth(expirationMonth, false);
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setExpirationMonth(expirationMonth);
  }

  public void setExpirationYear(short expirationYear) throws IllegalArgumentException, JspException {
    CreditCard.validateExpirationYear(expirationYear, false);
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setExpirationYear(expirationYear);
  }

  public void setExpirationDate(String expirationDate) throws IllegalArgumentException, JspException {
    expirationDate = Strings.trimNullIfEmpty(expirationDate);
    if (expirationDate == null) {
      CreditCard creditCard = PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard();
      creditCard.setExpirationMonth(CreditCard.UNKNOWN_EXPIRATION_MONTH);
      creditCard.setExpirationYear(CreditCard.UNKNOWN_EXPIRATION_YEAR);
    } else {
      int slashPos = expirationDate.indexOf('/');
      if (slashPos == -1) {
        throw new IllegalArgumentException("Invalid expirationDate, unable to find / : " + expirationDate);
      }

      String expirationMonthString = expirationDate.substring(0, slashPos).trim();
      byte expirationMonth;
      try {
        expirationMonth = Byte.parseByte(expirationMonthString);
      } catch (NumberFormatException err) {
        throw new IllegalArgumentException("Invalid expirationMonth: " + expirationMonthString, err);
      }
      CreditCard.validateExpirationMonth(expirationMonth, false);

      String expirationYearString = expirationDate.substring(slashPos + 1).trim();
      short expirationYear;
      try {
        expirationYear = Short.parseShort(expirationYearString);
      } catch (NumberFormatException err) {
        throw new IllegalArgumentException("Invalid expirationYear: " + expirationYearString, err);
      }
      CreditCard.validateExpirationYear(expirationYear, false);

      CreditCard creditCard = PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard();
      creditCard.setExpirationMonth(expirationMonth);
      creditCard.setExpirationYear(expirationYear);
    }
  }

  public void setCardCode(String cardCode) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setCardCode(Strings.trimNullIfEmpty(cardCode));
  }

  public void setCreditCardGUID(String creditCardGUID) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setProviderUniqueId(Strings.trimNullIfEmpty(creditCardGUID));
  }

  public void setFirstName(String firstName) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setFirstName(Strings.trimNullIfEmpty(firstName));
  }

  public void setLastName(String lastName) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setLastName(Strings.trimNullIfEmpty(lastName));
  }

  public void setCompanyName(String companyName) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setCompanyName(Strings.trimNullIfEmpty(companyName));
  }

  public void setEmail(String email) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setEmail(Strings.trimNullIfEmpty(email));
  }

  public void setPhone(String phone) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setPhone(Strings.trimNullIfEmpty(phone));
  }

  public void setFax(String fax) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setFax(Strings.trimNullIfEmpty(fax));
  }

  public void setCustomerId(String customerId) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setCustomerId(Strings.trimNullIfEmpty(customerId));
  }

  public void setCustomerTaxId(String customerTaxId) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setCustomerTaxId(Strings.trimNullIfEmpty(customerTaxId));
  }

  public void setStreetAddress1(String streetAddress1) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setStreetAddress1(Strings.trimNullIfEmpty(streetAddress1));
  }

  public void setStreetAddress2(String streetAddress2) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setStreetAddress2(Strings.trimNullIfEmpty(streetAddress2));
  }

  public void setCity(String city) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setCity(Strings.trimNullIfEmpty(city));
  }

  public void setState(String state) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setState(Strings.trimNullIfEmpty(state));
  }

  public void setPostalCode(String postalCode) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setPostalCode(Strings.trimNullIfEmpty(postalCode));
  }

  public void setCountryCode(String countryCode) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setCountryCode(Strings.trimNullIfEmpty(countryCode));
  }

  public void setComment(String comment) throws JspException {
    PaymentTag.requireCurrent(TAG_NAME, pageContext.getRequest()).getCreditCard()
        .setComments(Strings.trimNullIfEmpty(comment));
  }

  private void init() {
    requestAttributeSet = false;
  }

  @Override
  public int doStartTag() throws JspException {
    ServletRequest request = pageContext.getRequest();
    // Make sure nested in payment tag
    PaymentTag.requireCurrent(TAG_NAME, request);
    // Store this on the request
    if (getCurrent(request).isPresent()) {
      throw new JspTagException(TAG_NAME + " may not be nested within " + TAG_NAME);
    }
    REQUEST_ATTRIBUTE_NAME.context(request).set(this);
    requestAttributeSet = true;
    return EVAL_BODY_INCLUDE;
  }

  @Override
  public void doCatch(Throwable t) throws Throwable {
    throw t;
  }

  @Override
  public void doFinally() {
    if (requestAttributeSet) {
      REQUEST_ATTRIBUTE_NAME.context(pageContext.getRequest()).remove();
    }
    init();
  }
}
