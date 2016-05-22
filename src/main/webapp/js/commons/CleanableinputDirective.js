teamagApp.directive('cleanableinput', function () {
    return {
        scope: {
            field: '=model',
            placeholder: '@'
        },
        templateUrl: "partials/directives/cleanableinput.html",
        restrict: "E"
    };
});