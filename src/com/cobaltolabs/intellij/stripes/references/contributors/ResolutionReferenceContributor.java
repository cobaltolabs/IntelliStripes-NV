package com.cobaltolabs.intellij.stripes.references.contributors;

import com.cobaltolabs.intellij.stripes.references.JavaStringResolutionMethodsReference;
import com.cobaltolabs.intellij.stripes.references.StripesReferenceUtil;
import com.cobaltolabs.intellij.stripes.references.filters.NewForwardResolutionFilter;
import com.cobaltolabs.intellij.stripes.references.filters.NewRedirectResolutionFilter;
import com.cobaltolabs.intellij.stripes.references.filters.StringArrayAnnotationParameterFilter;
import com.cobaltolabs.intellij.stripes.references.providers.EventAttrResolutionMethodsReferenceProvider;
import com.cobaltolabs.intellij.stripes.references.providers.TagResolutionMethodsReferenceProvider;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.patterns.StandardPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.filters.OrFilter;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.filters.position.ParentElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.cobaltolabs.intellij.stripes.references.contributors.ContributorUtil.registerXmlAttributeReferenceProvider;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:42
 */
public class ResolutionReferenceContributor extends PsiReferenceContributor {
// ------------------------------ FIELDS ------------------------------

    private static final PsiReferenceProvider ONWARD_RESOLUTION_REFERENCE_PROVIDER = new PsiReferenceProvider() {
        @NotNull
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            final PsiClass psiClass = StripesReferenceUtil.getPsiClassFromExpressionList((PsiExpressionList) element.getParent());
            return psiClass == null
                    ? PsiReference.EMPTY_ARRAY
                    : new PsiReference[]{new JavaStringResolutionMethodsReference((PsiLiteralExpression) element, psiClass)};
        }
    };

// -------------------------- OTHER METHODS --------------------------

    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        //Resolutions on Annotations
        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new FilterPattern(
                new OrFilter(
                        new StringArrayAnnotationParameterFilter(VALIDATION_METHOD_ANNOTATION, ON_ATTR),
                        new StringArrayAnnotationParameterFilter(VALIDATE_ANNOTATION, ON_ATTR),
                        new StringArrayAnnotationParameterFilter(AFTER_ANNOTATION, ON_ATTR),
                        new StringArrayAnnotationParameterFilter(BEFORE_ANNOTATION, ON_ATTR),
                        new StringArrayAnnotationParameterFilter(WIZARD_ANNOTATION, START_EVENTS_ATTR)
                ))), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiClass cls = PsiTreeUtil.getParentOfType(element, PsiClass.class);
                return null == cls
                        ? PsiReference.EMPTY_ARRAY
                        : new PsiReference[]{new JavaStringResolutionMethodsReference((PsiLiteralExpression) element, cls)};
            }
        });

        //on new ForwardResolution
        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new FilterPattern(
                new ParentElementFilter(new NewForwardResolutionFilter())
        )), ONWARD_RESOLUTION_REFERENCE_PROVIDER);

        //on new RedirectResolution
        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new FilterPattern(
                new ParentElementFilter(new NewRedirectResolutionFilter())
        )), ONWARD_RESOLUTION_REFERENCE_PROVIDER);

        //on input tags with name _eventName
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute(VALUE_ATTR).withParent(
                XmlPatterns.xmlTag()
                        .withNamespace(STRIPES_TLDS)
                        .withLocalName(StandardPatterns.string().oneOf(EVENT_NAME_TAGS))
                        .withChild(XmlPatterns.xmlAttribute(NAME_ATTR).withText(StandardPatterns.string().contains(EVENT_NAME)))
        )), new TagResolutionMethodsReferenceProvider());

        //all stripes tags for submit form add Reference Provider for Event(Resolution Method)
        registerXmlAttributeReferenceProvider(registrar, new TagResolutionMethodsReferenceProvider(), NAME_ATTR, RESOLUTION_TAGS);
        //all stripes special tags with event parameter add Reference Provider for Event(Resolution Method)
        registerXmlAttributeReferenceProvider(registrar, new EventAttrResolutionMethodsReferenceProvider(), EVENT_ATTR, ACTION_BEAN_TAGS_WITH_EVENT);
    }
}