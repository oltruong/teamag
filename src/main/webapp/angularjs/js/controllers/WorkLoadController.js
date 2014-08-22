'use strict';

teamagApp.controller('WorkLoadController', ['$scope', '$http', 'WorkLoad',
    function ($scope, $http) {

        $http.get('../resources/workload/').success(function (data) {


            $scope.workloadContainer = data;
        }, function (error) {


        });


        $scope.orderMember = 'name';
        $scope.orderContainerMember = 'member.name';
        $scope.orderProp = 'businessCase.identifier';

        $scope.totalMember = function ($member) {
            var total = 0;
            angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {

                angular.forEach(workLoadContainer.workLoadList, function (workLoad) {
                    if (workLoad.member.id === $member.id) {
                        total += workLoad.estimated;
                    }

                });
            });
            return total;
        };

        $scope.displayClass = function ($member) {
            var comparison = ($member.estimatedWorkDays / 21) - $scope.totalMember($member);
            if (comparison < -0.01) {
                return 'text-danger';
            } else if (comparison > 0.01) {
                return 'text-success';
            } else {
                return 'text-muted';
            }
        };

        $scope.displayClassWorkLoad = function ($workload) {
            if ($workload.estimated === 0) {
                return 'hidden';
            } else if ($workload.estimated < 0) {
                return 'text-danger';
            } else {
                return '';
            }
        };

        $scope.displayTotalClass = function () {
            var comparison = $scope.sumBC() - ($scope.sumMember() / 21);

            if (comparison < -0.01) {
                return 'text-danger';
            } else if (comparison > 0.01) {
                return 'text-success';
            } else {
                return 'text-muted';
            }
        };


        $scope.displayRealizedClass = function (workload) {
            if (workload.realized === 0) {
                return 'hidden';
            } else if (workload.realized > workload.estimated) {
                return 'text-danger';
            } else {
                return 'text-info';
            }
        };

        $scope.displayClassSum = function (value) {

            if (value < -0.01) {
                return 'text-danger';
            } else if (value > 0.01) {
                return '';
            } else {
                return 'text-muted';
            }
        };


        $scope.sumBC = function ($workLoadContainer) {
            var total = 0;

            angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {
                total += workLoadContainer.businessCase.amount;
            });

            return total;
        };


        $scope.sumWorkLoad = function ($workLoadContainer) {
            var total = 0;

            angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {
                angular.forEach(workLoadContainer.workLoadList, function (workLoad) {
                    total += workLoad.estimated;
                });
            });

            return total;
        };

        $scope.sumMember = function () {
            var total = 0;

            angular.forEach($scope.workloadContainer.memberList, function (member) {
                total += member.estimatedWorkDays;
            });

            return total;
        };


        $scope.totalbyBC = function ($workLoadContainer) {
            var total = 0;
            angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {

                if ($workLoadContainer === workLoadContainer) {

                    angular.forEach(workLoadContainer.workLoadList, function (workLoad) {
                        total += workLoad.estimated;

                    });
                }

            });
            return total;
        };


        $scope.update = function () {

            $http.put('../resources/workload/', $scope.workloadContainer.workLoadContainerList).success(function (data) {
                $scope.confirmation = 'Mise à jour effectuée';
                $scope.error = '';
            }, function (error) {

            }).error(function (data, status, headers, config) {
                $scope.confirmation = '';
                $scope.error = 'Erreur HTTP' + status;
            });
        };

        $scope.filterMember = function (workload) {
            var re = new RegExp($scope.queryMember, 'i');
            return re.test(workload.member.name);
        };

        $scope.filterMemberName = function (member) {
            var re = new RegExp($scope.queryMember, 'i');
            return re.test(member.name);
        };

    }]);

