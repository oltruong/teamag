'use strict';

teamagApp.controller('CheckWorkController', ['$scope', '$http', 'userInfo', 'Member', 'Work', 'WeekComment',
    function ($scope, $http, userInfo, Member, Work, WeekComment) {
        $scope.months = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];
        $scope.daysOfWeek = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];


        $scope.selectedTab = "week";
        var today = new Date();
        $scope.year = today.getFullYear();

        $scope.weekNumber = getWeekNumber(today);

        $scope.month = today.getMonth() + 1;

        $scope.ordermonth = "name";
        $scope.tasks = [];

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
            $scope.minWeek = 53;
            $scope.maxWeek = 0;
            Work.query({
                month: $scope.month,
                year: $scope.year,
                notnull: true,
                memberId: $scope.selectedMember.id
            }).$promise.then(function (works) {
                    $scope.works = works;
                    $scope.days = new Array();

                    if ($scope.works.length === 0) {
                        $scope.minWeek = getMinWeek($scope.month);
                        $scope.maxWeek = getMaxWeek($scope.month);
                    }

                    for (var i = 0; i < $scope.works.length; i++) {
                        if ($scope.days.indexOf($scope.works[i].daylong) === -1) {
                            $scope.days.push($scope.works[i].daylong);
                            var weekDay = getWeekNumber(new Date($scope.works[i].daylong));
                            if ($scope.minWeek > weekDay) {
                                $scope.minWeek = weekDay;
                            }
                            if ($scope.maxWeek < weekDay) {
                                $scope.maxWeek = weekDay;
                            }
                        }
                    }

                    if ($scope.weekNumber > $scope.maxWeek) {
                        $scope.weekNumber = $scope.maxWeek;
                    } else if ($scope.weekNumber < $scope.minWeek) {
                        $scope.weekNumber = $scope.minWeek;
                    }

                    initWeekDays();

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

        function getTotal(task, works) {
            var total = 0;
            for (var i = 0; i < works.length; i++) {
                if (getTaskDescription(task) === getTaskDescription(works[i].taskBean)) {
                    total += works[i].amount;
                }
            }
            return total;
        }

        function initWeekDays() {
            $scope.weekdays = [];
            for (var d = 0; d < $scope.days.length; d++) {
                if (getWeekNumber(new Date($scope.days[d])) === $scope.weekNumber) {
                    $scope.weekdays.push(Number($scope.days[d]));
                }
            }

            $scope.weekWorks = [];
            for (var i = 0; i < $scope.works.length; i++) {
                if (getWeekNumber(new Date($scope.works[i].daylong)) === $scope.weekNumber) {
                    $scope.weekWorks.push($scope.works[i]);
                }
            }

            $scope.weektasks = [];
            var indexweetask = '';
            for (var i = 0; i < $scope.weekWorks.length; i++) {
                if (indexweetask.indexOf(getTaskDescription($scope.weekWorks[i].taskBean)) === -1) {

                    $scope.weektasks.push({
                        name: getTaskDescription($scope.weekWorks[i].taskBean),
                        total: getTotal($scope.weekWorks[i].taskBean, $scope.weekWorks)
                    });
                    indexweetask += ";" + getTaskDescription($scope.weekWorks[i].taskBean);
                }
            }

        }

        function initWeekComment() {
            WeekComment.get({
                memberId: $scope.selectedMember.id,
                weekNumber: $scope.weekNumber,
                month: $scope.month,
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
            if ($scope.weekNumber <= $scope.minWeek) {
                $scope.month--;
                findWorks();

            } else {
                $scope.weekNumber--;
                initWeekDays();
                initWeekComment();

            }
        };

        $scope.increaseWeek = function () {
            if ($scope.weekNumber >= $scope.maxWeek) {
                $scope.month++;
                findWorks();
            } else {
                $scope.weekNumber++;
                initWeekDays();
                initWeekComment();
            }
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

        $scope.showOrderClass = function (value) {

            if (value === $scope.ordermonth) {
                return "glyphicon glyphicon-chevron-up";
            } else if ("-" + value === $scope.ordermonth) {
                return "glyphicon glyphicon-chevron-down";
            } else {
                return "hidden";
            }
        };


        $scope.totalTask = function () {
            var total = 0;
            for (var i = 0; i < $scope.tasks.length; i++) {
                total += $scope.tasks[i].total;

            }
            return total;
        };

        function getWeekNumber(d) {
            d = new Date(+d);
            d.setHours(0, 0, 0);
            d.setDate(d.getDate() + 4 - (d.getDay() || 7));
            var yearStart = new Date(d.getFullYear(), 0, 1);
            return Math.ceil(( ( (d - yearStart) / 86400000) + 1) / 7)
        }

        function getMinWeek(month) {
            var d = new Date($scope.year, month - 1, 1, 0, 0, 0, 0);
            return getWeekNumber(d.getTime());
        }

        function getMaxWeek(month) {
            var d = new Date($scope.year, month, 1, 0, 0, 0, 0);
            return getWeekNumber(d.getTime() - 1000 * 3600 * 24);
        }

    }]);

