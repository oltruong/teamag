'use strict';

teamagApp.factory('Login', ['$resource', function ($resource) {
    return $resource('../resources/login/:loginInformation', {loginInformation: '@loginInformation'});
}]);