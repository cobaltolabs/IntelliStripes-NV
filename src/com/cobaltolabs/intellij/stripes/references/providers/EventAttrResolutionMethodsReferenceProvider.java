package com.cobaltolabs.intellij.stripes.references.providers;

import com.cobaltolabs.intellij.stripes.references.JspTagAttrResolutionMethodsReference;
import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.cobaltolabs.intellij.stripes.util.StripesUtil;
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
 *         Time: 20:36
 */
public class EventAttrResolutionMethodsReferenceProvider extends PsiReferenceProvider {
// -------------------------- OTHER METHODS --------------------------

    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (PsiTreeUtil.getChildOfType(element, ELExpressionHolder.class) != null) {
            return PsiReference.EMPTY_ARRAY;
        }

        XmlTag tag = (XmlTag) element.getParent().getParent();
        final PsiClass actionBeanPsiClass = StripesUtil.findPsiClassByName(tag.getAttributeValue(StripesConstants.BEANCLASS_ATTR), element.getProject());
        return actionBeanPsiClass == null
                ? PsiReference.EMPTY_ARRAY
                : new PsiReference[]{new JspTagAttrResolutionMethodsReference((XmlAttributeValue) element, actionBeanPsiClass)};
    }
}