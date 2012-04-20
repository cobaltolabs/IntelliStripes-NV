package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.ReferenceSetBase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 15:37
 */
public abstract class StripesReferenceSetBase<T extends PsiReference> {
// ------------------------------ FIELDS ------------------------------

    private final String str;
    private final PsiElement element;
    private final int offset;
    private final char separator;
    private final PsiClass actionBeanPsiClass;
    private final Boolean supportBraces;

    private List<T> references;

// --------------------------- CONSTRUCTORS ---------------------------

    public StripesReferenceSetBase(@NotNull final PsiElement element, final int offset, final char separator,
                                   final PsiClass actionBeanPsiClass, final Boolean supportBraces) {
        this.str = ElementManipulators.getValueText(element);
        this.element = element;
        this.offset = offset;
        this.separator = separator;
        this.actionBeanPsiClass = actionBeanPsiClass;
        this.supportBraces = supportBraces;

        this.references = initRefs();
    }

    protected List<T> initRefs() {
        ReferenceSetBase<T> referenceSet = new ReferenceSetBase<T>(this.str, element, offset, separator) {
            @NotNull
            @Override
            protected T createReference(TextRange range, int index) {
                return StripesReferenceSetBase.this.createReference(range, index);
            }
        };

        return referenceSet.getReferences();
    }

    protected abstract T createReference(TextRange range, int index);

    public StripesReferenceSetBase(final String str, @NotNull final PsiElement element, final int offset, final char separator,
                                   final PsiClass actionBeanPsiClass, final Boolean supportBraces) {
        this.str = str;
        this.element = element;
        this.offset = offset;
        this.separator = separator;
        this.actionBeanPsiClass = actionBeanPsiClass;
        this.supportBraces = supportBraces;

        this.references = initRefs();
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public PsiClass getActionBeanPsiClass() {
        return actionBeanPsiClass;
    }

    public PsiElement getElement() {
        return element;
    }

    protected int getOffset() {
        return offset;
    }

    public String getStr() {
        return str;
    }

    public Boolean isSupportBraces() {
        return supportBraces;
    }

// -------------------------- OTHER METHODS --------------------------

    public PsiReference[] getPsiReferences() {
        return references.toArray(new PsiReference[references.size()]);
    }

    public T getReference(int index) {
        return references.get(index);
    }
}
