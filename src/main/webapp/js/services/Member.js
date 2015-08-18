'use strict';

teamagApp.factory('Member', ['$resource', function ($resource) {
    return $resource('resources/member/:id', {id: '@id'},
        { 'update': {method: 'PUT'} });
}]);