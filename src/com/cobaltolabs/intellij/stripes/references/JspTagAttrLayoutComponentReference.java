package com.cobaltolabs.intellij.stripes.references;

import com.cobaltolabs.intellij.stripes.references.filters.StripesTagFilter;
import com.cobaltolabs.intellij.stripes.util.StripesUtil;
import com.cobaltolabs.intellij.stripes.util.XmlTagContainer;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.jsp.JspFile;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 22:39
 */
public class JspTagAttrLayoutComponentReference extends JspTagAttrReference {
// ------------------------------ FIELDS ------------------------------

    private static PsiElementFilter LAYOUT_COMPONENT_FILTER = new StripesTagFilter(LAYOUT_COMPONENT_TAG);

    private static PsiElementFilter LAYOUT_DEFINITION_FILTER = new StripesTagFilter(LAYOUT_DEFINITION_TAG);

    private JspFile layoutDefJspFile;

// --------------------------- CONSTRUCTORS ---------------------------

    public JspTagAttrLayoutComponentReference(XmlAttributeValue xmlAttributeValue, JspFile layoutDefJsp) {
        super(xmlAttributeValue);
        this.layoutDefJspFile = layoutDefJsp;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public Object[] getVariants() {
        List<XmlTag> tags = getLayoutComponents(layoutDefJspFile);
        Object[] variants = new Object[tags.size()];
        for (int i = 0; i < tags.size(); i++) {
            variants[i] = LookupElementBuilder.create(tags.get(i).getAttributeValue(NAME_ATTR)).setIcon(LAYOUT_COMPONENT_ICON).getObject();
        }
        return variants;
    }

    /**
     * Get all the tags layout-component in a {@link com.intellij.psi.jsp.JspFile}.
     * <p/>
     * Returns only tags having name attribute.
     *
     * @param jspFile {@link com.intellij.psi.jsp.JspFile} to search tags for
     * @return a {@link java.util.List} of tags
     */
    private static List<XmlTag> getLayoutComponents(JspFile jspFile) {
        List<XmlTag> retval = new ArrayList<XmlTag>(16);
        XmlTag layoutDefTag = StripesUtil.findTag(jspFile.getDocument().getRootTag(), LAYOUT_DEFINITION_FILTER);
        if (null != layoutDefTag) {
            StripesUtil.collectTags(jspFile.getDocument().getRootTag(),
                    LAYOUT_COMPONENT_FILTER, StripesReferenceUtil.NAME_ATTR_FILTER,
                    new XmlTagContainer<List<XmlTag>>(retval) {
                        public void add(XmlTag tag) {
                            container.add(tag);
                        }
                    });
        }
        return retval;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    @Override
    @Nullable
    public PsiElement resolve() {
        for (XmlTag tag : getLayoutComponents(layoutDefJspFile)) {
            if (getCanonicalText().equals(tag.getAttributeValue(NAME_ATTR))) {
                return tag;
            }
        }
        return null;
    }

    /**
     * When Method will renamed
     *
     * @param newElementName the new methodName
     * @return Element
     * @throws com.intellij.util.IncorrectOperationException
     *
     */
    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        ((XmlAttribute) xmlAttributeValue.getParent()).setValue(newElementName);
        return resolve();
    }
}