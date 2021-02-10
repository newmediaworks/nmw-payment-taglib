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
package com.newmediaworks.taglib.payment;

import com.aoindustries.creditcards.TransactionResult;
import com.aoindustries.encoding.MediaType;
import com.aoindustries.encoding.taglib.EncodingNullTag;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Gets the reason for a processing error.
 *
 * @see  Functions#getErrorReason()
 * @see  TransactionResult#getProviderErrorCode()
 * @see  TransactionResult#getProviderErrorMessage()
 * @see  TransactionResult#getErrorCode()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class GetErrorReasonTag extends EncodingNullTag {

/* SimpleTag only: */
	public static final String TAG_NAME = "<payment:getErrorReason>";
/**/

	@Override
	public MediaType getOutputType() {
		return MediaType.TEXT;
	}

/* BodyTag only:
	private static final long serialVersionUID = 1L;
/**/

	@Override
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
/* BodyTag only:
	protected int doStartTag(Writer out) throws JspException, IOException {
/**/
/* SimpleTag only: */
	protected void doTag(Writer out) throws JspException, IOException {
		PageContext pageContext = (PageContext)getJspContext();
/**/
		String errorReason = Functions.getErrorReason(TAG_NAME, pageContext.getRequest());
		if(errorReason != null) out.write(errorReason);
/* BodyTag only:
		return SKIP_BODY;
/**/
	}
}
