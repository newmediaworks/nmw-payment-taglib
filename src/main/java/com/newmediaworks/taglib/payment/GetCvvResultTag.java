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

import com.aoapps.encoding.MediaType;
import com.aoapps.encoding.taglib.EncodingNullTag;
import com.aoapps.payments.AuthorizationResult;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Gets the CVV2 (card security code) verification result.
 *
 * @see  Functions#getCvvResult()
 * @see  AuthorizationResult#getCvvResult()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class GetCvvResultTag extends EncodingNullTag {

/* SimpleTag only: */
	public static final String TAG_NAME = "<payment:getCvvResult>";
/**/

	@Override
	public MediaType getOutputType() {
		return MediaType.TEXT;
	}

/* BodyTag only:
	private static final long serialVersionUID = 1L;
/**/

	@Override
/* BodyTag only:
	protected int doStartTag(Writer out) throws JspException, IOException {
/**/
/* SimpleTag only: */
	protected void doTag(Writer out) throws JspException, IOException {
		PageContext pageContext = (PageContext)getJspContext();
/**/
		String cvvResult = Functions.getCvvResult(TAG_NAME, pageContext.getRequest());
		if(cvvResult != null) out.write(cvvResult);
/* BodyTag only:
		return SKIP_BODY;
/**/
	}
}
