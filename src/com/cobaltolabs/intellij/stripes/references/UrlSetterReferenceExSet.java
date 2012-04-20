package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 18:59
 */
public class UrlSetterReferenceExSet extends StripesReferenceSetBase<SetterReferenceEx<PsiElement>> {
// --------------------------- CONSTRUCTORS ---------------------------

    public UrlSetterReferenceExSet(String str, @NotNull PsiElement element, int offset, char separator,
                                   PsiClass actionBeanPsiClass, Boolean supportBraces) {
        super(str, element, offset, separator, actionBeanPsiClass, supportBraces);
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * This method should never be directly used.
     *
     * @param range
     * @param index
     * @return
     */
    @NotNull
    protected final SetterReferenceEx<PsiElement> createReference(TextRange range, int index) {
        return this.createReferenceWithBraces(range, index, false);
    }

    @NotNull
    protected SetterReferenceEx<PsiElement> createReferenceWithBraces(TextRange range, int index, boolean hasBraces) {
        return new SetterReferenceEx<PsiElement>(range, isSupportBraces(), hasBraces, this, index);
    }

    @Override
    protected List<SetterReferenceEx<PsiElement>> initRefs() {
        if (!isSupportBraces()) {
            return super.initRefs();
        }

        List<SetterReferenceEx<PsiElement>> retval = new ArrayList<SetterReferenceEx<PsiElement>>(8);
        for (int i = 0, wStart = 0, lBrace = 0, index = 0, wEnd = 0; i < getStr().length(); i++) {
            final char charAt = getStr().charAt(i);
            if (charAt == '.' && lBrace == 0) {
                retval.add(createReferenceWithBraces(new TextRange(getOffset() + wStart, getOffset() + wEnd + 1), index++, wEnd != (i - 1)));
                wStart = i + 1;
            } else if (charAt == '[') {
                lBrace++;
            } else if (charAt == ']') {
                lBrace--;
            } else if (lBrace == 0) {
                wEnd = i;
            }

            if (i == (getStr().length() - 1)) {
                retval.add(createReferenceWithBraces(new TextRange(getOffset() + wStart, getOffset() + (wStart < wEnd ? wEnd + 1 : i + 1)), index++, wEnd != i && lBrace == 0));
            }
        }
        return retval;
    }
}
