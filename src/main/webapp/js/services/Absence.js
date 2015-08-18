'use strict';

teamagApp.factory('Absence', ['$resource', function ($resource) {
    return $resource('resources/absences/:id', {id: '@id'},
        { 'update': {method: 'PUT'} });
}]);