/*
 * Copyright 2010 - 2012 Ed Venaglia
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.venaglia.nondairy.soylang.elements.path;

import net.venaglia.nondairy.soylang.SoyElement;
import org.jetbrains.annotations.NotNull;

/**
 * User: ed
 * Date: 1/27/12
 * Time: 6:19 PM
 *
 * A PsiElementPath that will find template tab elements by their fully
 * qualified name.
 *
 * This path makes use of {@link SoyFileElementTraversalPredicate} to navigate
 * to other files.
 */
public class TemplatePath extends PsiElementPath {

    private TemplatePath(@NotNull String templateName) {
        super(buildPath(templateName));
    }

    private static ElementPredicate[] buildPath(@NotNull String templateName) {
        templateName = templateName.trim();
        if (templateName.length() == 0) {
            throw new IllegalArgumentException("template name cannot be blank.");
        }
        if (templateName.endsWith(".") || templateName.startsWith(".")) {
            throw new IllegalArgumentException("invalid template name: " + templateName);
        }
        int lastDot = templateName.lastIndexOf('.');
        if (lastDot < 0) {
            throw new IllegalArgumentException("invalid template name: " + templateName);
        }
        String namespace = templateName.substring(0, lastDot);
//        templateName = templateName.substring(lastDot + 1);
        return new ElementPredicate[]{
                SoyFileElementTraversalPredicate.filesForNamespace(namespace),
                new ElementTypePredicate(SoyElement.soy_file).onChildren(),
                new ElementTypePredicate(SoyElement.template_tag).onDescendants(2,3),
                new TemplateNamePredicate(templateName),
                new ElementTypePredicate(SoyElement.tag_between_braces).onChildren(),
                new ElementTypePredicate(SoyElement.template_name).onChildren()
        };
    }

    /**
     * Safely builds a new {@link PsiElementPath} for the specified template 
     * name. If the template name is malformed, {@link PsiElementPath#EMPTY} is
     * returned.
     * @param templateName The template name to navigate to.
     * @return A path object to navigate to the specified template.
     */
    @NotNull
    public static PsiElementPath forTemplateName(@NotNull String templateName) {
        try {
            return new TemplatePath(templateName);
        } catch (IllegalArgumentException e) {
            return PsiElementPath.EMPTY;
        }
    }
}
