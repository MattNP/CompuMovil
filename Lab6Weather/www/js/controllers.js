angular.module('app.controllers', ['app.services'])

.controller('weather4AllCtrl', function($scope, $stateParams, City) {

  $scope.search = function(inputCity) {
    var cityName = 'q=' + inputCity;
    $scope.city = City.get({query: cityName});
    };

    var onSuccess = function(position) {
      var latlon = 'lat=' + position.coords.latitude + '&lon=' +  position.coords.longitude;
      $scope.city = City.get({query: latlon});
      console.log("getlonlat");
    };

    function onError(error) {
      console.log("error geolocation");
      alert('Error obteniendo la ubicación\n' +
            'Cargando la ciudad por defecto\n' +
            'Mensaje: ' + error.message + '\n');

      $scope.city = City.get({query: 'q=Medellin'});
    }

    var options = { timeout: 10000, enableHighAccuracy: true };

    document.addEventListener("deviceready", onDeviceReady, false);
    function onDeviceReady() {
      navigator.geolocation.getCurrentPosition(onSuccess, onError, options);
    }
})
