'use strict';

teamagApp.controller('WorkLoadController', ['$scope', '$http',
    function ($scope, $http) {

        var loaded = false;

        $http.get('resources/workload/').success(function (data) {
            $scope.workloadContainer = data;
            loaded = true;
        }, function (error) {
        });

        $scope.unit = 'MHI';

        $scope.orderMember = 'name';
        $scope.orderContainerMember = 'member.name';
        $scope.orderProp = 'businessCase.identifier';

        $scope.nonFullBCOnly = false;
        $scope.nonEmptyBCOnly = true;

        $scope.showRemaining = false;

        $scope.changeUnit = function () {
            if ($scope.unit === 'MHI') {
                $scope.unit = 'JHI';
            } else {
                $scope.unit = 'MHI';
            }
        };

        $scope.otherUnit = function () {
            if ($scope.unit === 'MHI') {
                return 'JHI';
            } else {
                return 'MHI';
            }
        };

        $scope.factor = function () {
            if ($scope.unit === 'MHI') {
                return 1;
            } else {
                return 21;
            }
        };

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


        $scope.totalRealizedMember = function ($member) {
            var total = 0;
            angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {

                angular.forEach(workLoadContainer.workLoadList, function (workLoad) {
                    if (workLoad.member.id === $member.id) {
                        total += workLoad.realized;
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
                return 'hidden';
            }
        };

        $scope.displayClassWorkLoad = function ($estimated) {
            if ($estimated < -0.01) {
                return 'text-danger';
            } else if ($estimated < 0.01) {
                return 'hidden';
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
            } else if (workload.realized > workload.estimated + 0.05) {
                return 'text-danger';
            } else {
                return 'text-info';
            }
        };

        $scope.displayRealizedTotal = function (value) {
            if (value > 0.1) {
                return 'text-info';
            } else if (value < -0.1) {
                return 'text-danger';
            } else {
                return 'hidden';
            }
        };


        $scope.displayClassSum = function (value) {

            if (value < -0.01) {
                return 'text-danger';
            } else if (value > 0.01) {
                return '';
            } else {
                return 'hidden';
            }
        };

        $scope.displayMacroClassSum = function (value) {

            if (value < -0.1) {
                return 'text-danger';
            } else if (value > 0.1) {
                return '';
            } else {
                return 'hidden';
            }
        };


        $scope.displayClassGlyphicon = function (value) {

            if (value < -0.01) {
                return 'glyphicon glyphicon-remove';
            } else if (value > 0.01) {
                return 'glyphicon glyphicon-flag';
            } else {
                return 'glyphicon glyphicon-ok';
            }
        };

        $scope.displayClassText = function (value) {

            if (value < -0.01) {
                return 'text-danger';
            } else if (value > 0.01) {
                return 'text-warning';
            } else {
                return 'text-success';
            }
        };


        $scope.sumBC = function ($workLoadContainer) {
            var total = 0;
            if (loaded) {
                angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {
                    total += workLoadContainer.businessCase.amount;
                });
            }

            return total;
        };


        $scope.sumWorkLoad = function ($workLoadContainer) {
            var total = 0;
            if (loaded) {
                angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {
                    angular.forEach(workLoadContainer.workLoadList, function (workLoad) {
                        total += workLoad.estimated;
                    });
                });
            }

            return total;
        };

        $scope.sumMember = function () {
            var total = 0;
            if (loaded) {
                angular.forEach($scope.workloadContainer.memberList, function (member) {
                    total += member.estimatedWorkDays;
                });

            }

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

        $scope.totalRealizedByBC = function ($workLoadContainer) {
            var total = 0;
            angular.forEach($scope.workloadContainer.workLoadContainerList, function (workLoadContainer) {

                if ($workLoadContainer === workLoadContainer) {

                    angular.forEach(workLoadContainer.workLoadList, function (workLoad) {
                        total += workLoad.realized;

                    });
                }

            });
            return total;
        };


        $scope.update = function () {

            $http.put('resources/workload/', $scope.workloadContainer.workLoadContainerList).success(function (data) {
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

        $scope.filterNonFullBC = function (workLoadContainer) {
            if ($scope.nonFullBCOnly) {
                return Math.abs(workLoadContainer.businessCase.amount - $scope.totalbyBC(workLoadContainer)) > 0.01;
            } else {
                return true;
            }
        };

        $scope.filterNonEmptyBC = function (workLoadContainer) {
            console.log('begin filter');
            if ($scope.nonEmptyBCOnly) {

                for (var i = 0; i < $scope.filteredMembers.length; i++) {
                    var member = $scope.filteredMembers[i];
                    console.log('member!!' + member.name);
                    for (var j = 0; j < workLoadContainer.workLoadList.length; j++) {
                        var workload = workLoadContainer.workLoadList[j];
                        console.log('workload!!' + workload);
                        if (workload.member.name === member.name) {
                            console.log('yahoooo' + workload.estimated + "-" + workload.realized);
                            if (workload.estimated !== 0 || workload.realized !== 0) {
                                console.log('truuuue');
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            return true;

        };

    }]);

