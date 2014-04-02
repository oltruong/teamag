'use strict';

teamagApp.controller('MemberController', ['$scope', '$http', 'Member',
    function ($scope, $http, Member) {


        $scope.members = Member.query(function () {
        }, function (error) {
            $scope.error = 'Erreur HTTP ' + error.status;
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
