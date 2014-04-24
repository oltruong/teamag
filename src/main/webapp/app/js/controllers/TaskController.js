'use strict';

teamagApp.controller('TaskController', ['$scope', '$http', '$location', '$routeParams', 'Task', 'Activity',
    function ($scope, $http, $location, $routeParams, Task, Activity) {


        $scope.confirmation = $routeParams.confirmation;

        $scope.tasks = Task.query(function () {
            var tasksLength = $scope.tasks.length;
            for (var i = 0; i < tasksLength; i++) {

                if ($scope.tasks[i].task != null) {
                    for (var j = 0; j < tasksLength; j++) {
                        if ($scope.tasks[i].task.id == $scope.tasks[j].id) {
                            $scope.tasks[i].task = $scope.tasks[j];
                        }
                    }
                }

            }

            $scope.activities = Activity.query(function (data) {
                var activitiesLength = $scope.activities.length;
                for (var i = 0; i < tasksLength; i++) {

                    if ($scope.tasks[i].activity != null) {
                        for (var j = 0; j < activitiesLength; j++) {
                            if ($scope.tasks[i].activity.id == $scope.activities[j].id) {
                                $scope.tasks[i].activity = $scope.activities[j];
                            }
                        }
                    }

                }

            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });


        }, function (error) {
            $scope.error = 'Erreur HTTP' + error.status;
        });

        $scope.orderProp = 'project';


        $scope.deleteTask = function ($taskId) {
            Task.delete({id: $taskId}, function () {
                $scope.tasks = Task.query();
                $scope.confirmation = "Tâche supprimée";
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });
        };


        $scope.updateTask = function ($task) {
            if ($task.task != null && $task.id == $task.task.id) {
                alert('vous ne pouvez assigner la même tâche en parente!');
                $task.task = null;
            } else {
                Task.update({id: $task.id}, $task, function () {
                    $scope.confirmation = "Tâche mise à jour";
                }, function (error) {
                    $scope.error = 'Erreur HTTP' + error.status;
                });
            }

        };


        $scope.mergeTask = function ($task) {
            if ($task.task != null && $task.id == $task.task.id) {
                alert('vous ne pouvez fusionner la même tâche en parente!');
                $task.task = null;
            } else {
                alert('fusion');
//                Task.update({id: $task.id}, $task, function () {
//                    $scope.confirmation = "Tâche mise à jour";
//                }, function (error) {
//                    $scope.error = 'Erreur HTTP' + error.status;
//                });
            }

        };


        $scope.refreshTask = function ($task) {

            var oldTask = Task.get({id: $task.id}, function () {
                $task.comment = oldTask.comment;
                $task.identifier = oldTask.identifier;
                $task.name = oldTask.name;
                $task.amount = oldTask.amount;
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });

        }

        $scope.create = function () {
            Task.save($scope.newTask, function () {
                $location.path('tasks').search({confirmation: 'Tâche ' + $scope.newTask.project + '-' + $scope.newTask.name + ' ajoutée'});
            }, function (error) {
                if (error.status == "406") {
                    $scope.warning = "Création invalide";
                } else {
                    $scope.error = 'Erreur HTTP' + error.status;
                }
            });
        };

    }
]);
