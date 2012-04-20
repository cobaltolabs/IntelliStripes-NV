package com.cobaltolabs.intellij.stripes.references.filters;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.filters.ElementFilter;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.SPRING_BEAN_ANNOTATION;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:40
 */
public class SpringBeanAnnotationFilter implements ElementFilter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ElementFilter ---------------------

    public boolean isAcceptable(final Object element, final PsiElement context) {
        if (element instanceof PsiNameValuePair) {
            PsiElement value = (PsiNameValuePair) element;
            //Ia the element An Annotation Parameter List
            if (value.getParent() instanceof PsiAnnotationParameterList) {
                PsiElement annotationParameterList = value.getParent();
                //get the Annotation Object
                PsiAnnotation annotation = (PsiAnnotation) annotationParameterList.getParent();
                try {
                    //Is @SpringBean Annotation??
                    return annotation.getQualifiedName().equals(SPRING_BEAN_ANNOTATION);
                } catch (NullPointerException e) {
                    //OOPS
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isClassAcceptable(final Class hintClass) {
        return true;
    }
}