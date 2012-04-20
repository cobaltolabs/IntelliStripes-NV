package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.RESOLUTION_ICON;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:43
 */
public class JavaStringResolutionMethodsReference extends JavaStringReference {
// ------------------------------ FIELDS ------------------------------

    private static final Pattern PATTERN = Pattern.compile("!");
    private PsiClass actionBeanPsiClass;

// --------------------------- CONSTRUCTORS ---------------------------

    public JavaStringResolutionMethodsReference(PsiLiteralExpression expression, PsiClass actionBeanPsiClass) {
        super(expression);
        this.actionBeanPsiClass = actionBeanPsiClass;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PsiReference ---------------------

    public TextRange getRangeInElement() {
        String txt = expression.getText();
        return new TextRange(txt.length() > 1 && txt.charAt(1) == '!' ? 2 : 1, txt.length() - 1);
    }

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

    @Override
    public String getCanonicalText() {
        return StringUtil.stripQuotesAroundValue(
                expression.getText().charAt(1) == '!' ? PATTERN.matcher(expression.getText()).replaceFirst("") : expression.getText()
        );
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
        return ElementManipulators.getManipulator(expression).handleContentChange(expression, newElementName);
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
