'use strict';

teamagApp.controller('ProfileController', ['$scope', '$http',
    function ($scope, $http) {

        $http.get('../resources/member/profile').success(function (member) {
            $scope.member = member;
        });
    }]);
