'use strict';

teamagApp.controller('TaskListController', ['$scope',
    function ($scope) {
        $scope.order = 'name';
        $scope.setOrder = function (value) {
            if (value === $scope.order) {
                $scope.order = "-" + value;
            } else {
                $scope.order = value;
            }
        };

        $scope.showOrderClass = function (value) {

            if (value === $scope.order) {
                return "glyphicon glyphicon-chevron-up";
            } else if ("-" + value === $scope.order) {
                return "glyphicon glyphicon-chevron-down";
            } else {
                return "hidden";
            }
        };

        $scope.total = function () {
            var total = 0;
            for (var i = 0; i < $scope.taskList.length; i++) {
                total += $scope.taskList[i].total;

            }
            return total;
        };


    }

])
;

