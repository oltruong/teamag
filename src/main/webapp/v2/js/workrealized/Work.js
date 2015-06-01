'use strict';

teamagApp.factory('Work', ['$resource', function ($resource) {
    return $resource('../resources/works', {},
        {'update': {method: 'PUT'}});
}]);