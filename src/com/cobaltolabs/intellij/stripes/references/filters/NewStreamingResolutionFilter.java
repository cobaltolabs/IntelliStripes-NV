package com.cobaltolabs.intellij.stripes.references.filters;

import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:21
 */
public class NewStreamingResolutionFilter implements ElementFilter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ElementFilter ---------------------

    public boolean isAcceptable(Object element, PsiElement psiElement) {
        PsiExpressionList expressionList = (PsiExpressionList) element;
        if (expressionList.getExpressions()[0].equals(psiElement)) {
            PsiNewExpression p = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
            if (p != null) {
                return StripesConstants.STREAMING_RESOLUTION.equals(p.getClassOrAnonymousClassReference().getQualifiedName());
            }
        }
        return false;
    }

    public boolean isClassAcceptable(Class aClass) {
        return PsiExpressionList.class.isAssignableFrom(aClass);
    }
}
