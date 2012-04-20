package com.cobaltolabs.intellij.stripes.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:44
 */
public abstract class StripesReference implements PsiReference {
// ------------------------------ FIELDS ------------------------------

    private static final Object[] EMPTY_OBJECT = new Object[0];

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    @Nullable
    public PsiElement resolve() {
        return null;
    }

    public String getCanonicalText() {
        return null;
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return null;
    }

    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    public boolean isReferenceTo(PsiElement element) {
        return element == resolve();
    }

    public Object[] getVariants() {
        return EMPTY_OBJECT;
    }

    public boolean isSoft() {
        return false;
    }
}
