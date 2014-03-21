'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
//    controller('MemberController', function MemberController($scope, $http) {
    controller('MemberController', ['$scope', '$http',
        function ($scope, $http) {
            $http.get('../resources/member').success(function (data) {
                $scope.members = data;
            }).
                error(function (data, status, headers, config) {
                    alert("error " + status);
                });
            $scope.orderProp = 'name';
        }])
    .controller('MyCtrl2', [function () {

    }]);