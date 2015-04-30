'use strict';

teamagApp.controller('MemberController', ['$scope', '$http', '$location', 'Member',
    function ($scope, $http, $location, Member) {


        $scope.types = ["BASIC", "ADMINISTRATOR"];

        $scope.members = Member.query(function () {
        }, function (error) {
            $scope.error = 'Erreur HTTP ' + error.status;
        });


        $scope.orderProp = 'name';

        $scope.totalDays = function () {
            var totalDays = 0;
            angular.forEach($scope.filteredMembers, function (member) {

                totalDays += member.estimatedWorkDays;
            });
            return totalDays;
        };

        $scope.updateMember = function ($member) {
            Member.update({id: $member.id}, $member, function () {
                $scope.confirmation = 'Membre mis à jour';
                $scope.error = '';
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
                $scope.confirmation = '';
            });
        };

        $scope.refreshMember = function ($member) {

            var oldMember = Member.get({id: $member.id}, function () {
                $member.name = oldMember.name;
                $member.company = oldMember.company;
                $member.email = oldMember.email;
                $member.estimatedWorkDays = oldMember.estimatedWorkDays;
                $member.estimatedWorkMonths = oldMember.estimatedWorkMonths;
                $member.absenceHTMLColor = oldMember.absenceHTMLColor;
                $member.memberType = oldMember.memberType;
                $member.comment = oldMember.comment;
            }, function (error) {
                $scope.error = 'Erreur HTTP' + error.status;
            });

        };

        $scope.create = function () {
            Member.save($scope.newMember, function () {
                $location.path('members').search({confirmation: 'Member ' + $scope.newMember.name + ' ajoutée'});
            }, function (error) {
                if (error.status === "406") {
                    $scope.warning = "Le membre " + $scope.newMember.name + " existe déjà";
                } else {
                    $scope.error = 'Erreur HTTP' + error.status;
                }
            });
        };

    }]);

