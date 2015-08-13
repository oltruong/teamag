'use strict';

/* Filters */

teamagApp.filter('multiplier', function () {
    return function (number, coefficient) {
        coefficient = coefficient || 1;
        return number * coefficient;
    }
});
