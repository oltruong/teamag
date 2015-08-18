teamagApp.directive('weekcomment', function () {
    return {
        scope: {
            disabled: '=disabled',
            weekcomment: '=comment',
            hide: '=hide'
        },
        templateUrl: "partials/directives/weekcomment.html",
        restrict: "E"
    };
});