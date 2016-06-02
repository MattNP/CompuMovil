angular.module('app.services', ['ngResource'])

.factory('City', function ($resource) {
    return $resource('http://api.openweathermap.org/data/2.5/weather?:query'+ '&units=metric&APPID=52e359532a13971b030578a1c0e35e07');
});
