'use strict';

teamagApp.factory('WorkByTask', ['$resource', function ($resource) {
    return $resource('../resources/works/byTasks', {},
        {});
}]);