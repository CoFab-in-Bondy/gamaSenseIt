import * as L from "leaflet";



const MarkerIcon: any = L.Icon.extend({
  options: {
    shadowUrl: "assets/markers/marker-shadow.png",
    iconSize: [25, 40], // size of the icon
    shadowSize: [41, 41], // size of the shadow
    iconAnchor: [12, 40], // point of the icon which will correspond to marker's location
    shadowAnchor: [13, 40], // the same for the shadow
    popupAnchor: [0, -25], // point from which the popup should open relative to the iconAnchor
  },
});

export const GREEN_MARKER = new MarkerIcon({
  iconUrl: "assets/markers/marker-green.png",
});
export const ORANGE_MARKER = new MarkerIcon({
  iconUrl: "assets/markers/marker-orange.png",
});
export const RED_MARKER = new MarkerIcon({ iconUrl: "assets/markers/marker-red.png" });
export const GRAY_MARKER = new MarkerIcon({ iconUrl: "assets/markers/marker-gray.png" });
export const CLICK_MARKER = new MarkerIcon({ iconUrl: "assets/markers/marker-icon.png" });
