'use strict';

teamagApp.controller('WorkTaskController', ['$scope', '$http', '$location', '$routeParams', 'Work',
    function ($scope, $http, $location, $routeParams) {

        $scope.taskId = $routeParams.taskId;


        $http.get('resources/works?taskId=' + $scope.taskId).success(function (data) {

            $scope.works = data;
        });
        $scope.orderProp = 'member';

        $scope.total = function () {
            var total = 0;
            angular.forEach($scope.filteredWorks, function (work) {
                total += work.amount;
            });
            return total;
        };


    }
])
;
