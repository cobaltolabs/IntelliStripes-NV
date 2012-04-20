package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.search.searches.AnnotatedMembersSearch;
import com.intellij.util.Processor;
import com.intellij.util.containers.HashMap;

import java.util.Map;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.URL_BINDING_ANNOTATION;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.VALUE_ATTR;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 23:57
 */
public class UrlBindingSearcher {
// ------------------------------ FIELDS ------------------------------

    private PsiClass urlBindingCls;

// --------------------------- CONSTRUCTORS ---------------------------

    public UrlBindingSearcher(PsiClass urlBindingCls) {
        this.urlBindingCls = urlBindingCls;
    }

// -------------------------- OTHER METHODS --------------------------

    public Map<String, PsiClass> execute() {
        UrlBindingProcessor proc = new UrlBindingProcessor();
        try {
            AnnotatedMembersSearch.search(urlBindingCls, urlBindingCls.getUseScope()).forEach(proc);
        } catch (ProcessCanceledException e) {
            //Do nothig, this exception is very common and can be throw for intellij
            //Logger don't be reported or just raise an ugly error
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proc.getBindings();
    }

// -------------------------- INNER CLASSES --------------------------

    public static class UrlBindingProcessor implements Processor<PsiMember> {
        private Map<String, PsiClass> bindings = new HashMap<String, PsiClass>(8);

        public boolean process(PsiMember member) {
            if (member instanceof PsiClass) {
                PsiAnnotation ann = member.getModifierList().findAnnotation(URL_BINDING_ANNOTATION);
                if (null != ann) {
                    PsiAnnotationMemberValue value = ann.findAttributeValue(VALUE_ATTR);
                    if (value != null)
                        bindings.put(StringUtil.stripQuotesAroundValue(value.getText()), (PsiClass) member);
                }
            }
            return true;
        }

        public Map<String, PsiClass> getBindings() {
            return bindings;
        }
    }
}