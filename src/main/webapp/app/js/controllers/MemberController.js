'use strict';

teamagApp.controller('MemberController', ['$scope', '$http',
    function ($scope, $http) {

        $http.get('../resources/member').success(function (data) {
            $scope.members = data;
        }).
            error(function (data, status, headers, config) {
                alert("error " + status);
            });

        $scope.orderProp = 'name';

        $scope.totalDays = function () {
            var totalDays = 0;
            angular.forEach($scope.filteredMembers, function (member) {

                totalDays += member.estimatedWorkDays;
            })
            return totalDays;
        }

        $scope.totalMonths = function () {
            var totalMonths = 0;
            angular.forEach($scope.filteredMembers, function (member) {

                totalMonths += member.estimatedWorkMonths;
            })
            return totalMonths;
        };

    }]);
