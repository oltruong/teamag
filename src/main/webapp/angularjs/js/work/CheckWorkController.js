'use strict';

teamagApp.controller('CheckWorkController', ['$scope', '$http', 'Member', 'Work', 'WeekComment',
    function ($scope, $http, Member, Work, WeekComment) {
        $scope.months = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];
        $scope.daysOfWeek = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

        var today = new Date();

        $scope.selectedTab = "week";

        $scope.weekNumber = getWeekNumber(today);
        $scope.month = today.getMonth() + 1;
        $scope.year = today.getFullYear();
        $scope.minWeek = 53;
        $scope.maxWeek = 0;


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
                notnull: true,
                memberId: $scope.selectedMember.id
            }).$promise.then(function (works) {
                    console.log("works");
                    console.log(works);
                    $scope.works = works;
                    $scope.days = new Array();
                    for (var i = 0; i < $scope.works.length; i++) {
                        if ($scope.days.indexOf($scope.works[i].daylong) === -1) {
                            $scope.days.push(Number($scope.works[i].daylong));
                            console.log("push days!!! [" + $scope.works[i].daylong + "] date [" + new Date($scope.works[i].daylong) + "]")
                            var weekDay = getWeekNumber(new Date($scope.works[i].dayLong));
                            if ($scope.minWeek > weekDay) {
                                $scope.minWeek = weekDay;
                            }
                            if ($scope.maxWeek < weekDay) {
                                $scope.maxWeek = weekDay;
                            }

                        }
                    }

                    initWeekDays();

                    $scope.tasks = [];
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

        function initWeekDays() {
            $scope.weekdays = [];


            for (var d = 0; d < $scope.days.length; d++) {

                console.log('Parcours ! ' + getWeekNumber(new Date($scope.days[d])) + " " + $scope.weekNumber);

                if (getWeekNumber(new Date($scope.days[d])) === $scope.weekNumber) {
                    console.log('add weekday ! ' + new Date($scope.days[d]));
                    $scope.weekdays.push(Number($scope.days[d]));
                }
            }

            $scope.weekWorks = [];
            for (var i = 0; i < $scope.works.length; i++) {
                if (getWeekNumber(new Date($scope.works[i].daylong)) === $scope.weekNumber) {

                    console.log('add weekWorks ! ' + getTaskDescription($scope.works[i].taskBean));
                    $scope.weekWorks.push($scope.works[i]);
                }
            }

            $scope.weektasks = [];
            for (var i = 0; i < $scope.weekWorks.length; i++) {
                if ($scope.weektasks.indexOf(getTaskDescription($scope.weekWorks[i].taskBean)) === -1) {
                    console.log('add weektasks ' + getTaskDescription($scope.weekWorks[i].taskBean));

                    $scope.weektasks.push(getTaskDescription($scope.weekWorks[i].taskBean));
                }
            }

        }

        $scope.displayString = function ($day) {
            var day = new Date($day);
            return $scope.daysOfWeek[day.getDay() - 1] + " " + day.getDate();
        };

        $scope.decreaseWeek = function () {
            $scope.weekNumber--;
            initWeekDays();
        };

        $scope.increaseWeek = function () {
            $scope.weekNumber++;
            initWeekDays();
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

