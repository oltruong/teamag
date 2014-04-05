'use strict';

teamagApp.controller('TaskController', ['$scope', '$http', '$location', '$routeParams', 'Task',
    function ($scope, $http, $location, $routeParams, Task) {


        $scope.confirmation = $routeParams.confirmation;

        $scope.tasks = Task.query(function () {
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
            Task.update({id: $task.id}, $task, function () {
                $scope.confirmation = "Tâche mise à jour";
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });
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
                $location.path('tasks').search({confirmation: 'Task ' + $scope.newTask.project + '-' + $scope.newTask.name + ' ajouté'});
            }, function (error) {
                if (error.status == "406") {
                    $scope.warning = "Le BC " + $scope.newTask.project + '-' + $scope.newTask.name + " existe déjà";
                } else {
                    $scope.error = 'Erreur HTTP' + error.status;
                }
            });
        };
    }
]);
