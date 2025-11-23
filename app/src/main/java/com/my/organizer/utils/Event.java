package com.my.organizer.utils;

/**
 * A lifecycle-safe wrapper for one-time events sent from ViewModel → Fragment.
 *
 * Why needed?
 * LiveData replays the last value on configuration change (rotation),
 * so without this wrapper, Toast/Snackbar might fire twice.
 *
 * Usage:
 * viewModel.getDeleteEvent().observe(this, event -> {
 *     Boolean success = event.getIfNotHandled();
 *     if (success == null) return;  // event already handled
 *     if (success) {
 *         // show success message
 *     } else {
 *         // show failure message
 *     }
 * });
 */
public class Event<T> {

    private final T content;
    private boolean handled = false;

    public Event(T content) {
        this.content = content;
    }

    /**
     * Returns content ONLY if it has not been handled.
     * The first caller receives the value, future callers get null.
     */
    public synchronized T getIfNotHandled() {
        if (handled) return null;
        handled = true;
        return content;
    }

    /**
     * Returns content even if already handled.
     */
    public T peek() {
        return content;
    }
}
