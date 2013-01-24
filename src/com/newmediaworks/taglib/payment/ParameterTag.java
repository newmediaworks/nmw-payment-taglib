/*
 * new-payment-taglib - Java taglib encapsulating the ao-credit-cards API.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013  New Media Works
 *     info@newmediaworks.com
 *     PO BOX 853
 *     Napa, CA 94559
 *
 * This file is part of new-payment-taglib.
 *
 * new-payment-taglib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * new-payment-taglib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with new-payment-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.payment;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Provides the connectorName to a <code>UseProcessorTag</code>.  Must be nested in a <code>UseProcessorTag</code>.
 *
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class ParameterTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    /** Attributes */
    private String name;

    public ParameterTag() {
        init();
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        BodyContent myBodyContent = getBodyContent();
        String value = myBodyContent==null ? "" : myBodyContent.getString().trim();
        UseProcessorTag useProcessorTag = (UseProcessorTag)findAncestorWithClass(this, UseProcessorTag.class);
        if(useProcessorTag==null) throw new JspException("parameter tag must be within useProcessor tag");
        useProcessorTag.addParameter(name, value);

        init();
        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    private void init() {
        name=null;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
