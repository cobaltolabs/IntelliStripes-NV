package com.cobaltolabs.intellij.stripes.references;

import com.cobaltolabs.intellij.stripes.util.StripesUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.FIELD_ICON;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.FILE_BEAN;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:34
 */
public class FileBeanSetterReference extends PsiReferenceBase<XmlAttributeValue> {
// ------------------------------ FIELDS ------------------------------

    public static final String SET = "set";
    private static final Pattern PATTERN = Pattern.compile("\\[.*?\\]");
    private PsiClass actionBeanPsiClass;

// --------------------------- CONSTRUCTORS ---------------------------

    public FileBeanSetterReference(XmlAttributeValue xmlAttributeValue, PsiClass actionBeanPsiClass) {
        super(xmlAttributeValue);
        this.actionBeanPsiClass = actionBeanPsiClass;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    public TextRange getRangeInElement() {
        int i = getElement().getText().indexOf('[');
        return i == -1 ? super.getRangeInElement() : new TextRange(1, i);
    }

    @Nullable
    public PsiElement resolve() {
        PsiMethod[] arr = actionBeanPsiClass.findMethodsByName(SET + StringUtil.capitalize(PATTERN.matcher(getValue()).replaceAll("")), true);
        if (arr.length > 0) {
            PsiMethod psiMethod = arr[0];

            PsiType propertyType = psiMethod.getParameterList().getParameters()[0].getType();
            PsiClass propertyClass = StripesReferenceUtil.resolveClassInType(propertyType, actionBeanPsiClass.getProject());

            if (StripesUtil.isSubclass(getElement().getProject(), FILE_BEAN, propertyClass)) {
                if (getValue().indexOf('[') > 0) {
                    propertyClass = PsiUtil.resolveClassInType(propertyType);
                    return StripesReferenceUtil.isIndexedType(propertyType, propertyClass, getElement().getProject()) ? psiMethod : null;
                }
                return psiMethod;
            }
        }
        return null;
    }

    public Object[] getVariants() {
        return StripesReferenceUtil.getVariants(StripesReferenceUtil.getFileBeanProperties(actionBeanPsiClass), FIELD_ICON);
    }
}