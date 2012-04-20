package com.cobaltolabs.intellij.stripes.references.contributors;

import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.filters.AndFilter;
import com.intellij.psi.filters.ClassFilter;
import com.intellij.psi.filters.ScopeFilter;
import com.intellij.psi.filters.TextFilter;
import com.intellij.psi.filters.position.NamespaceFilter;
import com.intellij.psi.filters.position.ParentElementFilter;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.util.XmlUtil;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.STRIPES_TLDS;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 20:06
 */

public final class ContributorUtil {
// ------------------------------ FIELDS ------------------------------

    public static final NamespaceFilter STRIPES_NAMESPACE_FILTER = new NamespaceFilter(STRIPES_TLDS);

// -------------------------- STATIC METHODS --------------------------

    public static void registerXmlAttributeReferenceProvider(PsiReferenceRegistrar registrar, PsiReferenceProvider provider, String attributeName, String... tagNames) {
        XmlUtil.registerXmlAttributeValueReferenceProvider(registrar, new String[]{attributeName},
                new ScopeFilter(new ParentElementFilter(new AndFilter(STRIPES_NAMESPACE_FILTER, new ClassFilter(XmlTag.class), new TextFilter(tagNames)), 2)), provider
        );
    }

// --------------------------- CONSTRUCTORS ---------------------------

    private ContributorUtil() {
    }
}
