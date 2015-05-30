'use strict';

teamagApp.factory('WeekComment', ['$resource', function ($resource) {
    return $resource('../resources/weekcomments/:id', {id: '@id'},
        {'update': {method: 'PUT'}, 'patch': {method: 'PATCH'}});
}]);