'use strict';

teamagApp.controller('CheckWorkController', ['$scope', '$http', 'userInfo', 'Member', 'Work', 'WorkByTask', 'WeekComment',
    function ($scope, $http, userInfo, Member, Work, WorkByTask, WeekComment) {
        $scope.months = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];
        $scope.daysOfWeek = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

        $scope.minWeek = 53;
        $scope.maxWeek = 0;

        $scope.selectedTab = "week";

        generateByWeekValues(new Date());
        generateWeekBorder();

        generateByMonthValues(new Date());

        $scope.ordermonth = "name";
        $scope.ordertotal = "name";

        $scope.taskMonths = [];
        $scope.taskTotal = [];
        $scope.tasks = [];
        $scope.works = [];

        $scope.admin = userInfo.admin;
        if ($scope.admin) {
            $scope.members = Member.query(function () {
                $scope.selectedMember = $scope.members[0];
                findWorks();
            }, function (error) {
                $scope.error = 'Erreur HTTP ' + error.status;
            });

        } else {
            $scope.selectedMember = {id: userInfo.id};
            findWorks();
        }


        function generateByWeekValues(date) {
            $scope.year = date.getFullYear();
            $scope.weekNumber = getWeekNumber(date);
            $scope.byWeekMonth = date.getMonth() + 1;
        }

        function generateByMonthValues(date) {
            $scope.byMonthMonth = date.getMonth() + 1;
        }

        function generateWeekBorder() {

            var firstDayOfMonth = new Date($scope.year, $scope.byWeekMonth - 1, 1);

            while (firstDayOfMonth.getDay() === 0 || firstDayOfMonth.getDay() === 6) {
                firstDayOfMonth = new Date(firstDayOfMonth.getTime() + 86400000);
            }

            $scope.firstWeekOfMonth = getWeekNumber(firstDayOfMonth);

            var lastDayOfMonth = new Date($scope.year, $scope.byWeekMonth, 0);

            while (lastDayOfMonth.getDay() === 0 || lastDayOfMonth.getDay() === 6) {
                lastDayOfMonth = new Date(lastDayOfMonth.getTime() - 86400000);
            }
            $scope.lastWeekOfMonth = getWeekNumber(lastDayOfMonth);
        };

        $scope.updateMember = function () {
            findWorks();
        };


        $scope.findAmount = function ($task, $day) {
            for (var i = 0; i < $scope.works.length; i++) {
                if (getTaskDescription($scope.works[i].taskBean) === $task && $scope.works[i].daylong === $day) {
                    if ($scope.works[i].amount === 0) {
                        return '';
                    } else {
                        return $scope.works[i].amount;
                    }
                }
            }
            return '';

        };

        function findWorks() {
            findWorksWeek();
            findWorksMonth();
            findWorksTotal();
        };

        function findWorksWeek() {
            Work.query({
                notnull: true,
                week: $scope.weekNumber,
                month: $scope.byWeekMonth,
                year: $scope.year,
                memberId: $scope.selectedMember.id
            }).$promise.then(function (works) {
                    $scope.works = works;
                    $scope.days = new Array();
                    for (var i = 0; i < $scope.works.length; i++) {
                        if ($scope.days.indexOf($scope.works[i].daylong) === -1) {
                            $scope.days.push($scope.works[i].daylong);
                        }
                    }
                    $scope.tasks = [];
                    var indextask = '';
                    for (var i = 0; i < $scope.works.length; i++) {
                        if (indextask.indexOf(getTaskDescription($scope.works[i].taskBean)) === -1) {
                            $scope.tasks.push({
                                name: getTaskDescription($scope.works[i].taskBean),
                                total: getTotal($scope.works[i].taskBean, $scope.works)
                            });
                            indextask += ";" + getTaskDescription($scope.works[i].taskBean);
                        }
                    }
                    initWeekComment();
                }, function (error) {
                    $scope.error = 'Erreur HTTP ' + error.status;
                });

        };


        function findWorksMonth() {
            WorkByTask.query({
                month: $scope.byMonthMonth,
                year: $scope.year,
                memberId: $scope.selectedMember.id
            }).$promise.then(function (tasks) {
                    $scope.taskMonths = tasks;
                }, function (error) {
                    $scope.error = 'Erreur HTTP ' + error.status;
                }
            )
            ;

        };

        function findWorksTotal() {
            WorkByTask.query({
                memberId: $scope.selectedMember.id
            }).$promise.then(function (tasks) {
                    $scope.taskTotal = tasks;
                }, function (error) {
                    $scope.error = 'Erreur HTTP ' + error.status;
                }
            )
            ;

        };

        function getTotal(task, works) {
            var total = 0;
            for (var i = 0; i < works.length; i++) {
                if (getTaskDescription(task) === getTaskDescription(works[i].taskBean)) {
                    total += works[i].amount;
                }
            }
            return total.toFixed(1);
        }


        function initWeekComment() {
            WeekComment.get({
                memberId: $scope.selectedMember.id,
                weekNumber: $scope.weekNumber,
                month: $scope.byWeekMonth,
                year: $scope.year
            }).$promise.then(function (data) {
                    $scope.weekcomment = data;
                });
        }

        $scope.displayString = function ($day) {
            var day = new Date($day);
            return $scope.daysOfWeek[day.getDay() - 1] + " " + day.getDate();
        };

        $scope.decreaseWeek = function () {

            if ($scope.weekNumber === $scope.firstWeekOfMonth) {
                var previousMonth = new Date($scope.year, $scope.byWeekMonth - 1, 0);

                while (previousMonth.getDay() == 0 || previousMonth.getDay() == 6) {
                    previousMonth = new Date(previousMonth.getTime() - 86400000);
                }
                generateByWeekValues(previousMonth);
                generateWeekBorder();
            }
            else {
                $scope.weekNumber--;
            }

            findWorksWeek();
        };

        $scope.increaseWeek = function () {
            if ($scope.weekNumber === $scope.lastWeekOfMonth) {
                var nextMonth = new Date($scope.year, $scope.byWeekMonth, 1);
                while (nextMonth.getDay() === 0 || nextMonth.getDay() === 6) {
                    nextMonth = new Date(nextMonth.getTime() + 86400000);
                }
                generateByWeekValues(nextMonth);
                generateWeekBorder();
            } else {
                $scope.weekNumber++;
            }
            findWorksWeek();
        };


        $scope.decreaseMonth = function () {
            $scope.byMonthMonth--;
            findWorksMonth();
        };


        $scope.increaseMonth = function () {
            $scope.byMonthMonth++;
            findWorksMonth();
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

        $scope.setOrderMonth = function (value) {
            if (value === $scope.ordermonth) {
                $scope.ordermonth = "-" + value;
            } else {
                $scope.ordermonth = value;
            }
        };

        $scope.setOrderTotal = function (value) {
            if (value === $scope.ordertotal) {
                $scope.ordertotal = "-" + value;
            } else {
                $scope.ordertotal = value;
            }
        };


        $scope.showOrderMonthClass = function (value) {

            if (value === $scope.ordermonth) {
                return "glyphicon glyphicon-chevron-up";
            } else if ("-" + value === $scope.ordermonth) {
                return "glyphicon glyphicon-chevron-down";
            } else {
                return "hidden";
            }
        };

        $scope.showOrderTotalClass = function (value) {

            if (value === $scope.ordertotal) {
                return "glyphicon glyphicon-chevron-up";
            } else if ("-" + value === $scope.ordertotal) {
                return "glyphicon glyphicon-chevron-down";
            } else {
                return "hidden";
            }
        };


        $scope.totalTask = function (tasks) {
            var total = 0;
            for (var i = 0; i < tasks.length; i++) {
                total += tasks[i].total;

            }
            return total;
        };

        function getWeekNumber(d) {
            d = new Date(+d);
            d.setHours(0, 0, 0);
            d.setDate(d.getDate() + 4 - (d.getDay() || 7));
            var yearStart = new Date(d.getFullYear(), 0, 1);
            return Math.ceil(( ( (d - yearStart) / 86400000) + 1) / 7);
        }


    }

])
;

