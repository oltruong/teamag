teamagApp.directive('confirmationmessage', function () {
    return {
        scope: {
            title: '=disabled',
            detail: '=comment',
            class: '=hide'
        },
        templateUrl: "partials/directives/confirmation.html",
        restrict: "E"
    }
});
