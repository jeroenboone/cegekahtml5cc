'use strict';


// Declare app level module which depends on filters, and services
angular.module('eventualizrApp', ['ngResource', 'eventualizrApp.filters', 'eventualizrApp.services', 'eventualizrApp.directives',
                                  'http-auth-interceptor',
                                  'content-mocks'
                                  ]).
  config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/meetings', {templateUrl: 'partials/meeting-list.html', controller: MeetingListController});
    $routeProvider.when('/view1', {templateUrl: 'partials/partial1.html', controller: MyCtrl1});
    $routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: MyCtrl2});
    $routeProvider.when('/session', {templateUrl: 'partials/session.html', controller: MyCtrl2});
    $routeProvider.otherwise({redirectTo: '/meetings'});
  }]);
