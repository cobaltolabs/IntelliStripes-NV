package com.cobaltolabs.intellij.stripes.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.xml.XmlTag;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 23:28
 */
public abstract class StripesTagFilter implements PsiElementFilter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiElementFilter ---------------------

    public boolean isAccepted(PsiElement element) {
        return element instanceof XmlTag
                && ((XmlTag) element).getNamespace().startsWith(StripesConstants.TAGLIB_PREFIX)
                && isDetailsAccepted((XmlTag) element);
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Implement this method to extend filter functionality.
     *
     * @param tag tag to be checked
     * @return true if tag match conditions, false otherwise.
     */
    protected abstract boolean isDetailsAccepted(XmlTag tag);
}
