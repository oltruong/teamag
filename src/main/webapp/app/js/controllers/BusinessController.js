'use strict';

teamagApp.controller('BusinessController', ['$scope', '$http',
    function ($scope, $http) {
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
        $scope.create = function () {
            alert('coucousubmit');
            $http.post('../resources/business/newbc', JSON.stringify($scope.newbc)).success(function () {/*success callback*/
            }).error(function (data, status, headers, config) {
                alert("error " + status);
            });
        };
    }
]);
