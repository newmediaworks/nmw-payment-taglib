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
package com.newmediaworks.taglib.payment.legacy;

import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.encoding.MediaType;
import com.aoindustries.encoding.taglib.legacy.EncodingNullBodyTag;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import static com.newmediaworks.taglib.payment.GetCvvResultTag.TAG_NAME;
import com.newmediaworks.taglib.payment.PaymentTag;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;

/**
 * Gets the CVV2 (card security code) verification result.
 *
 * @see  AuthorizationResult#getCvvResult()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class GetCvvResultTag extends EncodingNullBodyTag {

/* SimpleTag only:
	public static final String TAG_NAME = "<payment:getCvvResult>";
/**/

	@Override
	public MediaType getOutputType() {
		return MediaType.TEXT;
	}

/* BodyTag only: */
	private static final long serialVersionUID = 1L;
/**/

	@Override
/* BodyTag only: */
	protected int doStartTag(Writer out) throws JspException, IOException {
/**/
/* SimpleTag only:
	protected void doTag(Writer out) throws JspException, IOException {
/**/
		PaymentTag paymentTag = JspTagUtils.requireAncestor(TAG_NAME, this, PaymentTag.TAG_NAME, PaymentTag.class);

		AuthorizationResult.CvvResult cvvResult = paymentTag.getAuthorizationResult().getCvvResult();
		if(cvvResult != null) out.write(cvvResult.toString());
/* BodyTag only: */
		return SKIP_BODY;
/**/
	}
}
