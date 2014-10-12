'use strict';

teamagApp.controller('AbsenceController', ['$scope', '$http', '$filter', 'userInfo',
    function ($scope, $http, $filter, userInfo) {

        $scope.today = function () {
            $scope.dt = new Date();
            $scope.minDate = new Date();
        };
        $scope.today();

        $scope.getAbsences = function () {
            $http.get('../resources/absences/' + userInfo.id).success(function (data) {
                $scope.absences = data;
            }, function (error) {
                $scope.error = 'Erreur HTTP ' + error.status;
            })
        };
        $scope.getAbsences();


        $scope.delete = function (absenceId) {
            $http.delete('../resources/absences/' + userInfo.id + "/" + absenceId).success(function () {
                $scope.getAbsences();
            }, function (error) {
                $scope.error = 'Erreur HTTP ' + error.status;
            });
        };

        $scope.displayAbsence = function (type) {
            return type !== 0;
        };

        $scope.decode = function (type) {
            if (type === 1) {
                return 'matin';
            } else if (type === 2) {
                return 'après-midi';
            } else {
                return type;
            }


        };

        $scope.beginDate = new Date();
        $scope.beginType = 3;
        $scope.endType = 3;
        $scope.endDate = new Date();


        $scope.addAbsence = function () {

            $scope.confirmation = '';
            $scope.error = '';

            var absence = new Object();

            absence.beginDateString = $filter('date')($scope.beginDate, 'yyyy-MM-dd');
            absence.beginType = $scope.beginType;
            absence.endDateString = $filter('date')($scope.endDate, 'yyyy-MM-dd');
            absence.endType = $scope.endType;

            $http.post('../resources/absences/' + userInfo.id, absence).success(function (data) {

                $scope.confirmation = "Absence ajoutée";
                $scope.getAbsences();

            }, function (error) {
                $scope.error = 'Erreur HTTP ' + error.status;
            })

        }


    }
])
;
