'use strict';

teamagApp.factory('WeekComment', ['$resource', function ($resource) {
    return $resource('../resources/weekcomments', {},
        {'update': {method: 'PUT'}});
}]);