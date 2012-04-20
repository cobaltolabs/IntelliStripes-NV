package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 23:21
 */
public class SetterReferenceEx<T extends PsiElement> extends SetterReference<T> {
// ------------------------------ FIELDS ------------------------------

    private final StripesReferenceSetBase referenceSet;
    private final int index;

// --------------------------- CONSTRUCTORS ---------------------------

    public SetterReferenceEx(TextRange range, Boolean supportBraces, StripesReferenceSetBase referenceSet, int index) {
        super((T) referenceSet.getElement(), range, supportBraces);
        this.referenceSet = referenceSet;
        this.index = index;
    }

    public SetterReferenceEx(TextRange range, Boolean supportBraces, Boolean hasBraces, StripesReferenceSetBase referenceSet, int index) {
        super((T) referenceSet.getElement(), range, supportBraces);
        this.hasBraces = hasBraces;
        this.referenceSet = referenceSet;
        this.index = index;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public int getIndex() {
        return index;
    }

    public StripesReferenceSetBase getReferenceSet() {
        return referenceSet;
    }

// -------------------------- OTHER METHODS --------------------------

    public PsiClass getActionBeanPsiClass() {
        if (index == 0) {
            return referenceSet.getActionBeanPsiClass();
        } else {
            final PsiElement method = referenceSet.getReference(this.index - 1).resolve();
            if (method instanceof PsiMethod) {
                return StripesReferenceUtil
                        .resolveClassInType(((PsiMethod) method).getParameterList().getParameters()[0].getType(), getElement().getProject());
            }
        }
        return null;
    }
}