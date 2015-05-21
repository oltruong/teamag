'use strict';

teamagApp.controller('WorkRealizedController', ['$scope', 'Work', '$http',
    function ($scope, Work, $http) {

        $scope.months = ["Janv", "Fev", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Dec"];
        $scope.daysOfWeek = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

        var today = new Date();
        $scope.month = today.getMonth() + 1;
        $scope.year = today.getFullYear();
        $scope.minWeek = 52;
        $scope.maxWeek = 0;
        $scope.beginWeek = 52;
        $scope.weekdays = [];
        $scope.days = [];
        $scope.worktasks = [];


        $scope.newTask = {};

        Work.query({month: $scope.month, year: $scope.year}).$promise.then(function (works) {
            $scope.works = works;
            initData();
        });


        function initData() {
            $scope.tasks = [];
            var taskIndex = "";

            for (var i = 0; i < $scope.works.length; i++) {
                var work = $scope.works[i];
                if (taskIndex.indexOf(work.taskBean.id) === -1) {
                    $scope.tasks.push(work.taskBean);
                    taskIndex = taskIndex + ";" + work.taskBean.id + ";";
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


            for (var d = 0; d < $scope.days.length; d++) {
                if ($scope.displayTotal($scope.days[d]) !== 1) {
                    var week = getWeekNumber(new Date($scope.days[d]));
                    if ($scope.beginWeek > week) {
                        $scope.beginWeek = week;
                    }
                }
            }
            initWeekDays();


        }

        function initWeekDays() {
            $scope.weekdays = [];
            for (var d = 0; d < $scope.days.length; d++) {
                if (getWeekNumber(new Date($scope.days[d])) === $scope.beginWeek) {
                    $scope.weekdays.push($scope.days[d]);
                }
            }
        }

        $scope.increaseWeek = function () {
            $scope.beginWeek++;
            initWeekDays();
        };

        $scope.decreaseWeek = function () {
            $scope.beginWeek--;
            initWeekDays();
        };

        $scope.displayString = function ($day) {
            var day = new Date($day);
            return $scope.daysOfWeek[day.getDay() - 1] + " " + day.getDate();
        };


        $scope.sum = function ($task) {
            var total = 0;
            for (var i = 0; i < $scope.weekdays.length; i++) {
                total += $scope.worktasks [$task.id + $scope.weekdays[i]].amount;
            }
            return total;

        };

        $scope.removetask = function ($task) {
            console.log("removing task" + $task.id);

            $http.delete('../resources/tasks/' + $task.id + '?month=' + $scope.month + '&year=' + $scope.year, $scope.newTask).success(function (data, status, headers, config) {
                console.log("Response " + status);
            }).error(function (data, status, headers, config) {
                console.log('ERROR ' + status);
            });


            for (var i = 0; i < $scope.tasks.length; i++) {
                if ($scope.tasks[i].id === $task.id) {
                    $scope.tasks.splice(i, 1);
                    return;
                }
            }
        };

        $scope.sumTotal = function ($task) {


            var total = 0;
            for (var i = 0; i < $scope.days.length; i++) {
                total += $scope.worktasks [$task.id + $scope.days[i]].original;
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

        $scope.addTask = function () {
            $http.post('../resources/tasks?month=' + $scope.month + '&year=' + $scope.year, $scope.newTask).success(function (data, status, headers, config) {
                console.log("Response " + status);
                var taskId = headers('Location').substring(headers('Location').lastIndexOf('/') + 1);
                console.log("taskId " + taskId);
                Work.query({month: $scope.month, year: $scope.year, taskId: taskId}).$promise.then(function (newWorks) {
                    for (var index = 0; index < newWorks.length; index++) {
                        $scope.works.push(newWorks[index]);
                        $scope.worktasks[newWorks[index].taskBean.id + newWorks[index].daylong] = newWorks[index];
                    }
                    $scope.tasks.push(newWorks[0].taskBean);
                });
                console.log("yay");

            }).error(function (data, status, headers, config) {
                console.log('ERROR ' + status);
                console.log(status);
            });
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