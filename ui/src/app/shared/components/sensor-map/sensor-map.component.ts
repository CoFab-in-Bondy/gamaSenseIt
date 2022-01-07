import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import * as L from "leaflet";
import { Subscription } from "rxjs";
import { DELAY_DEAD, DELAY_NO_SIGNAL } from "src/app/constantes";
import { HumanService } from "../../services/human.service";
import { SensorMetadataService } from "../../services/sensorMetadata.service";

const MarkerIcon: any = L.Icon.extend({
  options: {
    shadowUrl: "assets/markers/marker-shadow.png",
    iconSize: [25, 40], // size of the icon
    shadowSize: [41, 41], // size of the shadow
    iconAnchor: [12, 40], // point of the icon which will correspond to marker's location
    shadowAnchor: [13, 40], // the same for the shadow
    popupAnchor: [13, 40], // point from which the popup should open relative to the iconAnchor
  },
});

const greenMarker = new MarkerIcon({
  iconUrl: "assets/markers/marker-green.png",
});
const orangeMarker = new MarkerIcon({
  iconUrl: "assets/markers/marker-orange.png",
});
const redMarker = new MarkerIcon({ iconUrl: "assets/markers/marker-red.png" });
const clickMarker = new MarkerIcon({ iconUrl: "assets/markers/marker-icon.png" })


@Component({
  selector: "app-sensor-map",
  templateUrl: "./sensor-map.component.html",
  styleUrls: ["./sensor-map.component.scss"],
})
export class SensorMapComponent implements OnInit {
  private map: L.Map;
  private sensorsSubscription: Subscription;
  private routeSubscription: Subscription;
  private sensorsMetadata: SensorMetadataExtended[] = [];
  private markers = new Map<number, [Sensor, any]>();
  private markerActive: any;
  private sensorActive: any;

  @Input() height: number = 250;

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  private initMap(): void {
    this.map = L.map("map", {
      center: [48.856614, 2.3522219],
      zoom: 12,
    });

    const tiles = L.tileLayer(
      "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
      {
        maxZoom: 18,
        minZoom: 1,
        attribution:
          "&copy; <a href='http://www.openstreetmap.org/copyright'>OpenStreetMap</a>",
      }
    );

    tiles.addTo(this.map);
  }

  ngOnInit(): void {
    this.initMap();
    this.sensorsSubscription = this.sensorMetadataService
      .observeAll()
      .subscribe(
        (sensors) => {

          // add icons to the map
          this.sensorsMetadata = sensors;
          for (let sensorMetadata of this.sensorsMetadata) {
            for (let sensor of sensorMetadata.sensors) {
              this.addSensorToMap(sensor);
            }
          }

          // highlight the actual icons
          this.routeSubscription = <any>this.route.firstChild?.params.subscribe(params => {
            for (let sensorMetadata of this.sensorsMetadata) {
              for (let sensor of sensorMetadata.sensors) {
                if (sensor.id === +params["id"]) {
                  this.addSensorToMap(sensor, true);
                  return;
                }
              }
            }
          });
          this.update();
        },
        (err) => console.error(err)
      );
    this.sensorMetadataService.lazyLoad();
  }

  update() {
    this.map.invalidateSize();
  }

  private deactiveMarker() {
    if (this.markerActive && this.sensorActive) {
      const sensorActive = this.sensorActive;
      this.markerActive = null;
      this.sensorActive = null;
      this.addSensorToMap(sensorActive);
    }
  }

  private addSensorToMap(s: Sensor, clicked: boolean = false) {

    // generate an icon
    const marker = L.marker(
      [s.latitude, s.longitude],
      { icon: clicked? clickMarker: this.state(s) }
    );

    // remove old icon
    if (clicked) {
      this.deactiveMarker();
      this.markerActive = marker;
      this.sensorActive = s;
    }

    // replace by actual marker if sensor already has an icon
    if (this.markers.has(s.id)) {
      this.map.removeLayer((<any>this.markers.get(s.id))[1]);
    }

    // add it to the map
    this.markers.set(s.id, [s, marker]);
    marker.addTo(this.map);
    marker.on("click", (e: L.LeafletEvent) => {
      this.router.navigate(["sensors", s.id]);
    });
  }

  ngOnDestroy(): void {
    this.sensorsSubscription?.unsubscribe();
    this.routeSubscription?.unsubscribe();
  }

  state(s: Sensor): any {
    let now = new Date().getTime();
    let date = new Date(s.lastCaptureDate).getTime();
    if (date > now - DELAY_NO_SIGNAL) {
      return greenMarker;
    } else if (date > now - DELAY_DEAD) {
      return orangeMarker;
    } else {
      return redMarker;
    }
  }
}
