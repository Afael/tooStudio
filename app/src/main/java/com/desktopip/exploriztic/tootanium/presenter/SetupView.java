package com.desktopip.exploriztic.tootanium.presenter;

/**
 * Created by Jayus on 31/07/2018.
 */

public interface SetupView {
    /**
     * Menampilkan notifikasi validation error
     */
    void showValidationSetupError();

    /**
     * Setup success
     */
    void setupSuccess(String success);

    /**
     * Setup failed
     */
    void setupFailed(String failed);

    /**
     * Menampilkan notifikasi setup error
     */
    void setupError(String error);
}
