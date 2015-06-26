'use strict';

teamagApp.controller('WorkRealizedController', ['$scope', 'Work', 'WeekComment', '$http',
    function ($scope, Work, WeekComment, $http) {

        $scope.months = ["Janv", "Fev", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Dec"];
        $scope.daysOfWeek = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

        var today = new Date();
        $scope.month = today.getMonth() + 1;
        $scope.year = today.getFullYear();
        $scope.minWeek = 53;
        $scope.maxWeek = 0;
        $scope.beginWeek = 53;
        $scope.weekdays = [];
        $scope.days = [];
        $scope.worktasks = [];


        $scope.newTask = {};

        Work.query({month: $scope.month, year: $scope.year}).$promise.then(function (works) {
            $scope.works = works;
            initData();
            loadWeekComment();
        }, function (error) {
            console.log('ERROR retrieving works');
            console.log(error);
        });

        function initData() {
            $scope.tasks = [];
            var taskIndex = "";

            for (var i = 0; i < $scope.works.length; i++) {
                var work = $scope.works[i];
                work.original = work.amount;
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

            if ($scope.beginWeek > $scope.maxWeek) {
                $scope.beginWeek = $scope.maxWeek;
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
            loadWeekComment();

        };

        $scope.decreaseWeek = function () {
            $scope.beginWeek--;
            initWeekDays();
            loadWeekComment();

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

            if (total == 0) {
                return "";
            } else {
                return total.toFixed(2);
            }

        };

        $scope.removetask = function ($task) {
            $http.delete('../resources/tasks/' + $task.id + '?month=' + $scope.month + '&year=' + $scope.year, $scope.newTask).success(function (data, status, headers, config) {
            }).error(function (data, status, headers, config) {
                console.log('ERROR deleting task' + status);
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
            return (Math.round(total * 100) / 100);

        };

        $scope.displayTotalStr = function ($day) {
            var total = $scope.displayTotal($day);
            if (total == 0) {
                return "";
            } else {
                return total.toFixed(2);
            }
        };


        $scope.displayTotalWeekStr = function () {
            var total = 0;
            for (var i = 0; i < $scope.weekdays.length; i++) {
                total += $scope.displayTotal($scope.weekdays[i]);
            }
            if (total === 0) {
                return "";
            } else {
                return total.toFixed(2);
            }

        };

        $scope.displayClass = function ($day) {

            var total = $scope.displayTotal($day);

            if (total === 1.0) {
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

            updateWorks();
            updateWeekComment();
        };


        function updateWorks() {
            var updatedWorks = [];
            var updatedWorkList = [];
            for (var i = 0; i < $scope.works.length; i++) {
                var work = $scope.works[i];
                if (work.amount === null) {
                    work.amount = 0;
                }

                if (work.amount !== work.original) {
                    updatedWorks.push({id: work.id, total: work.amount});
                    updatedWorkList.push(work);
                }
            }
            if (updatedWorkList.length > 0) {
                $http.patch('../resources/works', updatedWorks).success(function (data) {
                }).error(function (data, status, headers, config) {
                    console.log('ERROR patch work');
                });
                for (var i = 0; i < updatedWorkList.length; i++) {
                    updatedWorkList[i].original = updatedWorkList[i].amount;
                }
            }
        }

        function updateWeekComment() {

            if ($scope.weekcomment.comment !== $scope.weekcomment.original) {
                if ($scope.weekcomment.comment === '') {
                    WeekComment.delete({id: $scope.weekcomment.id});
                    $scope.weekcomment = {comment: '', original: ''};
                } else if ($scope.weekcomment.original === '') {
                    $scope.weekcomment.month = $scope.month;
                    $scope.weekcomment.weekYear = $scope.beginWeek;
                    $scope.weekcomment.year = $scope.year;
                    WeekComment.save($scope.weekcomment, function (data, headers) {
                        var weekCommentId = headers('Location').substring(headers('Location').lastIndexOf('/') + 1);
                        $scope.weekcomment.id = weekCommentId;
                    });


                } else {
                    WeekComment.patch({id: $scope.weekcomment.id}, $scope.weekcomment);
                }
                $scope.weekcomment.original = $scope.weekcomment.comment;
            }
        }

        $scope.addTask = function () {
            $http.post('../resources/tasks?month=' + $scope.month + '&year=' + $scope.year, $scope.newTask).success(function (data, status, headers, config) {

                var taskId = headers('Location').substring(headers('Location').lastIndexOf('/') + 1);

                Work.query({month: $scope.month, year: $scope.year, taskId: taskId}).$promise.then(function (newWorks) {
                    for (var index = 0; index < newWorks.length; index++) {
                        $scope.works.push(newWorks[index]);
                        $scope.worktasks[newWorks[index].taskBean.id + newWorks[index].daylong] = newWorks[index];
                    }
                    $scope.tasks.push(newWorks[0].taskBean);
                });


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

        function loadWeekComment() {
            WeekComment.get({
                weekNumber: $scope.beginWeek,
                month: $scope.month,
                year: $scope.year
            }).$promise.then(function (data) {
                    $scope.weekcomment = data;
                    if ($scope.weekcomment.comment) {
                        $scope.weekcomment.original = $scope.weekcomment.comment;
                    } else {
                        $scope.weekcomment = {comment: '', original: ''};
                    }
                });
        }


    }])
;