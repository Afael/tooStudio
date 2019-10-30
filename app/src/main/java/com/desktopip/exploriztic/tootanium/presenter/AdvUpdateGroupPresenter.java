package com.desktopip.exploriztic.tootanium.presenter;

public interface AdvUpdateGroupPresenter {

    /**
     *
     * @param username
     * @param groupId
     * @param groupName
     * @param groupDesc
     */
    void updateGroup(
            String username
            , String groupId
            , String groupName
            , String groupDesc
    );
}
