'use strict';

teamagApp.controller('BusinessController', ['$scope', '$http', '$location', '$routeParams',
    function ($scope, $http, $location, $routeParams) {


        $scope.confirmation = $routeParams.confirmation;

        $http.get('../resources/business/bc').success(function (data) {
            $scope.businesscases = data;
        }).
            error(function (data, status, headers, config) {
                alert("error " + status);
            });
        $scope.orderProp = 'name';
        $scope.total = function () {
            var total = 0;
            angular.forEach($scope.filteredBC, function (bc) {
                total += bc.amount;
            })
            return total;
        };

        $scope.deleteBc = function ($bcId) {
            $http.delete('../resources/business/bc/' + $bcId).success(function (data, status, headers, config) {/*success callback*/
                $http.get('../resources/business/bc').success(function (data) {
                    $scope.businesscases = data;
                }).
                    error(function (data, status, headers, config) {
                        alert("error " + status);
                    });
                $scope.confirmation = "BC supprimé";
            }).error(function (data, status, headers, config) {
                $scope.error = 'Erreur HTTP' + status;
            });
        };

        $scope.create = function () {
            $http.post('../resources/business/bc', JSON.stringify($scope.newbc)).success(function (data, status, headers, config) {/*success callback*/
                if (status == "200") {
                    $location.path('businesscases').search({confirmation: 'Business Case ' + $scope.newbc.identifier + '-' + $scope.newbc.name + ' ajouté'});
                    ;
                } else {
                    $scope.confirmation = status;
                }

            }).error(function (data, status, headers, config) {
                if (status == "406") {
                    $scope.warning = "Le BC " + $scope.newbc.identifier + " existe déjà";
                } else {
                    $scope.error = 'Erreur HTTP' + status;
                }
            });
        };
    }
]);
