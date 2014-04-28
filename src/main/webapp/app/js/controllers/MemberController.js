'use strict';

teamagApp.controller('MemberController', ['$scope', '$http', 'Member',
    function ($scope, $http, Member) {


        $scope.members = Member.query(function () {
        }, function (error) {
            $scope.error = 'Erreur HTTP ' + error.status;
        });


        $scope.orderProp = 'name';

        $scope.totalDays = function () {
            var totalDays = 0;
            angular.forEach($scope.filteredMembers, function (member) {

                totalDays += member.estimatedWorkDays;
            })
            return totalDays;
        }

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

        }

    }]);

