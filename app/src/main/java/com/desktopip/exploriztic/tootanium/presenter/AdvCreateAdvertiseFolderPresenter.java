package com.desktopip.exploriztic.tootanium.presenter;

public interface AdvCreateAdvertiseFolderPresenter {

    /**
     *
     * @param username
     * @param folderName
     * @param password
     * @param groupId
     */
    void createAdvertise(
            String username
            , String password
            , String folderName
            , String groupId
    );
}
