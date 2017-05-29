var app = angular.module('app', ['ngRoute','ngResource', 'smart-table']);

app.config(function($routeProvider){

    $routeProvider
        .when('/players',{
            templateUrl: '/views/players.html',
            controller: 'playersController'
        })
        .when('/player',{
            templateUrl: '/views/player.html',
            controller: 'playerController'
        })
        .otherwise(
            { redirectTo: '/players'}
        );
});

app.constant('app_config', {
  baseUrl: 'http://localhost:8080'
});

app.filter('integerValue', function () {
    return function (item) {
      return item | 0;
    }
});

app.filter('attributeValue', function () {
    return function (item) {
      return item.toFixed(1);
    }
});

app.factory('Service', function($resource) {
    return $resource('players');
});