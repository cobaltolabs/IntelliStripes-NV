package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:43
 */
public abstract class JavaStringReference extends StripesReference {
// ------------------------------ FIELDS ------------------------------

    protected PsiLiteralExpression expression;

// --------------------------- CONSTRUCTORS ---------------------------

    public JavaStringReference(PsiLiteralExpression expression) {
        this.expression = expression;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    public PsiElement getElement() {
        return expression;
    }

    public TextRange getRangeInElement() {
        return new TextRange(1, expression.getText().length() - 1);
    }

    @Override
    public String getCanonicalText() {
        return StringUtil.stripQuotesAroundValue(expression.getText());
    }
}
