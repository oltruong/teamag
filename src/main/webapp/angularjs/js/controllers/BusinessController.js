'use strict';

teamagApp.controller('BusinessController', ['$scope', '$http', '$location', '$routeParams', 'BusinessCase',
    function ($scope, $http, $location, $routeParams, BusinessCase) {


        $scope.confirmation = $routeParams.confirmation;

        $scope.businesscases = BusinessCase.query(function () {
        }, function (error) {
            $scope.error = 'Erreur HTTP' + error.status;
        });

        $scope.orderProp = 'identifier';
        $scope.total = function () {
            var total = 0;
            angular.forEach($scope.filteredBC, function (bc) {
                total += bc.amount;
            })
            return total;
        };

        $scope.deleteBc = function ($bcId) {
            BusinessCase.delete({id: $bcId}, function () {
                $scope.businesscases = BusinessCase.query();
                $scope.confirmation = "BusinessCase supprimé";
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });
        };


        $scope.updateBc = function ($bc) {
            BusinessCase.update({id: $bc.id}, $bc, function () {
                $scope.confirmation = "BusinessCase mis à jour";
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });
        };

        $scope.refreshBc = function ($bc) {

            var oldbc = BusinessCase.get({id: $bc.id}, function () {
                $bc.comment = oldbc.comment;
                $bc.identifier = oldbc.identifier;
                $bc.name = oldbc.name;
                $bc.amount = oldbc.amount;
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });

        }

        $scope.create = function () {
            BusinessCase.save($scope.newbc, function () {
                $location.path('businesscases').search({confirmation: 'Business Case ' + $scope.newbc.identifier + '-' + $scope.newbc.name + ' ajouté'});
            }, function (error) {
                if (error.status === "406") {
                    $scope.warning = "Le BC " + $scope.newbc.identifier + " existe déjà";
                } else {
                    $scope.error = 'Erreur HTTP' + error.status;
                }
            });
        };
    }
]);
