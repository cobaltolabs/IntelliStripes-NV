package com.cobaltolabs.intellij.stripes.references.providers;

import com.cobaltolabs.intellij.stripes.references.JspTagAttrLayoutComponentReference;
import com.cobaltolabs.intellij.stripes.references.StripesReferenceUtil;
import com.cobaltolabs.intellij.stripes.references.filters.StripesTagFilter;
import com.cobaltolabs.intellij.stripes.util.StripesUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.jsp.JspFile;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.LAYOUT_RENDER_TAG;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.NAME_ATTR;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 22:39
 */
public class LayoutComponentReferenceProvider extends PsiReferenceProvider {
// ------------------------------ FIELDS ------------------------------

    private static PsiElementFilter LAYOUT_RENDER_FILTER = new StripesTagFilter(LAYOUT_RENDER_TAG);

// -------------------------- OTHER METHODS --------------------------

    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        final JspFile jspFile = getLayoutDefinitionJspFile((XmlTag) element.getParent().getParent());
        return jspFile == null
                ? PsiReference.EMPTY_ARRAY
                : new PsiReference[]{new JspTagAttrLayoutComponentReference((XmlAttributeValue) element, jspFile)};
    }

    /**
     * Get JspFile from given layout-render tag that includes layout-component tag passed as parameter
     *
     * @param xmlTag XmlTag
     * @return JspFile
     */
    private static JspFile getLayoutDefinitionJspFile(@NotNull XmlTag xmlTag) {
        if (!StripesUtil.isStripesPage(xmlTag.getContainingFile())) return null;

        XmlTag layoutRenderTag = StripesUtil.findParent(xmlTag, LAYOUT_RENDER_FILTER, StripesReferenceUtil.NAME_ATTR_FILTER);
        if (layoutRenderTag != null) {
            PsiReference[] refs = layoutRenderTag.getAttribute(NAME_ATTR).getValueElement().getReferences();
            for (PsiReference ref : refs) {
                PsiElement el = ref.resolve();
                if (StripesUtil.isStripesPage(el)) {
                    return (JspFile) el;
                }
            }
        }

        return null;
    }
}