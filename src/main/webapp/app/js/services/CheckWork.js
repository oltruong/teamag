'use strict';

teamagApp.factory('CheckWork', ['$resource', function ($resource) {
    return $resource('../resources/checkWork/byWeek/:memberId/:weekNumber/:macroTask', {memberId: '@memberId', weekNumber: '@weekNumber', macroTask: '@macroTask'},
        { 'update': {method: 'PUT'} });
}]);