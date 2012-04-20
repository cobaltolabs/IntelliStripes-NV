package com.cobaltolabs.intellij.stripes.references.filters;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.filters.ElementFilter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 8:56
 */
public class QualifiedNameElementFilter implements ElementFilter {
// ------------------------------ FIELDS ------------------------------

    private String qName;

// --------------------------- CONSTRUCTORS ---------------------------

    public QualifiedNameElementFilter(@NotNull String qName) {
        this.qName = qName;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ElementFilter ---------------------

    public boolean isAcceptable(Object element, PsiElement context) {
        return element instanceof PsiAnnotation
                && qName.equals(((PsiAnnotation) element).getQualifiedName());
    }

    public boolean isClassAcceptable(Class hintClass) {
        return PsiAnnotation.class.isAssignableFrom(hintClass);
    }
}
