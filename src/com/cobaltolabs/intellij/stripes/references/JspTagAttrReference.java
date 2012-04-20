package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:54
 */
public abstract class JspTagAttrReference extends StripesReference {
// ------------------------------ FIELDS ------------------------------

    protected XmlAttributeValue xmlAttributeValue;

// --------------------------- CONSTRUCTORS ---------------------------

    public JspTagAttrReference(XmlAttributeValue xmlAttributeValue) {
        this.xmlAttributeValue = xmlAttributeValue;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    public PsiElement getElement() {
        return xmlAttributeValue;
    }

    public TextRange getRangeInElement() {
        return new TextRange(1, xmlAttributeValue.getValue().length() + 1);
    }

    @Override
    public String getCanonicalText() {
        return xmlAttributeValue.getValue();
    }
}