package com.cobaltolabs.intellij.stripes.references;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.RESOLUTION_ICON;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:55
 */
public class JspTagAttrResolutionMethodsReference extends JspTagAttrReference {
// ------------------------------ FIELDS ------------------------------

    private PsiClass actionBeanPsiClass;

// --------------------------- CONSTRUCTORS ---------------------------

    public JspTagAttrResolutionMethodsReference(XmlAttributeValue xmlAttributeValue, PsiClass actionBeanPsiClass) {
        super(xmlAttributeValue);
        this.actionBeanPsiClass = actionBeanPsiClass;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    /**
     * Ctrl + Click in the attribute name will be resolved
     *
     * @return Element
     */
    @Nullable
    @Override
    public PsiElement resolve() {
        return StripesReferenceUtil.resolveResolutionMethod(actionBeanPsiClass, getCanonicalText());
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

    /**
     * Get all Posible Resolution Methods for an ActionBean Class
     *
     * @return an Array with References to Resolution Methods
     */
    @Override
    public Object[] getVariants() {
        return StripesReferenceUtil.getVariants(StripesReferenceUtil.getResolutionMethodsNames(actionBeanPsiClass), RESOLUTION_ICON);
    }
}