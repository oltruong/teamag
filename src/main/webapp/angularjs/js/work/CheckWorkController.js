'use strict';

teamagApp.controller('CheckWorkController', ['$scope', '$http', 'Member', 'Work', 'WeekComment',
    function ($scope, $http, Member, Work, WeekComment) {
        $scope.months = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];

        var today = new Date();

        $scope.selectedTab = "week";

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
                if (getTaskDescription($scope.works[i].taskBean) === $task && $scope.works[i].day === $day) {
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
            Work.query({
                month: $scope.month,
                year: $scope.year,
                week: $scope.weekNumber,
                notnull: true,
                memberId: $scope.selectedMember.id
            }).$promise.then(function (works) {

                    $scope.works = works;
                    $scope.days = new Array();
                    for (var i = 0; i < $scope.works.length; i++) {
                        if ($scope.days.indexOf($scope.works[i].day) === -1) {
                            $scope.days.push($scope.works[i].day);
                        }
                    }

                    $scope.tasks = new Array();
                    for (var i = 0; i < $scope.works.length; i++) {
                        if ($scope.tasks.indexOf(getTaskDescription($scope.works[i].taskBean)) === -1) {
                            $scope.tasks.push(getTaskDescription($scope.works[i].taskBean));
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


        $scope.setTab = function (value) {
            $scope.selectedTab = value;
        };

        $scope.isSelectedTab = function (value) {
            return $scope.selectedTab === value;
        };

        $scope.showclass = function (value) {
            if ($scope.isSelectedTab(value)) {
                return "active";
            } else {
                return "";
            }
        };

        $scope.showtabclass = function (value) {
            if ($scope.isSelectedTab(value)) {
                return "tab-pane fade active";
            } else {
                return "tab-pane fade";
            }
        };

        function getTaskDescription(taskBean) {
            return taskBean.project + "-" + taskBean.name;
        }

        function getWeekNumber(d) {
            d = new Date(+d);
            d.setHours(0, 0, 0);
            d.setDate(d.getDate() + 4 - (d.getDay() || 7));
            var yearStart = new Date(d.getFullYear(), 0, 1);
            return Math.ceil(( ( (d - yearStart) / 86400000) + 1) / 7)
        }

    }]);

