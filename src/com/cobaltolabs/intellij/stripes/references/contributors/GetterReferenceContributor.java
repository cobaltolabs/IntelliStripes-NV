package com.cobaltolabs.intellij.stripes.references.contributors;

import com.cobaltolabs.intellij.stripes.references.GetterReference;
import com.cobaltolabs.intellij.stripes.util.StripesUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.jsp.el.ELExpressionHolder;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.cobaltolabs.intellij.stripes.references.contributors.ContributorUtil.registerXmlAttributeReferenceProvider;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:22
 */
public class GetterReferenceContributor extends PsiReferenceContributor {
// ------------------------------ FIELDS ------------------------------

    private PsiReferenceProvider REFERENCE_PROVIDER = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            if (PsiTreeUtil.getChildOfType(element, ELExpressionHolder.class) != null) {
                return PsiReference.EMPTY_ARRAY;
            }
            XmlTag tag = (XmlTag) element.getParent().getParent();
            final PsiClass actionBeanPsiClass = StripesUtil.findPsiClassByName(tag.getAttributeValue(ENUM_ATTR), element.getProject());
            return actionBeanPsiClass == null
                    ? PsiReference.EMPTY_ARRAY
                    : new PsiReference[]{new GetterReference(element, actionBeanPsiClass)};
        }
    };

// -------------------------- OTHER METHODS --------------------------

    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registerXmlAttributeReferenceProvider(registrar, REFERENCE_PROVIDER,
                LABEL_ATTR, OPTIONS_ENUMERATION_TAG);

        registerXmlAttributeReferenceProvider(registrar, new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull final PsiElement element, @NotNull ProcessingContext context) {
                if (PsiTreeUtil.getChildOfType(element, ELExpressionHolder.class) != null) {
                    return PsiReference.EMPTY_ARRAY;
                }

                XmlTag tag = (XmlTag) element.getParent().getParent();
                final PsiClass actionBeanPsiClass = StripesUtil.findPsiClassByName(tag.getAttributeValue(ENUM_ATTR), element.getProject());

                if (null != actionBeanPsiClass) {
                    return new ReferenceSetBase<PsiReference>(ElementManipulators.getValueText(element), element, 1, ',') {
                        @NotNull
                        protected PsiReference createReference(TextRange range, int index) {
//							getReferences().add(new StaticReference(element, range, "sort", "label"));
                            return new GetterReference(element, range, actionBeanPsiClass, "sort", "label");
                        }
                    }.getPsiReferences();
                }
                return PsiReference.EMPTY_ARRAY;
            }
        }, SORT_ATTR, OPTIONS_ENUMERATION_TAG);
    }
}