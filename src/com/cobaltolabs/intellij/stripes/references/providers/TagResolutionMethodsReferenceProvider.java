package com.cobaltolabs.intellij.stripes.references.providers;

import com.cobaltolabs.intellij.stripes.references.JspTagAttrResolutionMethodsReference;
import com.cobaltolabs.intellij.stripes.references.StripesReferenceUtil;
import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.jsp.el.ELExpressionHolder;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:53
 */
public class TagResolutionMethodsReferenceProvider extends PsiReferenceProvider {
// -------------------------- OTHER METHODS --------------------------

    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (PsiTreeUtil.getChildOfType(element, ELExpressionHolder.class) != null) {
            return PsiReference.EMPTY_ARRAY;
        }

        final PsiClass actionBeanPsiClass = StripesReferenceUtil.getBeanClassFromParentTag((XmlTag) element.getParent().getParent(), StripesConstants.FORM_TAG);
        return actionBeanPsiClass == null
                ? PsiReference.EMPTY_ARRAY
                : new PsiReference[]{new JspTagAttrResolutionMethodsReference((XmlAttributeValue) element, actionBeanPsiClass)};
    }
}