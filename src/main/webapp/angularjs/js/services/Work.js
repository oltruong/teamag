'use strict';

teamagApp.factory('Work', ['$resource', function ($resource) {
    return $resource('../resources/work/task/:id', {id: '@id'},
        {'update': {method: 'PUT'}});
}]);