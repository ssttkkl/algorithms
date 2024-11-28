package io.ssttkkl.algorithms.bst

interface BidirectionIterator<out T> : Iterator<T> {
    /**
     * Returns `true` if there are elements in the iteration before the current element.
     */
    public fun hasPrevious(): Boolean

    /**
     * Returns the previous element in the iteration and moves the cursor position backwards.
     *
     * @throws NoSuchElementException if the iteration has no previous element.
     */
    public fun previous(): T
}

interface MutableBidirectionIterator<out T> : MutableIterator<T>, BidirectionIterator<T>