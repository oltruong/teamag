teamagApp.directive('tasklist', function () {
    return {
        scope: {
            taskList: '=list'
        },
        controller: 'taskListController',
        templateUrl: "partials/directives/tasklist.html",
        restrict: "E"
    }
});

teamagApp.controller('taskListController', ['$scope',
    function ($scope) {
        $scope.taskList = [];
        $scope.filteredTaskList = [];
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

        $scope.filteredTotal = function () {
            var total = 0;
            for (var i = 0; i < $scope.filteredTaskList.length; i++) {
                total += $scope.filteredTaskList[i].total;

            }
            return total;
        };


    }

])
;
