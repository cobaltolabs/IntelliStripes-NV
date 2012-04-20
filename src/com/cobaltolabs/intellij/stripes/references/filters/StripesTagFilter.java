package com.cobaltolabs.intellij.stripes.references.filters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.xml.XmlTag;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.TAGLIB_PREFIX;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 9/12/11
 *         Time: 12:49
 */
public class StripesTagFilter implements PsiElementFilter {
// ------------------------------ FIELDS ------------------------------

    final private String tag;

// --------------------------- CONSTRUCTORS ---------------------------

    public StripesTagFilter(String tag) {
        this.tag = tag;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiElementFilter ---------------------

    public boolean isAccepted(PsiElement element) {
        return (element instanceof XmlTag)
                && ((XmlTag) element).getNamespace().startsWith(TAGLIB_PREFIX)
                && tag.equals(((XmlTag) element).getLocalName());
    }
}
