'use strict';

teamagApp.factory('userInfo', function ($rootScope, localStorage) {

    var LOCAL_STORAGE_ID = 'fmUser',
        userInfoString = localStorage[LOCAL_STORAGE_ID];

    var userInfo = userInfoString ? JSON.parse(userInfoString) : {
        name: undefined,
        loggedIn: false,
        admin: false,
        supervisor: false,
        id: undefined,
        password: undefined
    };

    $rootScope.$watch(function () {
        return userInfo;
    }, function () {
        localStorage[LOCAL_STORAGE_ID] = JSON.stringify(userInfo);
    }, true);

    return userInfo;
});
