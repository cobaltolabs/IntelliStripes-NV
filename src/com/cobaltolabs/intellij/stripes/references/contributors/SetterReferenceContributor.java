package com.cobaltolabs.intellij.stripes.references.contributors;

import com.cobaltolabs.intellij.stripes.references.*;
import com.cobaltolabs.intellij.stripes.references.filters.QualifiedNameElementFilter;
import com.cobaltolabs.intellij.stripes.references.filters.StringArrayAnnotationParameterFilter;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.*;
import com.intellij.psi.filters.AndFilter;
import com.intellij.psi.filters.AnnotationParameterFilter;
import com.intellij.psi.filters.OrFilter;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.filters.position.SuperParentFilter;
import com.intellij.psi.jsp.el.ELExpressionHolder;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.cobaltolabs.intellij.stripes.references.contributors.ContributorUtil.registerXmlAttributeReferenceProvider;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 13:26
 */
public class SetterReferenceContributor extends PsiReferenceContributor {
// ------------------------------ FIELDS ------------------------------

    private static final Logger LOGGER = Logger.getInstance(SetterReferenceContributor.class);

// -------------------------- OTHER METHODS --------------------------

    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
//errors tag add Reference Provider for Setters Method on parameter field
        registerXmlAttributeReferenceProvider(registrar, new SetterMethodsReferenceProvider(FORM_TAG),
                FIELD_ATTR, ERRORS_TAG);

//all stripes tags for input form add Reference Provider for Setters Method
        registerXmlAttributeReferenceProvider(registrar, new SetterMethodsReferenceProvider(FORM_TAG),
                NAME_ATTR, INPUT_TAGS);

//param tag add Reference Provider for Setter Methods
        registerXmlAttributeReferenceProvider(registrar, new SetterMethodsReferenceProvider(LINK_TAG),
                NAME_ATTR, PARAM_TAG);
        registerXmlAttributeReferenceProvider(registrar, new SetterMethodsReferenceProvider(URL_TAG),
                NAME_ATTR, PARAM_TAG);

        registerXmlAttributeReferenceProvider(
                registrar, new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                if (PsiTreeUtil.getChildOfType(element, ELExpressionHolder.class) != null) {
                    return PsiReference.EMPTY_ARRAY;
                }

                final PsiClass actionBeanPsiClass = StripesReferenceUtil.getBeanClassFromParentTag(
                        (XmlTag) element.getParent().getParent(), FORM_TAG
                );

                if (null != actionBeanPsiClass) {
                    List<String> arr = StringUtil.split(ElementManipulators.getValueText(element), ",");

                    List<PsiReference> retval = new LinkedList<PsiReference>();
                    int offset = 1;
                    for (String anArr : arr) {
                        Collections.addAll(retval, new SetterReferenceExSet(anArr, element, offset, '.', actionBeanPsiClass, false).getPsiReferences());
                        offset += (anArr.length() + 1);
                    }

                    return retval.toArray(new PsiReference[retval.size()]);
                }

                return PsiReference.EMPTY_ARRAY;
            }
        }, FIELDS_ATTR, FIELD_METADATA_TAG
        );

        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new FilterPattern(
                new AndFilter(
                        new SuperParentFilter(new QualifiedNameElementFilter(VALIDATE_NESTED_PROPERTIES_ANNOTATION)),
                        new AnnotationParameterFilter(PsiLiteralExpression.class, VALIDATE_ANNOTATION, FIELD_ATTR)
                )
        )), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiMember parent = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
                if (parent == null) parent = PsiTreeUtil.getParentOfType(element, PsiField.class);

                PsiClass cls = StripesReferenceUtil.resolveClassInType(PropertyUtil.getPropertyType(parent), element.getProject());
                return null == cls
                        ? PsiReference.EMPTY_ARRAY
                        : new SetterReferenceExSet(element, 1, '.', cls, false).getPsiReferences();
            }
        });

        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new FilterPattern(
                new OrFilter(
                        new StringArrayAnnotationParameterFilter(STRICT_BINDING_ANNOTATION, ALLOW_ATTR),
                        new StringArrayAnnotationParameterFilter(STRICT_BINDING_ANNOTATION, DENY_ATTR)
                )
        )), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiClass cls = PsiTreeUtil.getParentOfType(element, PsiClass.class);
                return null == cls ? PsiReference.EMPTY_ARRAY : new SetterReferenceExSet(element, 1, '.', cls, false) {
                    @NotNull
                    @Override
                    protected SetterReferenceEx<PsiElement> createReferenceWithBraces(TextRange range, int index, boolean hasBraces) {
                        return new StrictBindingReference(range, this.isSupportBraces(), this, index);
                    }
                }.getPsiReferences();
            }
        });

        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new FilterPattern(
                new AnnotationParameterFilter(PsiLiteralExpression.class, URL_BINDING_ANNOTATION, VALUE_ATTR)
        )), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiClass actionBeanPsiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
                String str = ElementManipulators.getValueText(element);

                if (null != actionBeanPsiClass && str.startsWith("/")) {
                    final List<PsiReference> retval = new LinkedList<PsiReference>();
                    for (int i = 0, eqInd = -1, lBraceInd = -1, braceStack = 0; i < str.length(); i++) {
                        if (str.charAt(i) == '{') {
                            braceStack++;
                            lBraceInd = i;
                            eqInd = -1;
                        } else if (str.charAt(i) == '}') {// we found closing brace and need to retrieve references if possible
                            braceStack--;
                            if (braceStack != 0) continue;// braces are unbalanced - we should not try to parse

                            int endInd = eqInd != -1
                                    ? eqInd // there's '=' sign within curly braces bounded part of string. processign only part of text located within curl braces
                                    : i; // no '=' sign found. process whole text from curly braces;

                            String txt = str.substring(1 + lBraceInd, endInd);
                            if ($EVENT.equals(txt) && eqInd == -1) {
                                retval.add(new UrlBindingReference(element, new TextRange(1 + lBraceInd + 1, 1 + endInd), actionBeanPsiClass));
                            } else {
                                //                                final PsiReference[] psiReferences = new SetterReferenceExSet(str, element, 1 + lBraceInd + 1, '.', actionBeanPsiClass, true).getPsiReferences();
                                Collections.addAll(retval,
                                        new UrlSetterReferenceExSet(txt, element, 1 + lBraceInd + 1, '.', actionBeanPsiClass, true).getPsiReferences()
//                                        new UrlSetterReferenceExSet(txt, element, 1, '.', actionBeanPsiClass, true).getPsiReferences()
                                );
                            }

                            //retval.add(new UrlBindingReference(element, new TextRange(1 + lBraceInd + 1, 1 + lBraceInd + 1), actionBeanPsiClass));
                        } else if (str.charAt(i) == '=') {
                            eqInd = i;
                        }
                    }


                    return retval.toArray(new PsiReference[retval.size()]);
                }


                return PsiReference.EMPTY_ARRAY;
            }
        });

        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().methodCallParameter(0,
                PsiJavaPatterns
                        .psiMethod()
                        .definedInClass(VALIDATION_ERRORS)
                        .withName(StandardPatterns.string().oneOf(
                                ADD_METHOD, ADD_ALL_METHOD, PUT_METHOD, PUT_ALL_METHOD))
        ), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                PsiClass cls = PsiTreeUtil.getParentOfType(element, PsiClass.class);
                return null == cls ? PsiReference.EMPTY_ARRAY : new SetterReferenceExSet(element, 1, '.', cls, true).getPsiReferences();
            }
        });
    }

