'use strict';

teamagApp.controller('AbsenceController', ['$scope', '$http', '$filter', 'userInfo',
    function ($scope, $http, $filter, userInfo) {

        $scope.today = function () {
            $scope.dt = new Date();
            $scope.minDate = new Date();
        };
        $scope.today();

        $scope.getAbsences = function () {
            $http.get('resources/absences/').success(function (data) {
                $scope.absences = data;
            }, function (error) {
                $scope.error = 'Erreur HTTP ' + error.status;
            });
        };
        $scope.getAbsences();


        $scope.delete = function (absenceId) {
            $http.delete('resources/absences/' + absenceId).success(function () {
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
        $scope.beginType = 0;
        $scope.endType = 0;
        $scope.endDate = new Date();


        $scope.addAbsence = function () {

            $scope.confirmation = '';
            $scope.error = '';

            var absence = {
                beginDateString: $filter('date')($scope.beginDate, 'yyyy-MM-dd'),
                beginType: $scope.beginType,
                endDateString: $filter('date')($scope.endDate, 'yyyy-MM-dd'),
                endType: $scope.endType
            };

            $http.post('resources/absences/', absence).success(function (data, status) {
                $scope.confirmation = "Absence ajoutée";
                $scope.getAbsences();

            }).error(function (data, status) {
                if (status === 400) {
                    $scope.error = 'Ajout impossible, merci de vérifier la cohérence des dates';
                } else if (status === 403) {
                    $scope.error = "Ajout impossible, chevauchement avec d'autres absences";
                } else {
                    $scope.error = 'Erreur technique';
                }
            });

        };


    }
])
;
