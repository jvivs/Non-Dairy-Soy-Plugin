/*
 * Copyright 2011 Ed Venaglia
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

package net.venaglia.nondairy.soylang.parser.permutations.impl;

import net.venaglia.nondairy.soylang.parser.permutations.PermutationConsumer;
import net.venaglia.nondairy.soylang.parser.permutations.Permutator;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;

/**
 * Simple composite permutator that combines the permutations of several
 * iterators.
 */
public final class CompositePermutator implements Permutator {

    private final Collection<Permutator> permutators;

    public CompositePermutator(Collection<Permutator> permutators) {
        this.permutators = permutators;
    }

    @Override
    public void permutate(CharSequence source,
                          @NonNls String initialState,
                          PermutationConsumer consumer)
            throws Exception {
        for (Permutator permutator : permutators) {
            consumer.setPermutatorName(permutator.getClass().getSimpleName());
            permutator.permutate(source, initialState, consumer);
        }
    }
}
