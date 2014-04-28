'use strict';

teamagApp.factory('Task', ['$resource', function ($resource) {
    return $resource('../resources/task/:id', {id: '@id'},
        { 'update': {method: 'PUT'} });
}]);