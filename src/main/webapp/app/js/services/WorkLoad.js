'use strict';

teamagApp.factory('WorkLoad', ['$resource', function ($resource) {
    return $resource('../resources/workload',
        { 'update': {method: 'PUT'} });
}]);