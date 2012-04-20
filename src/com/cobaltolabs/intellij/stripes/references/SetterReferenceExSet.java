package com.cobaltolabs.intellij.stripes.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 18:59
 */
public class SetterReferenceExSet extends StripesReferenceSetBase<SetterReferenceEx<PsiElement>> {
// ------------------------------ FIELDS ------------------------------

    private static final Pattern BRACES = Pattern.compile("\\$\\{.*?\\}");
    private static final Pattern BRACKETS = Pattern.compile("\\[.*?\\$\\{.*?\\}\\.*?\\]");

// --------------------------- CONSTRUCTORS ---------------------------

    public SetterReferenceExSet(@NotNull PsiElement element, int offset, char separator,
                                PsiClass actionBeanPsiClass, Boolean supportBraces) {
        super(element, offset, separator, actionBeanPsiClass, supportBraces);
    }

    public SetterReferenceExSet(String str, @NotNull PsiElement element, int offset, char separator,
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
                TextRange range = new TextRange(getOffset() + wStart, getOffset() + wEnd + 1);
                String refText = new TextRange(range.getStartOffset() - 1, range.getEndOffset() - 1).substring(getStr());
                if (!BRACES.matcher(refText).find()
                        || BRACKETS.matcher(refText).find()) {
                    final SetterReferenceEx<PsiElement> referenceWithBraces = createReferenceWithBraces(range, index++, wEnd != (i - 1));
                    retval.add(referenceWithBraces);
                } else {
                    return retval;
                }

                wStart = i + 1;
            } else if (charAt == '[') {
                lBrace++;
            } else if (charAt == ']') {
                lBrace--;
            } else if (lBrace == 0) {
                wEnd = i;
            }

            if (i == (getStr().length() - 1)) {
                TextRange range = new TextRange(getOffset() + wStart, getOffset() + (wStart < wEnd ? wEnd + 1 : i + 1));
                String refText = new TextRange(range.getStartOffset() - 1, range.getEndOffset() - 1).substring(getStr());
                if (!BRACES.matcher(refText).find()
                        || BRACKETS.matcher(refText).find()) {
                    retval.add(createReferenceWithBraces(range, index++, wEnd != i && lBrace == 0));
                }
            }
        }
        return retval;
    }
}
