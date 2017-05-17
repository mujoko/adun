var googleMapLoaded = false;
// Called in personalitylifestylelocation.html
function setGoogleMapLoaded() {
  googleMapLoaded = true;
}

$(document).ready(function() {
  // Prevent submit on press of Enter key
  $(window).keydown(function(event) {
    if(event.keyCode === 13) {
      event.preventDefault();
      return false;
    }
  });

  function drawMap() {
    // Fixes Cannot read property 'firstChild' of null because google map might have been loaded but the DOMs are not yet drawn
    if(!googleMapLoaded) {
      setTimeout(drawMap, 500);
    } else {
      var california = {lat: 37.4419, lng: -122.1419};
      var map = new google.maps.Map(document.getElementById('google-maps-container'), {
        center: california,
        zoom: 13
      });
      var marker = null;
      var searchMarker = null;
      var infoWindow = new google.maps.InfoWindow;
      var handleLocationError = function(browserHasGeolocation, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
            'Error: The Geolocation service failed.' :
            'Error: Your browser doesn\'t support geolocation.');
        infoWindow.open(map);
      };

      var nearbyUserMarkers = null;

      function isFunction(functionToCheck) {
        var getType = {};
        return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
      }

      var createMarkerOptions = function(latlng, gender, markerAnimation) {
        var markerOptions = {
          position: latlng,
          draggable: true,
          map: map
        };
        if(markerAnimation) {
          markerOptions.animation = markerAnimation;
        }
        if(gender === 'MALE' || gender === 'FEMALE') {
          var image = {
            scaledSize: new google.maps.Size(25, 25)
          };
          if(gender === 'MALE') {
            image.url = baseUrl + '/images/boy.png';
          } else {
            image.url = baseUrl + '/images/girl.png';
          }
          markerOptions.icon = image;
        }
        return markerOptions;
      };
      /**
       * Converts latlng object which lat/lng property is function to plain latlng object with lat/lng property as float
       * @param latlng
       * @returns {{}}
       */
      var toLatLng = function(latlng) {
        var result = {};
        result.lat = isFunction(latlng.lat) ? latlng.lat() : latlng.lat;
        result.lng = isFunction(latlng.lng) ? latlng.lng() : latlng.lng;
        return result;
      };
      var $lastMileRadiusSearch = $('#last-mile-radius-search');
      var $lastMileRadiusSearchSlider = $('#last-mile-radius-search-slider');
      var showNearbyUsers = function(latlng) {
        var radius = parseInt($lastMileRadiusSearch.val());
        if(radius) {
          if(nearbyUserMarkers !== null) {
            for (var i = 0; i < nearbyUserMarkers.length; i++) {
              var nearbyUserMarker = nearbyUserMarkers.pop();
              nearbyUserMarker.setMap(null);
            }
          } else {
            nearbyUserMarkers = [];
          }
          var point = toLatLng(latlng);
          $.ajax({
            type: "POST",
            url: root + 'users/nearby',
            data: JSON.stringify({
              latitude: point.lat,
              longitude: point.lng,
              radius: radius
            }),
            contentType: "application/json",
            dataType: 'json',
            success: function(results) {
              for (var i = 0; i < results.length; i++) {
                var userLocation = results[i];
                var markerOptions = createMarkerOptions({
                  lat: userLocation.latitude,
                  lng: userLocation.longitude
                }, userLocation.gender, google.maps.Animation.DROP);
                nearbyUserMarkers.push(new google.maps.Marker(markerOptions));
              }
            }
          });
        }
      };
      $lastMileRadiusSearch.blur(function() {
        $lastMileRadiusSearchSlider.val($lastMileRadiusSearch.val());
        if(marker) {
          showNearbyUsers(marker.position);
        }
      });
      // On slider change
      (function(el, timeout) {
        var timer, trig = function() {
          $lastMileRadiusSearch.val($lastMileRadiusSearchSlider.val());
          if(marker) {
            showNearbyUsers(marker.position);
          }
        };
        el.bind("change", function() {
          if(timer) {
            clearTimeout(timer);
          }
          timer = setTimeout(trig, timeout);
        });
      })($lastMileRadiusSearchSlider, 500);

      var currentUserGender = $('#gender').val();
      var updateLocation = function(event) {
        if(marker !== null) {
          marker.setMap(null);
        }
        if(searchMarker !== null) {
          searchMarker.setMap(null);
        }
        var point = toLatLng(event.latLng);
        console.log('Setting user location lat:lng to ' + point.lat + ':' + point.lng);
        $latitude.val(point.lat);
        $longitude.val(point.lng);
        marker = new google.maps.Marker(createMarkerOptions(point, currentUserGender, google.maps.Animation.BOUNCE));
        google.maps.event.addListener(marker, "dragend", updateLocation);
        showNearbyUsers(point);
      };

      var $latitude = $('#latitude');
      var $longitude = $('#longitude');
      if($latitude.val() && $longitude.val()) {
        console.log('Initial map center ' + $latitude.val() + ':' + $longitude.val());
        var pos = {
          lat: parseFloat($latitude.val()),
          lng: parseFloat($longitude.val())
        };
        map.setCenter(pos);
        updateLocation({latLng: pos});
      } else {
        console.log('Fething map center through geolocation');
        // Try HTML5 geolocation.
        if(navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
              lat: position.coords.latitude,
              lng: position.coords.longitude
            };
            map.setCenter(pos);
            updateLocation({latLng: pos});
          }, function() {
            handleLocationError(true, map.getCenter());
          });
        } else {
          // Browser doesn't support Geolocation
          handleLocationError(false, map.getCenter());
        }
      }
      google.maps.event.addListener(map, "click", updateLocation);

      // Geocoding
      var geocoder = new google.maps.Geocoder();
      var $lastAddressSearch = $('#last-address-search').keyup(function(e) {
        if(e.which === 13) {
          var address = $lastAddressSearch.val();
          if(address) {
            geocoder.geocode({'address': address}, function(results, status) {
              if(status === 'OK') {
                var location = results[0].geometry.location;
                map.setCenter(location);
                showNearbyUsers(location);
              } else {
                alert('Geocode was not successful for the following reason: ' + status);
              }
            });
          } else {
            // Set center to last selected marker location
            map.setCenter(marker.position);
            showNearbyUsers(marker.position);
          }
        }
      });
    }
  }

  drawMap();

  function degreesToRadians(degrees) {
    return degrees * Math.PI / 180;
  }

  function distanceInKmBetweenEarthCoordinates(lat1, lon1, lat2, lon2) {
    var earthRadiusKm = 6371;

    var dLat = degreesToRadians(lat2 - lat1);
    var dLon = degreesToRadians(lon2 - lon1);

    lat1 = degreesToRadians(lat1);
    lat2 = degreesToRadians(lat2);

    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadiusKm * c;
  }
});