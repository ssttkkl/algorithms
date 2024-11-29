/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea and Josh Bloch with assistance from members of JCP
 * JSR-166 Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package io.ssttkkl.algorithms.bst

/**
 * A [SortedSet] extended with navigation methods reporting
 * closest matches for given search targets. Methods [.lower],
 * [.floor], [.ceiling], and [.higher] return elements
 * respectively less than, less than or equal, greater than or equal,
 * and greater than a given element, returning `null` if there
 * is no such element.
 *
 *
 * A `NavigableSet` may be accessed and traversed in either
 * ascending or descending order.  The [.descendingSet] method
 * returns a view of the set with the senses of all relational and
 * directional methods inverted. The performance of ascending
 * operations and views is likely to be faster than that of descending
 * ones.  This interface additionally defines methods [ ][.pollFirst] and [.pollLast] that return and remove the lowest
 * and highest element, if one exists, else returning `null`.
 * Methods
 * [subSet(E, boolean, E, boolean)][.subSet],
 * [headSet(E, boolean)][.headSet], and
 * [tailSet(E, boolean)][.tailSet]
 * differ from the like-named `SortedSet` methods in accepting
 * additional arguments describing whether lower and upper bounds are
 * inclusive versus exclusive.  Subsets of any `NavigableSet`
 * must implement the `NavigableSet` interface.
 *
 *
 * The return values of navigation methods may be ambiguous in
 * implementations that permit `null` elements. However, even
 * in this case the result can be disambiguated by checking
 * `contains(null)`. To avoid such issues, implementations of
 * this interface are encouraged to *not* permit insertion of
 * `null` elements. (Note that sorted sets of [ ] elements intrinsically do not permit `null`.)
 *
 *
 * Methods
 * [subSet(E, E)][.subSet],
 * [headSet(E)][.headSet], and
 * [tailSet(E)][.tailSet]
 * are specified to return `SortedSet` to allow existing
 * implementations of `SortedSet` to be compatibly retrofitted to
 * implement `NavigableSet`, but extensions and implementations
 * of this interface are encouraged to override these methods to return
 * `NavigableSet`.
 *
 *
 * This interface is a member of the
 * [
 * Java Collections Framework]({@docRoot}/java.base/java/util/package-summary.html#CollectionsFramework).
 *
 * @author Doug Lea
 * @author Josh Bloch
 * @param <E> the type of elements maintained by this set
 * @since 1.6
</E> */
interface NavigableSet<E> : MutableSortedSet<E> {
    /**
     * Returns the greatest element in this set strictly less than the
     * given element, or `null` if there is no such element.
     *
     * @param e the value to match
     * @return the greatest element less than `e`,
     * or `null` if there is no such element
     * @throws ClassCastException if the specified element cannot be
     * compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     * and this set does not permit null elements
     */
    fun lower(e: E): E?

    /**
     * Returns the greatest element in this set less than or equal to
     * the given element, or `null` if there is no such element.
     *
     * @param e the value to match
     * @return the greatest element less than or equal to `e`,
     * or `null` if there is no such element
     * @throws ClassCastException if the specified element cannot be
     * compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     * and this set does not permit null elements
     */
    fun floor(e: E): E?

    /**
     * Returns the least element in this set greater than or equal to
     * the given element, or `null` if there is no such element.
     *
     * @param e the value to match
     * @return the least element greater than or equal to `e`,
     * or `null` if there is no such element
     * @throws ClassCastException if the specified element cannot be
     * compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     * and this set does not permit null elements
     */
    fun ceiling(e: E): E?

    /**
     * Returns the least element in this set strictly greater than the
     * given element, or `null` if there is no such element.
     *
     * @param e the value to match
     * @return the least element greater than `e`,
     * or `null` if there is no such element
     * @throws ClassCastException if the specified element cannot be
     * compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     * and this set does not permit null elements
     */
    fun higher(e: E): E?
}
