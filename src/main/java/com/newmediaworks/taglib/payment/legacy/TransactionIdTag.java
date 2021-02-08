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
import com.aoindustries.encoding.taglib.legacy.EncodingBufferedBodyTag;
import com.aoindustries.io.buffer.BufferResult;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import com.newmediaworks.taglib.payment.CaptureTag;
import static com.newmediaworks.taglib.payment.TransactionIdTag.TAG_NAME;
import com.newmediaworks.taglib.payment.VoidTag;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;
import javax.servlet.jsp.JspException;

/**
 * Provides the transaction ID to a {@link CaptureTag}
 * or {@link VoidTag}.
 *
 * @see  AuthorizationResult#getProviderUniqueId()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class TransactionIdTag extends EncodingBufferedBodyTag {

/* SimpleTag only:
	public static final String TAG_NAME = "<payment:transactionId>";
/**/

	@Override
	public MediaType getContentType() {
		return MediaType.TEXT;
	}

	@Override
	public MediaType getOutputType() {
		return null;
	}

/* BodyTag only: */
	private static final long serialVersionUID = 1L;
/**/

	@Override
/* BodyTag only: */
	protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
/* SimpleTag only:
	protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
		String transactionId = capturedBody.trim().toString();
		// Java 9: Optional.ifPresentOrElse
		Optional<CaptureTag> captureTag = JspTagUtils.findAncestor(this, CaptureTag.class);
		if(captureTag.isPresent()) {
			captureTag.get().setTransactionId(transactionId);
		} else {
			JspTagUtils.requireAncestor(TAG_NAME, this, CaptureTag.TAG_NAME + " or " + VoidTag.TAG_NAME, VoidTag.class)
				.setTransactionId(transactionId);
		}
/* BodyTag only: */
		return EVAL_PAGE;
/**/
	}
}
