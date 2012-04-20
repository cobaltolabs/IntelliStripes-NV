package com.cobaltolabs.intellij.stripes.references;

import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PropertyUtil;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:24
 */
public class GetterReference extends PsiReferenceBase<PsiElement> {
// ------------------------------ FIELDS ------------------------------

    protected PsiClass actionBeanPsiClass;

    private String[] variants;

// --------------------------- CONSTRUCTORS ---------------------------

    public GetterReference(PsiElement psiElement, PsiClass actionBeanPsiClass) {
        super(psiElement);
        this.actionBeanPsiClass = actionBeanPsiClass;
    }

    public GetterReference(PsiElement psiElement, TextRange range, PsiClass actionBeanPsiClass, String... variants) {
        super(psiElement, range);
        this.actionBeanPsiClass = actionBeanPsiClass;
        this.variants = variants;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    @Nullable
    public PsiElement resolve() {
        String value = getValue();
        if (!ArrayUtils.contains(variants, value)) {
            return PropertyUtil.findPropertyGetter(actionBeanPsiClass, value, false, true);
        }
        return myElement;
    }

    public Object[] getVariants() {
        String[] retval = PropertyUtil.getReadableProperties(actionBeanPsiClass, true);
        if (variants != null) retval = (String[]) ArrayUtils.addAll(retval, variants);

        return StripesReferenceUtil.getVariants(Arrays.asList(retval), StripesConstants.FIELD_ICON);
    }
}