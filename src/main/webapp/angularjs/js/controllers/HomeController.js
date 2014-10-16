'use strict';

teamagApp.controller('HomeController', ['$scope', 'userInfo',
    function HomeController($scope, userInfo) {
        $scope.loggedIn = userInfo.loggedIn;

        $scope.name = userInfo.name;

        $scope.$watch(function () {
            return  userInfo;
        }, function (userInfoChanged) {
            $scope.loggedIn = userInfoChanged.loggedIn;
            $scope.name = userInfoChanged.name;
        }, true);


    }]);
