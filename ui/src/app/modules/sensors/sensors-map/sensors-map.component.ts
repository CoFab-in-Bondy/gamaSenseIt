import {Component, Input, OnInit, OnDestroy, Output, EventEmitter, SecurityContext} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import * as L from "leaflet";
import { Subscription } from "rxjs";
import {DELAY_DEAD, DELAY_NO_SIGNAL, LEAFLET_ATTRIBUTION, LEAFLET_URL, SM} from "src/app/constantes";
import { CLICK_MARKER, GREEN_MARKER, ORANGE_MARKER, RED_MARKER } from "@models/icon.model";
import { HumanService } from "@services/human.service";
import { SensorMetadataService } from "@services/sensorMetadata.service";
import {SecurePipe} from "@pipes/secure.pipe";
import {DomSanitizer} from "@angular/platform-browser";
import {AuthService} from "@services/auth.service";

@Component({
  selector: "app-sensors-map",
  templateUrl: "./sensors-map.component.html",
  styleUrls: ["./sensors-map.component.scss"],
})
export class SensorsMapComponent implements OnInit, OnDestroy {
  private map: L.Map;
  private routeSubscription: Subscription;
  private sensorsMetadata: SensorMetadataExtended[] = [];
  private markers = new Map<number, [Sensor, any]>();
  private markerActive: any;

  private sensorActive: any;
  private sensorMetadataActive: any;

  @Input() height: number = 600;
  @Output() select = new EventEmitter<number>();

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private router: Router,
    private route: ActivatedRoute,
    private secure: SecurePipe,
    private sanitizer: DomSanitizer,
    public auth: AuthService
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
              this.addSensorToMap(sensor, sensorMetadata);
            }
          }

          // highlight the actual icons
          this.routeSubscription = <any>this.route.firstChild?.params.subscribe(params => {
            for (let sensorMetadata of this.sensorsMetadata) {
              for (let sensor of sensorMetadata.sensors) {
                if (sensor.id === +params["id"]) {
                  this.addSensorToMap(sensor, sensorMetadata, true);
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

  private deactivateMarker() {
    if (this.markerActive && this.sensorActive && this.sensorMetadataActive) {
      const sensorActive = this.sensorActive;
      const sensorMetadataActive = this.sensorMetadataActive;
      this.markerActive = null;
      this.sensorActive = null;
      this.sensorMetadataActive = null;
      this.addSensorToMap(sensorActive, sensorMetadataActive);
    }
  }

  private addSensorToMap(s: Sensor, smd: SensorMetadataExtended, clicked: boolean = false) {

    // generate an icon
    const marker = L.marker(
      [s.latitude, s.longitude],
      { icon: clicked? CLICK_MARKER: this.state(s) }
    );

    // remove old icon
    if (clicked) {
      this.deactivateMarker();
      this.markerActive = marker;
      this.sensorActive = s;
      this.sensorMetadataActive = smd;
    }

    // replace by actual marker if sensor already has an icon
    if (this.markers.has(s.id)) {
      this.map.removeLayer((<any>this.markers.get(s.id))[1]);
    }

    // add it to the map
    this.markers.set(s.id, [s, marker]);
    marker.addTo(this.map);
    marker.on("click", (e: L.LeafletEvent) => {
      this.addSensorToMap(s, smd, true);
      this.router.navigate(["/sensors", s.id]);
      this.select.emit(s.id);
    });

    marker.on('mouseover', e => {
      this.secure.transform(`/public/sensors/${s.id}/image`).subscribe(
        img=>{
          const src = this.sanitizer.sanitize(SecurityContext.URL, img);
          marker.bindPopup('<p>' + s.displayName + '</p>' +
            '<img src="' + src +'" alt="Image du capteur" width="100px" height="100px">');
          marker.openPopup();
        },
        err=>{}
      );
    });
    marker.on('mouseout', function (e) {
      setTimeout(()=>marker.closePopup(), 100);
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

  getSize() {
    return this.auth.isUser()? window.innerHeight - 200: window.innerHeight - 140;
  }
}
