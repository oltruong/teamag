'use strict';

teamagApp.controller('ActivityController', ['$scope', '$http', '$location', '$routeParams', 'BusinessCase', 'Activity',
    function ($scope, $http, $location, $routeParams, BusinessCase, Activity) {


        $scope.confirmation = $routeParams.confirmation;

        $scope.activities = Activity.query(function () {
        }, function (error) {
            $scope.error = 'Erreur HTTP' + error.status;
        });


        $scope.businesscases = BusinessCase.query(function (data) {
       }, function (error) {
            $scope.error = 'Erreur HTTP' + error.status;
        });

        $scope.orderProp = 'name';
        $scope.total = function () {
            var total = 0;
            angular.forEach($scope.filteredActivities, function (activity) {
                total += activity.amount;
            })
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
                $activity.bc = oldActivity.bc;
                $activity.delegated = oldActivity.delegated;
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });

        }

        $scope.create = function () {
            Activity.save($scope.newActivity, function () {
                $location.path('activities').search({confirmation: 'Activité ' + $scope.newActivity.name + ' ajoutée'});
            }, function (error) {
                if (error.status == "406") {
                    $scope.warning = "L'activité " + $scope.newActivity.name + " sur BC " + $scope.newActivity.bc.identifier + "-" + $scope.newActivity.bc.name + " existe déjà";
                } else {
                    $scope.error = 'Erreur HTTP' + error.status;
                }
            });
        };
    }
]);
