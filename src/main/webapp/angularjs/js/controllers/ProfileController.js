'use strict';

teamagApp.controller('ProfileController', ['$scope', '$http', 'md5',
    function ($scope, $http, md5) {

        $http.get('../resources/member/profile').success(function (member) {
            $scope.member = member;
            $scope.member.color = '#' + member.absenceHTMLColor;
            $scope.member.newPassword1 = '';
            $scope.member.newPassword2 = '';
            $scope.member.hash = md5.createHash(member.email);
        });

        $scope.updateMember = function () {
            $http.put('../resources/member/profile', $scope.member).success(function () {
            }).error(function () {
                console.log('error');
            });
        };

        $scope.updatePassword = function () {
            $scope.member.newPassword = $scope.member.newPassword1;
            $scope.updateMember();
        };

        $scope.updateColor = function () {
            $scope.member.absenceHTMLColor = $scope.member.color.substring(1);
            $scope.updateMember();
        };
    }]);
