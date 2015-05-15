'use strict';

teamagApp.controller('WorkRealizedController', ['$scope', 'Work', '$http',
    function ($scope, Work, $http) {

        $scope.months = ["Janv", "Fev", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Dec"];
        $scope.daysOfWeek = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

        var today = new Date();
        $scope.month = today.getMonth();
        $scope.year = today.getFullYear();


        $scope.works = Work.query({month: $scope.month + 1, year: $scope.year}).$promise.then(function (works) {
            $scope.works = works;
            buildDays();
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
                    $scope.days.push(work.daylong);
                }

                $scope.worktasks[work.taskBean.id + work.daylong] = work;
            }

        }

        $scope.displayString = function ($day) {
            var day = new Date($day);
            return $scope.daysOfWeek[day.getDay() - 1] + " " + day.getDate();
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
            for (var i = 0; i < $scope.works.length; i++) {
                var work = $scope.works[i];
                if (work.amount !== work.original) {
                    updatedWorks.push({id: work.id, total: work.amount});
                }
            }

            $http.patch('../resources/works', updatedWorks).success(function (data) {
                console.log('ok mis a jour');
            }).error(function (data, status, headers, config) {
                console.log('ERREUr');
            });
        };


    }])
;