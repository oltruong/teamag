'use strict';

teamagApp.factory('Activity', ['$resource', function ($resource) {
    return $resource('../resources/business/activity/:id', {id: '@id'},
        { 'update': {method: 'PUT'} });
}]);