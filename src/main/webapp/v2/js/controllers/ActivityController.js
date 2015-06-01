'use strict';

teamagApp.controller('ActivityController', ['$scope', '$http', '$location', '$routeParams', 'BusinessCase', 'Activity',
    function ($scope, $http, $location, $routeParams, BusinessCase, Activity) {


        $scope.confirmation = $routeParams.confirmation;

        $scope.activities = Activity.query(function (data) {

            $scope.businesscases = BusinessCase.query(function (data) {

                var activitiesLength = $scope.activities.length;
                var bcLength = $scope.businesscases.length;
                for (var i = 0; i < activitiesLength; i++) {

                    if ($scope.activities[i].businessCase !== null) {
                        for (var j = 0; j < bcLength; j++) {
                            if ($scope.activities[i].businessCase.id === $scope.businesscases[j].id) {
                                $scope.activities[i].businessCase = $scope.businesscases[j];
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

        $scope.orderProp = 'name';
        $scope.total = function () {
            var total = 0;
            angular.forEach($scope.filteredActivities, function (activity) {
                total += activity.amount;
            });
            return total;
        };

        $scope.deleteActivity = function ($activityId) {
            Activity.delete({id: $activityId}, function () {
                $scope.activities = Activity.query();
                $scope.confirmation = "Activité supprimée";
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });
        };


        $scope.updateActivity = function ($activity) {
            Activity.update({id: $activity.id}, $activity, function () {
                $scope.confirmation = "Activité mise à jour";
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });
        };

        $scope.refreshActivity = function ($activity) {

            var oldActivity = Activity.get({id: $activity.id}, function () {
                $activity.comment = oldActivity.comment;
                $activity.name = oldActivity.name;
                $activity.amount = oldActivity.amount;
                $activity.businessCase = oldActivity.businessCase;
                $activity.delegated = oldActivity.delegated;
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });

        }

        $scope.create = function () {
            Activity.save($scope.newActivity, function () {
                $location.path('activities').search({confirmation: 'Activité ' + $scope.newActivity.name + ' ajoutée'});
            }, function (error) {
                if (error.status === "406") {
                    $scope.warning = "L'activité " + $scope.newActivity.name + " sur BC " + $scope.newActivity.businessCase.identifier + "-" + $scope.newActivity.businessCase.name + " existe déjà";
                } else {
                    $scope.error = 'Erreur HTTP' + error.status;
                }
            });
        };
    }
]);
