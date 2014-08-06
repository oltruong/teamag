'use strict';

teamagApp.factory('Work', ['$resource', function ($resource) {
    return $resource('../resources/work/:id', {id: '@id'},
        { 'update': {method: 'PUT'} });
}]);