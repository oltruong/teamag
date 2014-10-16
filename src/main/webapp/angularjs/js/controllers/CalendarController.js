'use strict';

teamagApp.controller('CalendarController', ['$scope', '$http',
    function ($scope, $http) {
        $scope.uiConfig = {
            calendar: {
                weekNumbers: true,
                firstDay: 1
            }
        };
        $scope.events = [];
        $scope.daysoff = [];


        $scope.eventSources = [$scope.events, $scope.daysoff];


        $http.get('../resources/absences/daysoff').success(function (data) {
            angular.forEach(data, function (absence) {

                $scope.daysoff[$scope.daysoff.length] = {
                    color: '#21610B',
                    textColor: 'white',
                    title: 'Férié',
                    start: new Date(absence.beginDateLong),
                    end: new Date(absence.endDateLong)
                };
            });
        });

        $scope.getAllAbsences = function () {
            $http.get('../resources/absences/').success(function (data) {
                angular.forEach(data, function (absence) {
                    if (absence.beginDateLong !== absence.endDateLong) {
                        var beginDateLongR = absence.beginDateLong;
                        var endDateLongR = absence.endDateLong;


                        if (absence.beginType !== 0) {
                            $scope.events[$scope.events.length] = {
                                color: '#' + absence.color,
                                textColor: 'white',
                                title: absence.memberName + $scope.decode(absence.beginType),
                                start: new Date(absence.beginDateLong),
                                end: new Date(absence.beginDateLong)
                            };
                            beginDateLongR += 86400000;
                        }

                        if (absence.endType !== 0) {
                            $scope.events[$scope.events.length] = {
                                color: '#' + absence.color,
                                textColor: 'white',
                                title: absence.memberName + $scope.decode(absence.endType),
                                start: new Date(absence.endDateLong),
                                end: new Date(absence.endDateLong)
                            };
                            endDateLongR -= 86400000;
                        }


                        $scope.events[$scope.events.length] = {
                            color: '#' + absence.color,
                            textColor: 'white',
                            title: absence.memberName,
                            start: new Date(beginDateLongR),
                            end: new Date(endDateLongR)
                        };


                    } else {
                        $scope.events[$scope.events.length] = {
                            color: '#' + absence.color,
                            textColor: 'white',
                            title: absence.memberName + $scope.decode(absence.beginType),
                            start: new Date(absence.beginDateLong),
                            end: new Date(absence.endDateLong)
                        };
                    }


                });

            }, function (error) {
                $scope.error = 'Erreur HTTP ' + error.status;
            });
        };

        $scope.decode = function (type) {
            if (type === 1) {
                return ' - matin';
            } else if (type === 2) {
                return ' - après-midi';
            } else {
                return '';
            }


        };
        $scope.getAllAbsences();
    }]);
