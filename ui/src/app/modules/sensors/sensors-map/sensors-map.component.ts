import {Component, Input, OnInit, OnDestroy, Output, EventEmitter, SecurityContext} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import * as L from "leaflet";
import { Subscription } from "rxjs";
import {
  DEFAULT_CENTER,
  DELAY_DEAD,
  DELAY_NO_SIGNAL,
  LEAFLET_ATTRIBUTION,
  LEAFLET_URL
} from "src/app/constantes";
import { CLICK_MARKER, GREEN_MARKER, ORANGE_MARKER, RED_MARKER } from "@models/icon.model";
import { HumanService } from "@services/human.service";
import { SensorMetadataService } from "@services/sensorMetadata.service";
import {SecurePipe} from "@pipes/secure.pipe";
import {DomSanitizer} from "@angular/platform-browser";
import {AuthService} from "@services/auth.service";
import {StateService} from "@services/state.service";
import {DateAgoPipe} from "@pipes/date-ago.pipe";

@Component({
  selector: "app-sensors-map",
  templateUrl: "./sensors-map.component.html",
  styleUrls: ["./sensors-map.component.scss"],
})
export class SensorsMapComponent implements OnInit, OnDestroy {
  private map: L.Map;
  private routeSubscription: Subscription;
  private sensorsMetadata: SensorMetadata<true>[] = [];
  private markers = new Map<number, [Sensor<false>, any]>();
  private markerActive: any;

  private sensorActive: any;
  private sensorMetadataActive: any;

  @Input() height: number = 600;
  @Output() select = new EventEmitter<number>();

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    public stateService: StateService,
    private router: Router,
    private route: ActivatedRoute,
    private secure: SecurePipe,
    private sanitizer: DomSanitizer,
    private ago: DateAgoPipe,
    public auth: AuthService
  ) {}

  private initMap(): void {
    this.map = L.map("map-view-map", {
      ...this.stateService.lastViewLngLat.get()
    });

    this.map.on("dragend", ()=>{
      this.stateService.lastViewLngLat.set({center: this.map.getCenter(), zoom: this.map.getZoom()});
    })

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

  private addSensorToMap(s: Sensor<false>, smd: SensorMetadata<true>, clicked: boolean = false) {

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

    // Add photo and text when mouse is over.
    marker.on('mouseover', e => {
      this.secure.transform(`/public/sensors/${s.id}/image`).subscribe(
        img=>{
          const src = this.sanitizer.sanitize(SecurityContext.URL, img);
          const html = `
            <div style="width: 350px; padding-bottom: 10px">
              <img src="${src}" alt="Image du capteur" width="100px" height="100px" style="display:inline-block;">
              <div style="display:inline-block; width: 200px; margin: 10px">
                <p style="font-weight: bold" class="ellipsis"> ${s.name}</p>
                <p style="font-style: italic" class="ellipsis"> ${this.ago.transform(s.lastCaptureDate)} </p>
              </div>
            </div>
          `;
          console.log(html);
          marker.bindPopup(html);
          marker.openPopup();
        },
        err=>{}
      );
    });

    marker.on('mouseout', function (e) {
      setTimeout(()=>marker.closePopup(), 0);
    });
    marker.on('dragend', function (e) {
      setTimeout(()=>marker.closePopup(), 0);
    });
  }

  ngOnDestroy(): void {
    this.routeSubscription?.unsubscribe();
  }

  state(s: Sensor<false>): any {
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
