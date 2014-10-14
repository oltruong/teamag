'use strict';

teamagApp.controller('HomeController', ['$scope', 'userInfo',
    function HomeController($scope, userInfo) {

        $scope.eventSources = [];

        /* config object */
        $scope.uiConfig = {
            calendar: {
                height: 450,
                editable: true,
                header: {
                    left: 'month basicWeek basicDay agendaWeek agendaDay',
                    center: 'title',
                    right: 'today prev,next'
                },
                dayClick: $scope.alertEventOnClick,
                eventDrop: $scope.alertOnDrop,
                eventResize: $scope.alertOnResize
            }
        };


        $scope.loggedIn = userInfo.loggedIn;

        $scope.name = userInfo.name;

        $scope.$watch(function () {
            return  userInfo;
        }, function (userInfoChanged) {
            $scope.loggedIn = userInfoChanged.loggedIn;
            $scope.name = userInfoChanged.name;
        }, true);


    }]);
