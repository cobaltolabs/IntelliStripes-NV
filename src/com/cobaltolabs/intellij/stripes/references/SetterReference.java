package com.cobaltolabs.intellij.stripes.references;

import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.cobaltolabs.intellij.stripes.util.StripesUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;

import java.util.LinkedList;
import java.util.List;

import static com.cobaltolabs.intellij.stripes.references.StripesReferenceUtil.EMPTY_STRING_LIST;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 15:21
 */
public class SetterReference<T extends PsiElement> extends PsiReferenceBase<T> {
// ------------------------------ FIELDS ------------------------------

    protected Boolean supportBraces;
    protected Boolean hasBraces = false;
    private PsiClass actionBeanPsiClass;

// --------------------------- CONSTRUCTORS ---------------------------

    public SetterReference(T element, TextRange range, Boolean supportBraces) {
        super(element, range);
        this.supportBraces = supportBraces;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public PsiClass getActionBeanPsiClass() {
        return actionBeanPsiClass;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    /**
     * Resolves reference to method
     * Must return only valid Stripes setter.
     */
    public PsiElement resolve() {
        if (getVariantsEx().contains(getValue())) {
            return resolveEx();
        }

        PsiMethod method = PropertyUtil.findPropertySetter(getActionBeanPsiClass(), getValue(), false, true);
        if (!StripesUtil.isActionBeanPropertySetter(method, false)) return null;

        if (this.supportBraces) {
            PsiType propertyType = method.getParameterList().getParameters()[0].getType();
            PsiClass propertyClass = PsiUtil.resolveClassInType(propertyType);
            Boolean indexedType = StripesReferenceUtil.isIndexedType(propertyType, propertyClass, getElement().getProject());

            method = hasBraces() && !indexedType ? null : method;
        }

        return method;
    }

    public PsiElement handleElementRename(final String newElementName) throws IncorrectOperationException {
        final String name = PropertyUtil.getPropertyName(newElementName);
        return super.handleElementRename(name == null ? newElementName : name);
    }

    public Object[] getVariants() {
        List<String> retval = new LinkedList<String>();
        retval.addAll(StripesReferenceUtil.getWritableProperties(getActionBeanPsiClass(), supportBraces));
        retval.addAll(0, getVariantsEx());

        return StripesReferenceUtil.getVariants(retval, StripesConstants.FIELD_ICON);
    }

// -------------------------- OTHER METHODS --------------------------

    protected List<String> getVariantsEx() {
        return EMPTY_STRING_LIST;
    }

    private Boolean hasBraces() {
        return this.hasBraces;
    }

    protected PsiElement resolveEx() {
        return null;
    }
}
