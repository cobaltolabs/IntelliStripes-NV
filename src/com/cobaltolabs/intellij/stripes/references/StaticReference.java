package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:20
 */
public class StaticReference extends PsiReferenceBase<PsiElement> {
// ------------------------------ FIELDS ------------------------------

    private String[] array;

// --------------------------- CONSTRUCTORS ---------------------------

    public StaticReference(PsiElement element, String... array) {
        super(element);
        this.array = array;
    }

    public StaticReference(PsiElement element, TextRange range, String... array) {
        super(element, range);
        this.array = array;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    @Nullable
    public PsiElement resolve() {
        return getElement();
    }

    public Object[] getVariants() {
        return array;
    }
}