'use strict';

teamagApp.controller('WorkTaskController', ['$scope', '$http', '$location', '$routeParams', 'Work',
    function ($scope, $http, $location, $routeParams, Work) {

        $scope.taskId = $routeParams.taskId;


        $http.get('../resources/work/' + $scope.taskId).success(function (data) {

            $scope.works = data;
        });

//        $scope.works = Work.get({id: $scope.taskId}, function (data) {
//            $scope.works = data;
//            alert('coucoucoucou2');
//        }, function (error) {
//            alert(':(:(:(:(:(');
//            $scope.error = 'Erreur HTTP' + error.status;
//        });


//        Work.get({id: $scope.taskId}, function (data) {
//            alert('hello world!');
//        }, function (error) {
//            alert('goodbye world!');
//        });

        $scope.orderProp = 'member';


        $scope.total = function () {
            var total = 0;
            angular.forEach($scope.filteredWorks, function (work) {
                total += work.amount;
            })
            return total;
        };


    }
])
;
