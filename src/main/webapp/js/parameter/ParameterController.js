'use strict';

teamagApp.controller('ParameterController', ['$scope', '$http', '$filter', 'userInfo',
    function ($scope, $http, $filter, userInfo) {


        $scope.adminemail = {};
        $scope.smtpserver = {};

        $scope.message = '';

        $http.get('resources/parameter').success(function (data) {

            for (var i = 0; i < data.length; i++) {
                var parameter = data[i];
                console.log(parameter);
                console.log(parameter.name);
                if (parameter.name === 'SMTP_HOST') {
                    $scope.smtpserver = parameter;
                } else if (parameter.name === 'ADMINISTRATOR_EMAIL') {
                    $scope.adminemail = parameter;
                }
            }
            console.log(data);
        }, function (error) {
            $scope.error = error;

        });

        $scope.update = function () {

            var parameters = [];
            parameters.push($scope.adminemail, $scope.smtpserver);
            $http.put('resources/parameter', parameters).success(function (data) {
                $scope.message = 'mise à jour effectuée';
            }).error(function (data) {
                $scope.message = 'Erreur de mise à jour';
                console.log(data);
            });

        };
        $scope.testemail = function () {

            $http.get('resources/parameter/test').success(function (data) {
                $scope.message = 'email envoyé';
            }).error(function (data) {
                $scope.message = "erreur dans l'envoi d'email";
                console.log(data);
            });

        };
    }
])
;
