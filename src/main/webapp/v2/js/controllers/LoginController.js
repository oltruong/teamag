'use strict';

teamagApp.controller('LoginController', ['$http', '$scope', '$location', 'Login', 'userInfo','md5',
    function ($http, $scope, $location, Login, userInfo,md5) {


        $scope.login = function () {

            if (typeof $scope.password === 'undefined') {
                $scope.password = '';
            }

            Login.get({loginInformation: $scope.username + "-" + $scope.password}, function (member) {
                userInfo.loggedIn = true;
                userInfo.name = member.name;
                userInfo.admin = member.memberType === 'ADMINISTRATOR';
                userInfo.supervisor = userInfo.admin || member.memberType === 'SUPERVISOR';
                userInfo.id = member.id;
                userInfo.hash= md5.createHash(member.email);
                userInfo.password = member.password;
                $http.defaults.headers.common.Authorization = member.password;
                $http.defaults.headers.common.userid = member.id;
                $scope.error = '';
                $location.path('home');

            }, function (error) {
                if (error.status === "404") {
                    $scope.error = 'Utilisateur ou mot de passe incorrect';
                } else {
                    $scope.error = 'Error HTTP ' + error.status;
                }
            });

        };
    }]);
