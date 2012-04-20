package com.cobaltolabs.intellij.stripes.references.filters;

import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.filters.ElementFilter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 22:50
 */
public class OnwardResolutionConstructorFilter implements ElementFilter {
// ------------------------------ FIELDS ------------------------------

    private int count;

// --------------------------- CONSTRUCTORS ---------------------------

    public OnwardResolutionConstructorFilter(int count) {
        this.count = count;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ElementFilter ---------------------

    public boolean isAcceptable(Object element, PsiElement context) {
        if (!(((PsiElement) element).getParent() instanceof PsiExpressionList)) return false;

        PsiElement constructor = ((PsiElement) element).getParent().getParent();
        if (!(constructor instanceof PsiNewExpression)) return false;

        String qName = ((PsiNewExpression) constructor).getClassReference().getQualifiedName();
        return (StripesConstants.FORWARD_RESOLUTION.equals(qName)
                || StripesConstants.REDIRECT_RESOLUTION.equals(qName))
                && ((PsiExpressionList) ((PsiElement) element).getParent()).getExpressions().length == count;
    }

    public boolean isClassAcceptable(Class hintClass) {
        return true;
    }
}