'use strict';

export default class Coordinate
{
	constructor(lat,lon){
			alert("coucou");
		   this.latitude = lat;
		   this.longitude=lon;
		}

	
	load(ville){
        if(ville != ""){
            $.ajax({
                url: "https://nominatim.openstreetmap.org/search", // URL de Nominatim
                type: 'get', // Requête de type GET
                data: "q="+ville+"&format=json&addressdetails=1&limit=1&polygon_svg=1" // Données envoyées (q -> adresse complète, format -> format attendu pour la réponse, limit -> nombre de réponses attendu, polygon_svg -> fournit les données de polygone de la réponse en svg)
            }).done(function (response) {
                if(response != ""){
                	this.latitude = response[0]['lat'];
                	this.longitude = response[0]['lon'];
                }                
            }).fail(function (error) {
                alert(error);
            });      
        }
	
	toString()
	{
		return this.latitude+" "+this.longitude;
	}
}
	


