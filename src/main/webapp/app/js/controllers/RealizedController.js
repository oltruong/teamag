'use strict';

teamagApp.controller('RealizedController', ['$scope', '$http', 'Task', 'WorkRealized',
    function ($scope, $http, Task, WorkRealized) {


        var month_name = new Array(12);
        month_name[0] = "Janv";
        month_name[1] = "Fev";
        month_name[2] = "Mars";
        month_name[3] = "Avril";
        month_name[4] = "Mai";
        month_name[5] = "Juin";
        month_name[6] = "Juil";
        month_name[7] = "Août";
        month_name[8] = "Sept";
        month_name[9] = "Oct";
        month_name[10] = "Nov";
        month_name[11] = "Dec";

        $scope.months = new Array(12);
        for (var i = 0; i < 12; i++) {
            $scope.months[i] = month_name[i];
        }

        $http.get('../resources/task/withactivity').success(function (data) {
            $scope.tasks = data;

            $scope.taskMonth = {};

            angular.forEach($scope.tasks, function (task) {
                var taskmonths = new Array(12);
                for (var i = 0; i < 12; i++) {
                    taskmonths[i] = i + 2;
                }
                $scope.taskMonth[task.id] = taskmonths;
            });


//            $scope.dummyObject = {};
//            $scope.dummyObject.prop = "monguidon";
//            $scope.dummyObject.prop2 = "selle";
//
//            for (var task in data) {
//                var taskmonths = new Array(12);
//                for (var i = 0; i < 12; i++) {
//                    taskmonths[i] = task.name + i + 2;
//                }
//                alert('taskname' + task.name + task.activity);
//                $scope.taskMonth[task.name] = taskmonths;
//


        }, function (error) {
            $scope.error = error;

        });


    }
]);