// -------------------------- INNER CLASSES --------------------------

    private static class StrictBindingReference extends SetterReferenceEx<PsiElement> {
        private static List<String> EXTRA_VALUES = Arrays.asList("*", "**");

        public StrictBindingReference(TextRange range, Boolean supportBraces, StripesReferenceSetBase referenceSet, int index) {
            super(range, supportBraces, referenceSet, index);
        }

        @Override
        protected List<String> getVariantsEx() {
            return EXTRA_VALUES;
        }

        @Override
        protected PsiElement resolveEx() {
            if (getIndex() == 0) return getReferenceSet().getActionBeanPsiClass();
            PsiMethod method = (PsiMethod) getReferenceSet().getReference(getIndex() - 1).resolve();
            return null == method ? null : method.getContainingClass();
        }
    }

    private static class UrlBindingReference extends PsiPolyVariantReferenceBase<PsiElement> {
        private static String[] VARIANTS = {$EVENT};

        private PsiClass actionBeanClass;

        public UrlBindingReference(PsiElement psiElement, TextRange range, PsiClass actionBeanClass) {
            super(psiElement, range, false);
            this.actionBeanClass = actionBeanClass;
        }

        public Object[] getVariants() {
            return VARIANTS;
        }

        @NotNull
        public ResolveResult[] multiResolve(boolean incompleteCode) {
            Collection<PsiMethod> resMethods = StripesReferenceUtil.getResolutionMethodsAsList(actionBeanClass);
            ResolveResult[] retval = new ResolveResult[resMethods.size()];
            int i = 0;
            for (PsiMethod method : resMethods) {
                retval[i++] = new PsiElementResolveResult(method);
            }

            return retval;
        }
    }

    /**
     * This class provide References to ActionBean setter methods in stripes tags.
     */
    private static class SetterMethodsReferenceProvider extends PsiReferenceProvider {
        private String parentTagName;

        public SetterMethodsReferenceProvider(String parentTagName) {
            this.parentTagName = parentTagName;
        }

        @NotNull
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            if (EVENT_NAME.equals(ElementManipulators.getValueText(element))) {
                return PsiReference.EMPTY_ARRAY;
            }

            XmlTag tag = (XmlTag) element.getParent().getParent();
            final PsiClass actionBeanPsiClass = StripesReferenceUtil.getBeanClassFromParentTag(tag, parentTagName);

            return actionBeanPsiClass == null
                    ? PsiReference.EMPTY_ARRAY
                    : new SetterReferenceExSet(element, 1, '.', actionBeanPsiClass, true).getPsiReferences();
        }
    }
}