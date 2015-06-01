'use strict';

teamagApp.factory('Task', ['$resource', function ($resource) {
    return $resource('../resources/tasks/:id', {id: '@id'},
        {'update': {method: 'PUT'}});
}]);