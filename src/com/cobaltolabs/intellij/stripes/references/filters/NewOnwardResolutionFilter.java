package com.cobaltolabs.intellij.stripes.references.filters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.filters.ElementFilter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:49
 */
public abstract class NewOnwardResolutionFilter implements ElementFilter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ElementFilter ---------------------

    public boolean isAcceptable(Object element, PsiElement psiElement) {
        if (element instanceof PsiExpressionList) {
            PsiExpressionList expressionList = (PsiExpressionList) element;
            if (expressionList.getExpressions().length == 2) {
                if (expressionList.getParent() instanceof PsiNewExpression) {
                    PsiNewExpression newExpression = (PsiNewExpression) expressionList.getParent();
                    return newExpression.getClassReference().getQualifiedName().equals(getResolutionClassName());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isClassAcceptable(Class aClass) {
        return true;
    }

// -------------------------- OTHER METHODS --------------------------

    protected abstract String getResolutionClassName();
}