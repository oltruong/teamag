'use strict';

var teamagApp = angular.module('teamagApp', ['ngRoute', 'ngResource']);

teamagApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/home', {templateUrl: 'views/home.html', controller: 'HomeController'});
    $routeProvider.when('/login', {templateUrl: 'views/login.html', controller: 'LoginController'});
    $routeProvider.when('/members', {templateUrl: 'views/members.html', controller: 'MemberController'});
    $routeProvider.when('/works/:taskId', {templateUrl: 'views/works.html', controller: 'WorkTaskController'});
    $routeProvider.when('/workload', {templateUrl: 'views/workLoad.html', controller: 'WorkLoadController'});
    $routeProvider.when('/tasks', {templateUrl: 'views/tasks.html', controller: 'TaskController'});
    $routeProvider.when('/newTask', {templateUrl: 'views/taskcreation.html', controller: 'TaskController'});
    $routeProvider.when('/activities', {templateUrl: 'views/activity.html', controller: 'ActivityController'});
    $routeProvider.when('/newActivity', {templateUrl: 'views/activitycreation.html', controller: 'ActivityController'});
    $routeProvider.when('/businesscases', {templateUrl: 'views/businesscases.html', controller: 'BusinessController'});
    $routeProvider.when('/newBusinessCase', {templateUrl: 'views/businesscasecreation.html', controller: 'BusinessController'});
    $routeProvider.otherwise({redirectTo: '/home'});
}]);
