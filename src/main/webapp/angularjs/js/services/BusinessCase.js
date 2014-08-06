'use strict';

teamagApp.factory('BusinessCase', ['$resource', function ($resource) {
    return $resource('../resources/business/bc/:id', {id: '@id'},
        { 'update': {method: 'PUT'} });
}]);