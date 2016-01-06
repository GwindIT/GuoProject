package com.jin.uitoolkit.snackbar.listeners;


import com.jin.uitoolkit.snackbar.Snackbar;

/**
 * Interface used to notify of all {@link com.jin.uitoolkit.snackbar.Snackbar} display events. Useful if you want
 * to move other views while the Snackbar is on screen.
 */
public interface EventListener {
    /**
     * Called when a {@link com.jin.uitoolkit.snackbar.Snackbar} is about to enter the screen
     *
     * @param snackbar the {@link com.jin.uitoolkit.snackbar.Snackbar} that's being shown
     */
    public void onShow(Snackbar snackbar);

    /**
     * Called when a {@link com.jin.uitoolkit.snackbar.Snackbar} is about to enter the screen while
     * a {@link com.jin.uitoolkit.snackbar.Snackbar} is about to exit the screen by replacement.
     *
     * @param snackbar the {@link com.jin.uitoolkit.snackbar.Snackbar} that's being shown
     */
    public void onShowByReplace(Snackbar snackbar);

    /**
     * Called when a {@link com.jin.uitoolkit.snackbar.Snackbar} is fully shown
     *
     * @param snackbar the {@link com.jin.uitoolkit.snackbar.Snackbar} that's being shown
     */
    public void onShown(Snackbar snackbar);

    /**
     * Called when a {@link com.jin.uitoolkit.snackbar.Snackbar} is about to exit the screen
     *
     * @param snackbar the {@link com.jin.uitoolkit.snackbar.Snackbar} that's being dismissed
     */
    public void onDismiss(Snackbar snackbar);

    /**
     * Called when a {@link com.jin.uitoolkit.snackbar.Snackbar} is about to exit the screen
     * when a new {@link com.jin.uitoolkit.snackbar.Snackbar} is about to enter the screen.
     *
     * @param snackbar the {@link com.jin.uitoolkit.snackbar.Snackbar} that's being dismissed
     */
    public void onDismissByReplace(Snackbar snackbar);

    /**
     * Called when a {@link com.jin.uitoolkit.snackbar.Snackbar} had just been dismissed
     *
     * @param snackbar the {@link com.jin.uitoolkit.snackbar.Snackbar} that's being dismissed
     */
    public void onDismissed(Snackbar snackbar);
}
