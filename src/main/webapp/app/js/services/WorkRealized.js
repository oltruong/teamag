'use strict';

teamagApp.factory('WorkRealized', ['$resource', function ($resource) {
    return $resource('../resources/workrealized',
        { 'update': {method: 'PUT'} });
}]);