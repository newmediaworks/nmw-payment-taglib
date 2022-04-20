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

package com.newmediaworks.taglib.payment.legacy;

import com.aoapps.encoding.MediaType;
import com.aoapps.encoding.taglib.legacy.EncodingBufferedBodyTag;
import com.aoapps.io.buffer.BufferResult;
import com.aoapps.lang.Strings;
import com.aoapps.payments.CreditCard;
import com.newmediaworks.taglib.payment.CreditCardTag;
import static com.newmediaworks.taglib.payment.PhoneTag.TAG_NAME;
import com.newmediaworks.taglib.payment.PropertyHelper;
import com.newmediaworks.taglib.payment.StoreCreditCardTag;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;

/**
 * Provides the phone number of the customer to either a {@link StoreCreditCardTag}
 * or {@link CreditCardTag}.
 *
 * @see  CreditCard#setPhone(java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class PhoneTag extends EncodingBufferedBodyTag {

/* SimpleTag only:
  public static final String TAG_NAME = "<payment:phone>";
/**/

  public PhoneTag() {
    init();
  }

  @Override
  public MediaType getContentType() {
    return MediaType.TEXT;
  }

  @Override
  public MediaType getOutputType() {
    return null;
  }

/* BodyTag only: */
  private static final long serialVersionUID = 2L;
/**/

  private String value;
  public void setValue(String value) {
    this.value = Strings.trimNullIfEmpty(value);
  }

  private void init() {
    value = null;
  }

  @Override
/* BodyTag only: */
  protected int doStartTag(Writer out) throws JspException, IOException {
    return (value != null) ? SKIP_BODY : EVAL_BODY_BUFFERED;
/**/
/* SimpleTag only:
  protected void invoke(JspFragment body, MediaValidator captureValidator) throws JspException, IOException {
    if (value == null) {
      super.invoke(body, captureValidator);
    }
/**/
  }

  @Override
/* BodyTag only: */
  protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
/* SimpleTag only:
  protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
    PageContext pageContext = (PageContext)getJspContext();
/**/
    PropertyHelper.setCardProperty(
      (value != null) ? value : capturedBody.trim().toString(),
      TAG_NAME,
      pageContext.getRequest(),
      StoreCreditCardTag::setPhone,
      CreditCardTag::setPhone
    );
/* BodyTag only: */
    return EVAL_PAGE;
/**/
  }

/* BodyTag only: */
  @Override
  public void doFinally() {
    try {
      init();
    } finally {
      super.doFinally();
    }
  }
/**/
}
