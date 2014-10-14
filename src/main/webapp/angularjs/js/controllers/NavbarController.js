'use strict';

teamagApp.controller('NavbarController', ['$scope', '$location', '$routeParams', 'userInfo', '$http',
    function NavbarController($scope, $location, $routeParams, userInfo, $http) {

        $scope.loggedIn = userInfo.loggedIn;

        $scope.name = userInfo.name;
        $scope.routeIs = function (routeName) {
            return $location.path() === routeName;
        };

        $scope.$watch(function () {
            return  userInfo;
        }, function (userInfoChanged) {
            $scope.loggedIn = userInfoChanged.loggedIn;
            $scope.name = userInfoChanged.name;
            $scope.admin = userInfoChanged.admin;
            $scope.supervisor = userInfoChanged.supervisor;
        }, true);


        $scope.disconnect = (function () {
            userInfo.loggedIn = false;
            userInfo.admin = false;
            userInfo.supervisor = false;
            userInfo.name = undefined;
            $http.defaults.headers.common.Authorization = undefined;
            $http.defaults.headers.common.userid = undefined;
            $location.path('home');
        })

    }]);
