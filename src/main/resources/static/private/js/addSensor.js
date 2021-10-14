//import Coordinate from '../../js/Coordinate.js';

var focusCoordinate = new Coordinate(48.852969,2.349903);
var macarte = null;

function find_address(address)
{
	alert ("find addre");
	var lat = 0.1;
	var lon = 0.1;
	
    if(address != ""){
        $.ajax({
            url: "https://nominatim.openstreetmap.org/search", // URL de Nominatim
            type: 'get', // Requête de type GET
            data: "q="+address+"&format=json&addressdetails=1&limit=1&polygon_svg=1" // Données envoyées (q -> adresse complète, format -> format attendu pour la réponse, limit -> nombre de réponses attendu, polygon_svg -> fournit les données de polygone de la réponse en svg)
        }).done(function (response) {
            if(response != ""){
                lat = response[0]['lat'];
                lon = response[0]['lon'];
                alert(lon);
                 alert(lat);
                 $("#latitude").val(lat);
                 $("#longitude").val(lon);
                
              //  focusCoordinate = new Coordinate(lat,lon)
                alert("truc");
            }                
        }).fail(function (error) {
            alert(error);
        });      
    }
}


function initMap() {
   // alert(focusCoordinate.latitude);
    //focusCoordinate = new Coordinate(48.852969,2.349903);
	// Créer l'objet "macarte" et l'insèrer dans l'élément HTML qui a l'ID "map"
    macarte = L.map('map').setView([focusCoordinate.latitude, focusCoordinate.longitude], 11);
    // Leaflet ne récupère pas les cartes (tiles) sur un serveur par défaut. Nous devons lui préciser où nous souhaitons les récupérer. Ici, openstreetmap.fr
    L.tileLayer('https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png', {
        // Il est toujours bien de laisser le lien vers la source des données
        attribution: 'données © <a href="//osm.org/copyright">OpenStreetMap</a>/ODbL - rendu <a href="//openstreetmap.fr">OSM France</a>',
        minZoom: 1,
        maxZoom: 20
    }).addTo(macarte);
    var marker = L.marker([focusCoordinate.latitude, focusCoordinate.longitude]).addTo(macarte);
}

