package com.cobaltolabs.intellij.stripes.references.providers;

import com.cobaltolabs.intellij.stripes.references.FileBeanSetterReference;
import com.cobaltolabs.intellij.stripes.references.StripesReferenceUtil;
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

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.FORM_TAG;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:33
 */
public class FileBeanSetterMethodsReferenceProvider extends PsiReferenceProvider {
// -------------------------- OTHER METHODS --------------------------

    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if (PsiTreeUtil.getChildOfType(element, ELExpressionHolder.class) != null) {
            return PsiReference.EMPTY_ARRAY;
        }

        final PsiClass actionBeanPsiClass = StripesReferenceUtil.getBeanClassFromParentTag((XmlTag) element.getParent().getParent(), FORM_TAG);
        return actionBeanPsiClass == null
                ? PsiReference.EMPTY_ARRAY
                : new PsiReference[]{new FileBeanSetterReference((XmlAttributeValue) element, actionBeanPsiClass)};
    }
}