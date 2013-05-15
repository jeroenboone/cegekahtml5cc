'use strict';

/* Controllers */

function MeetingListController($scope, Meeting) {

    $scope.meetings = Meeting.query();

    $scope.orderProp = 'datum';
}
MeetingListController.$inject = ['$scope', 'Meeting'];

function MyCtrl1() {}
MyCtrl1.$inject = [];


function MyCtrl2() {
}
MyCtrl2.$inject = [];


function SessionCtrl($scope, $http, authService) {
    window.scope =  $scope;

    $scope.publicContent = [];
    $scope.restrictedContent = [];

    $scope.publicAction = function() {
      $http.post('data/public', $scope.publicData).success(function(response) {
        $scope.publicContent.push(response);
      });
    }

    $scope.restrictedAction = function() {
      $http.post('data/protected', $scope.restrictedData).success(function(response) {
        // this piece of code will not be executed until user is authenticated
        $scope.restrictedContent.push(response);
      });
    }

    $scope.logout = function() {
      $http.post('auth/logout').success(function() {
        $scope.restrictedContent = [];
      });
    }

    $scope.login = function() {
      $http.post('auth/login').success(function() {
        authService.loginConfirmed();
      });
    }

}
SessionCtrl.$inject = ['$scope', '$http', 'authService'];