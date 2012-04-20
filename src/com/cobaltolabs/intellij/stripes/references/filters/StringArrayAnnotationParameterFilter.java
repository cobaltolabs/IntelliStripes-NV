package com.cobaltolabs.intellij.stripes.references.filters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.filters.AnnotationParameterFilter;
import org.jetbrains.annotations.NonNls;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 8:57
 */
public class StringArrayAnnotationParameterFilter extends AnnotationParameterFilter {
// --------------------------- CONSTRUCTORS ---------------------------

    public StringArrayAnnotationParameterFilter(String annotationName, @NonNls String annotationAttributeName) {
        super(PsiLiteralExpression.class, annotationName, annotationAttributeName);
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ElementFilter ---------------------

    public boolean isAcceptable(Object o, PsiElement psiElement) {
        return super.isAcceptable(o, psiElement) || super.isAcceptable(((PsiElement) o).getParent(), psiElement);
    }
}
