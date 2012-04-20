package com.cobaltolabs.intellij.stripes.psi;

import com.cobaltolabs.intellij.stripes.util.StripesConstants;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataCache;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.search.CustomPropertyScopeProvider;
import com.intellij.psi.impl.search.JavaSourceFilterScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.containers.hash.HashSet;

import java.util.Collection;
import java.util.Set;

import static com.cobaltolabs.intellij.stripes.util.StripesUtil.findPsiClassByName;
import static com.intellij.psi.search.GlobalSearchScopes.projectProductionScope;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : Mario Arias
 *         Date: 7/12/11
 *         Time: 8:17
 */
public class StripesPropertyScopeProvider implements CustomPropertyScopeProvider {
// ------------------------------ FIELDS ------------------------------

    private static Key<CachedValue<Set<VirtualFile>>> CACHED_FILES_KEY = Key.create("stripesCachedActionBeans");

    private static final UserDataCache<CachedValue<Set<VirtualFile>>, Project, Object> myCachedActionBeansFiles =
            new UserDataCache<CachedValue<Set<VirtualFile>>, Project, Object>() {
                protected CachedValue<Set<VirtualFile>> compute(final Project project, final Object p) {
                    return CachedValuesManager.getManager(project).createCachedValue(new CachedValueProvider<Set<VirtualFile>>() {
                        public Result<Set<VirtualFile>> compute() {
                            return Result.createSingleDependency(getActionBeanVirtualFiles(project), PsiModificationTracker.JAVA_STRUCTURE_MODIFICATION_COUNT);
                        }
                    }, false);
                }
            };

// -------------------------- STATIC METHODS --------------------------

    private static Set<VirtualFile> getActionBeanVirtualFiles(Project project) {
        Set<VirtualFile> retval = new HashSet<VirtualFile>();
        PsiClass actionBeanClass = findPsiClassByName(StripesConstants.ACTION_BEAN, project);
        if (null != actionBeanClass) {
            Collection<PsiClass> actionBeans = ClassInheritorsSearch.search(
                    actionBeanClass, projectProductionScope(project), true).findAll();

            for (PsiClass actionBean : actionBeans) {
                retval.add(actionBean.getContainingFile().getVirtualFile());
            }
        }
        return retval;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface CustomPropertyScopeProvider ---------------------

    public SearchScope getScope(final Project project) {
        final Set<VirtualFile> files = myCachedActionBeansFiles.get(CACHED_FILES_KEY, project, null).getValue();

        return new JavaSourceFilterScope(projectProductionScope(project)) {
            @Override
            public boolean contains(VirtualFile virtualFile) {
                return super.contains(virtualFile) && files.contains(virtualFile);
            }

            @Override
            public boolean isSearchInLibraries() {
                return false;
            }
        };
    }
}
