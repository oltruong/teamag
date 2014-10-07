'use strict';

teamagApp.controller('AbsenceController', ['$scope', '$http', 'userInfo',
    function ($scope, $http, userInfo) {

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
                return '';
            }


        };


        $scope.today = function () {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.dt = null;
        };

        // Disable weekend selection
        $scope.disabled = function (date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.toggleMin = function () {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];
    }
])
;
