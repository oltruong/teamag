'use strict';

teamagApp.controller('RealizedController', ['$scope', '$http', 'Task', 'Member', 'WorkRealized',
    function ($scope, $http, Task, Member, WorkRealized) {


        $scope.members = Member.query(function () {
            $scope.selectedMember = $scope.members[0];
            $scope.loadRealized();
        }, function (error) {
            $scope.error = 'Erreur HTTP ' + error.status;
        });

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


        $scope.orderProp = 'month';

        $scope.loadRealized = function () {
            $http.get('../resources/workrealized/' + $scope.selectedMember.id).success(function (data) {
                $scope.worksRealized = data;
            }, function (error) {
                $scope.error = error;

            });
        }

        $scope.save = function () {
            $http.put('../resources/workrealized/', $scope.worksRealized).success(function (data) {
                $scope.confirmation = 'Mise à jour effectuée';
                $scope.error = '';
            }, function (error) {

            }).error(function (data, status, headers, config) {
                $scope.confirmation = '';
                $scope.error = 'Erreur HTTP' + status;
            });
        }


    }
]);
