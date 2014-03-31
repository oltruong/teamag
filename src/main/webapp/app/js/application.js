'use strict';

var teamagApp = angular.module('teamagApp', ['ngRoute']);

// Declare app level module which depends on filters, and services
//angular.module('teamagApp', [
//    'ngRoute',
//    'myApp.filters',
//    'myApp.services',
//    'myApp.directives',
//    'myApp.controllers'
//])
teamagApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/members', {templateUrl: 'views/members.html', controller: 'MemberController'});
    $routeProvider.when('/businesscases', {templateUrl: 'views/businesscases.html', controller: 'BusinessController'});
    $routeProvider.when('/newBusinessCase', {templateUrl: 'views/businesscasecreation.html', controller: 'BusinessController'});
    $routeProvider.otherwise({redirectTo: '/businesscases'});
}]);
