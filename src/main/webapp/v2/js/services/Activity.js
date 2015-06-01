'use strict';

teamagApp.factory('Activity', ['$resource', function ($resource) {
    return $resource('../resources/activity/:id', {id: '@id'},
        {'update': {method: 'PUT'}});
}]);