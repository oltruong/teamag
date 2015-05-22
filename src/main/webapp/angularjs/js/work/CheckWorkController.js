'use strict';

teamagApp.controller('CheckWorkController', ['$scope', '$http', 'Member', 'CheckWork', 'WeekComment',
    function ($scope, $http, Member, CheckWork, WeekComment) {

        var today = new Date();

        $scope.weekNumber = getWeekNumber(today);
        $scope.month = today.getMonth() + 1;
        $scope.year = today.getFullYear();

        $scope.macroTask = false;
        $scope.members = Member.query(function () {
            $scope.selectedMember = $scope.members[0];
            $scope.findWorks();
        }, function (error) {
            $scope.error = 'Erreur HTTP ' + error.status;
        });

        $scope.findAmount = function ($task, $day) {
            for (var i = 0; i < $scope.works.length; i++) {
                if ($scope.works[i].task === $task && $scope.works[i].day === $day) {
                    if ($scope.works[i].amount === 0) {
                        return '';
                    } else {
                        return $scope.works[i].amount;
                    }
                }
            }
            return '';

        };

        $scope.findWorks = function () {
            CheckWork.query({
                memberId: $scope.selectedMember.id,
                weekNumber: $scope.weekNumber,
                macroTask: $scope.macroTask
            }, function (data) {

                $scope.works = data;
                $scope.days = new Array();
                for (var i = 0; i < $scope.works.length; i++) {
                    if ($scope.days.indexOf($scope.works[i].day) === -1) {
                        $scope.days.push($scope.works[i].day);
                    }
                }

                $scope.tasks = new Array();
                for (var i = 0; i < $scope.works.length; i++) {
                    if ($scope.tasks.indexOf($scope.works[i].task) === -1) {
                        $scope.tasks.push($scope.works[i].task);
                    }
                }


                WeekComment.get({
                    memberId: $scope.selectedMember.id,
                    weekNumber: $scope.weekNumber,
                    month: $scope.month,
                    year: $scope.year
                }).$promise.then(function (data) {
                        $scope.weekcomment = data;

                    });

            }, function (error) {
                $scope.error = 'Erreur HTTP ' + error.status;
            });

        };

        $scope.decreaseWeek = function () {
            $scope.weekNumber--;
            $scope.findWorks();
        };

        $scope.increaseWeek = function () {
            $scope.weekNumber++;
            $scope.findWorks();
        };

        function getWeekNumber(d) {
            d = new Date(+d);
            d.setHours(0, 0, 0);
            d.setDate(d.getDate() + 4 - (d.getDay() || 7));
            var yearStart = new Date(d.getFullYear(), 0, 1);
            return Math.ceil(( ( (d - yearStart) / 86400000) + 1) / 7)
        }

    }]);

