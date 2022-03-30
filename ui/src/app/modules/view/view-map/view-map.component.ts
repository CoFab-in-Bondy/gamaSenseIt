import { Component, Input, OnInit, OnDestroy, Output, EventEmitter } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import * as L from "leaflet";
import { Subscription } from "rxjs";
import { DELAY_DEAD, DELAY_NO_SIGNAL, LEAFLET_ATTRIBUTION, LEAFLET_URL } from "src/app/constantes";
import { CLICK_MARKER, GREEN_MARKER, ORANGE_MARKER, RED_MARKER } from "@models/icon.model";
import { HumanService } from "@services/human.service";
import { SensorMetadataService } from "@services/sensorMetadata.service";

@Component({
  selector: "app-view-map",
  templateUrl: "./view-map.component.html",
  styleUrls: ["./view-map.component.scss"],
})
export class ViewMapComponent implements OnInit, OnDestroy {
  private map: L.Map;
  private routeSubscription: Subscription;
  private sensorsMetadata: SensorMetadataExtended[] = [];
  private markers = new Map<number, [Sensor, any]>();
  private markerActive: any;
  private sensorActive: any;

  @Input() height: number = 600;
  @Output() select = new EventEmitter<number>();

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  private initMap(): void {
    this.map = L.map("map-view-map", {
      center: [48.856614, 2.3522219],
      zoom: 12,
    });

    const tiles = L.tileLayer(
      LEAFLET_URL,
      {
        maxZoom: 18,
        minZoom: 1,
        attribution: LEAFLET_ATTRIBUTION,
      }
    );

    tiles.addTo(this.map);
  }

  init() {
    this.initMap();
    this.sensorMetadataService.getAll()
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

          [0, 100, 500, 2000, 5000].forEach(s => {
            setTimeout(()=>{
              this.map.invalidateSize();
            }, s);
          });
        },
        (err) => console.error(err)
      );
  }

  ngOnInit(): void {
    this.init();
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
      { icon: clicked? CLICK_MARKER: this.state(s) }
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
      this.addSensorToMap(s, true);
      this.router.navigate(["/view", s.id]);
      this.select.emit(s.id);
    });
  }

  ngOnDestroy(): void {
    this.routeSubscription?.unsubscribe();
  }

  state(s: Sensor): any {
    if (s.lastCaptureDate == null) {
      return RED_MARKER;
    } else {
      let now = new Date().getTime();
      let date = new Date(s.lastCaptureDate).getTime();
      if (date > now - DELAY_NO_SIGNAL) {
        return GREEN_MARKER;
      } else if (date > now - DELAY_DEAD) {
        return ORANGE_MARKER;
      } else {
        return RED_MARKER;
      }
    }
  }
}
