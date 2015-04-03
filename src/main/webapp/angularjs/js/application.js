'use strict';

var teamagApp = angular.module('teamagApp', ['ngRoute', 'ngResource', 'ngCookies', 'ui.calendar', 'colorpicker.module']);

teamagApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/home', {templateUrl: 'partials/views/home.html', controller: 'HomeController'});
    $routeProvider.when('/absences', {templateUrl: 'partials/views/absences.html', controller: 'AbsenceController'});
    $routeProvider.when('/absencescalendar', {
        templateUrl: 'partials/views/absencescalendar.html',
        controller: 'CalendarController'
    });
    $routeProvider.when('/login', {templateUrl: 'partials/views/login.html', controller: 'LoginController'});
    $routeProvider.when('/members', {templateUrl: 'partials/views/members.html', controller: 'MemberController'});
    $routeProvider.when('/works/:taskId', {templateUrl: 'partials/views/works.html', controller: 'WorkTaskController'});
    $routeProvider.when('/workload', {templateUrl: 'partials/views/workLoad.html', controller: 'WorkLoadController'});
    $routeProvider.when('/realized', {templateUrl: 'partials/views/realized.html', controller: 'RealizedController'});
    $routeProvider.when('/tasks', {templateUrl: 'partials/views/tasks.html', controller: 'TaskController'});
    $routeProvider.when('/newTask', {templateUrl: 'partials/views/taskcreation.html', controller: 'TaskController'});
    $routeProvider.when('/activities', {templateUrl: 'partials/views/activity.html', controller: 'ActivityController'});
    $routeProvider.when('/newActivity', {
        templateUrl: 'partials/views/activitycreation.html',
        controller: 'ActivityController'
    });
    $routeProvider.when('/businesscases', {
        templateUrl: 'partials/views/businesscases.html',
        controller: 'BusinessController'
    });
    $routeProvider.when('/newBusinessCase', {
        templateUrl: 'partials/views/businesscasecreation.html',
        controller: 'BusinessController'
    });
    $routeProvider.when('/checkWork', {
        templateUrl: 'partials/views/checkWork.html',
        controller: 'CheckWorkController'
    });
    $routeProvider.when('/profile', {templateUrl: 'partials/views/profile.html', controller: 'ProfileController'});
    $routeProvider.otherwise({redirectTo: '/home'});
}]);
