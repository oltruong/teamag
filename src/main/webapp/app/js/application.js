'use strict';

var teamagApp = angular.module('teamagApp', ['ngRoute', 'ngResource']);

teamagApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/members', {templateUrl: 'views/members.html', controller: 'MemberController'});
    $routeProvider.when('/businesscases', {templateUrl: 'views/businesscases.html', controller: 'BusinessController'});
    $routeProvider.when('/newBusinessCase', {templateUrl: 'views/businesscasecreation.html', controller: 'BusinessController'});
    $routeProvider.otherwise({redirectTo: '/businesscases'});
}]);
