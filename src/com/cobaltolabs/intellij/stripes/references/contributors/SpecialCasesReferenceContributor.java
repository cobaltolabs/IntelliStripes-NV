package com.cobaltolabs.intellij.stripes.references.contributors;

import com.cobaltolabs.intellij.stripes.references.filters.SpringBeanAnnotationFilter;
import com.cobaltolabs.intellij.stripes.references.providers.FileBeanSetterMethodsReferenceProvider;
import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.intellij.psi.*;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.filters.position.ParentElementFilter;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.jsp.el.ELExpressionHolder;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.spring.references.SpringBeanNamesReferenceProvider;
import org.jetbrains.annotations.NotNull;

import static com.cobaltolabs.intellij.stripes.references.contributors.ContributorUtil.registerXmlAttributeReferenceProvider;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.*;
import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:29
 */
public class SpecialCasesReferenceContributor extends PsiReferenceContributor {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        for (String tag : ACTION_BEAN_TAGS) {
            //all stripes tags with beanclass parameter add Reference provider for implementations od Stripes ActionBean
            registerSubclass(tag, registrar);
        }

        registerXmlAttributeReferenceProvider(registrar, new FileBeanSetterMethodsReferenceProvider(), NAME_ATTR, FILE_TAG);

        //Spring's Beans
        registrar.registerReferenceProvider(psiElement(PsiLiteralExpression.class).and(new FilterPattern(new ParentElementFilter(new SpringBeanAnnotationFilter()))), new SpringBeanNamesReferenceProvider());


        JavaClassReferenceProvider provider = new JavaClassReferenceProvider();
        provider.setOption(JavaClassReferenceProvider.EXTEND_CLASS_NAMES, new String[]{Enum.class.getName()});
        provider.setOption(JavaClassReferenceProvider.ADVANCED_RESOLVE, Boolean.TRUE);
        provider.setOption(JavaClassReferenceProvider.RESOLVE_QUALIFIED_CLASS_NAME, Boolean.TRUE);
        registerXmlAttributeReferenceProvider(registrar, provider, StripesConstants.ENUM_ATTR, StripesConstants.OPTIONS_ENUMERATION_TAG);
    }

    private static void registerSubclass(String tagName, PsiReferenceRegistrar registry) {
        JavaClassReferenceProvider provider = new JavaClassReferenceProvider() {
            public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement) {
                if (PsiTreeUtil.getChildOfType(psiElement, ELExpressionHolder.class) != null) {
                    return PsiReference.EMPTY_ARRAY;
                }
                return super.getReferencesByElement(psiElement);
            }
        };

        provider.setOption(JavaClassReferenceProvider.EXTEND_CLASS_NAMES, new String[]{ACTION_BEAN});
        provider.setOption(JavaClassReferenceProvider.INSTANTIATABLE, true);
        registerXmlAttributeReferenceProvider(registry, provider, BEANCLASS_ATTR, tagName);
    }
}
