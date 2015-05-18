'use strict';

teamagApp.controller('WorkRealizedController', ['$scope', 'Work', '$http',
    function ($scope, Work, $http) {

        $scope.months = ["Janv", "Fev", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Dec"];
        $scope.daysOfWeek = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

        var today = new Date();
        $scope.month = today.getMonth();
        $scope.year = today.getFullYear();
        $scope.minWeek = 52;
        $scope.maxWeek = 0;

        $scope.works = Work.query({month: $scope.month + 1, year: $scope.year}).$promise.then(function (works) {
            $scope.works = works;
            buildDays();
        });

        $http.get('../resources/task/nonadmin').success(function (data) {
            $scope.tasks = data;
        });


        function buildDays() {
            $scope.days = [];
            $scope.tasks = [];
            $scope.worktasks = [];

            var taskId = -1;

            for (var i = 0; i < $scope.works.length; i++) {
                var work = $scope.works[i];
                if (work.taskBean.id !== taskId) {
                    $scope.tasks.push(work.taskBean);
                    taskId = work.taskBean.id;
                }

                if ($scope.days.indexOf(work.daylong) === -1) {

                    var weekDay = getWeekNumber(new Date(work.daylong));
                    if ($scope.minWeek > weekDay) {
                        $scope.minWeek = weekDay;
                    }
                    if ($scope.maxWeek < weekDay) {
                        $scope.maxWeek = weekDay;
                    }

                    $scope.days.push(work.daylong);
                }

                $scope.worktasks[work.taskBean.id + work.daylong] = work;
            }

            var beginWeek = 52;
            for (var d = 0; d < $scope.days.length; d++) {
                if ($scope.displayTotal($scope.days[d]) !== 1) {
                    var week = getWeekNumber(new Date($scope.days[d]));
                    if (beginWeek > week) {
                        beginWeek = week;
                    }
                }
            }
            console.log('premiere semaine ' + beginWeek);


        }

        $scope.displayString = function ($day) {
            var day = new Date($day);
            return $scope.daysOfWeek[day.getDay() - 1] + " " + day.getDate();
        };


        $scope.sum = function ($task) {
            var total = 0;
            for (var i = 0; i < $scope.works.length; i++) {
                if ($scope.works[i].taskBean.id === $task.id) {
                    total += $scope.works[i].amount;
                }
            }
            return total;

        };

        $scope.displayTotal = function ($day) {
            var total = 0;
            for (var i = 0; i < $scope.works.length; i++) {
                if ($scope.works[i].daylong === $day) {
                    total += $scope.works[i].amount;
                }
            }
            return total;

        };

        $scope.displayClass = function ($day) {

            var total = $scope.displayTotal($day);

            if (total === 1) {
                return "text-success";
            } else if (total > 1) {
                return "text-danger";
            } else {
                return "";
            }

        };

        $scope.displayIcon = function ($day) {

            var total = $scope.displayTotal($day);

            if (total === 1) {
                return "glyphicon glyphicon-thumbs-up";
            } else if (total > 1) {
                return "glyphicon glyphicon-remove";
            } else {
                return "hidden";
            }

        };

        $scope.update = function () {

            var updatedWorks = [];
            var updatedWorkList = [];
            for (var i = 0; i < $scope.works.length; i++) {
                var work = $scope.works[i];
                if (work.amount === null) {
                    work.amount = 0;
                }

                if (work.amount !== work.original) {

                    console.log('nouveau [' + work.amount + ']');
                    updatedWorks.push({id: work.id, total: work.amount});
                    updatedWorkList.push(work);
                }
            }
            if (updatedWorkList.length > 0) {
                $http.patch('../resources/works', updatedWorks).success(function (data) {
                    console.log('ok mis a jour');
                }).error(function (data, status, headers, config) {
                    console.log('ERROR');
                });
                for (var i = 0; i < updatedWorkList.length; i++) {
                    updatedWorkList[i].original = updatedWorkList[i].amount;
                }

            } else {
                console.log("no change");
            }
        };

        $scope.class = function ($value) {
            if ($value === 0) {
                return "white";
            } else {
                return "";
            }
        };

        $scope.resetWorks = function () {
            for (var i = 0; i < $scope.works.length; i++) {
                $scope.works[i].amount = $scope.works[i].original;
            }
        };

        function getWeekNumber(d) {
            d = new Date(+d);
            d.setHours(0, 0, 0);
            d.setDate(d.getDate() + 4 - (d.getDay() || 7));
            var yearStart = new Date(d.getFullYear(), 0, 1);
            return Math.ceil(( ( (d - yearStart) / 86400000) + 1) / 7)
        }


    }])
;