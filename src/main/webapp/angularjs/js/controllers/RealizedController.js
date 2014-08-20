'use strict';

teamagApp.controller('RealizedController', ['$scope', '$http', 'Task', 'Member', 'WorkRealized',
    function ($scope, $http, Task, Member) {


        $scope.members = Member.query(function () {
            $scope.selectedMember = $scope.members[0];
            $scope.loadRealized();
        }, function (error) {
            $scope.error = 'Erreur HTTP ' + error.status;
        });

        $scope.months = ["Janv", "Fev", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Dec"];
        $scope.months_number = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

        $scope.nonEmptyTask = false;

        $scope.orderActivity = 'activity';
        $scope.orderProp = 'month';

        $scope.loadRealized = function () {
            $http.get('../resources/workrealized/' + $scope.selectedMember.id).success(function (data) {
                $scope.worksRealized = data;
            }, function (error) {
                $scope.error = error;

            });
        };

        $scope.save = function () {
            $http.put('../resources/workrealized/', $scope.worksRealized).success(function (data) {
                $scope.confirmation = 'Mise à jour effectuée';
                $scope.error = '';
            }, function (error) {

            }).error(function (data, status, headers, config) {
                $scope.confirmation = '';
                $scope.error = 'Erreur HTTP' + status;
            });
        };


        $scope.displayTotalRealized = function (workRealized) {
            var total = 0;
            angular.forEach(workRealized.workRealizedList, function (realizedMonth) {
                total += realizedMonth.realized;
            });
            return total;
        };


        $scope.displayRealized = function (realized) {
            return realized !== 0;
        };

        $scope.displayTotalMonth = function (monthNumber) {

            var total = 0;
            angular.forEach($scope.worksRealized, function (workRealized) {

                angular.forEach(workRealized.workRealizedList, function (realizedMonth) {
                    if (realizedMonth.month === monthNumber) {
                        total += realizedMonth.realized;
                    }

                });
            });
            return total;
        };

        $scope.filterActivity = function (workRealized) {
            var re = new RegExp($scope.queryActivity, 'i');
            return re.test(workRealized.task.name) || re.test(workRealized.task.activity.name);
        };

        $scope.filterNonEmptyTask = function (workRealized) {
            if ($scope.nonEmptyTask) {
                var total = 0;
                angular.forEach(workRealized.workRealizedList, function (realizedMonth) {
                    total += realizedMonth.realized;
                });
                if (total === 0) {
                    return false;
                }
            }
            return true;
        };
    }
]);