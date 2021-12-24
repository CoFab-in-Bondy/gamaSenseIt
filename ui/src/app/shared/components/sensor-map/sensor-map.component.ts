import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as L from 'leaflet';
import { Subscription } from 'rxjs';
import { HumanService } from '../../services/human.service';
import { SensorMetadataService } from '../../services/sensorMetadata.service';

const MarkerIcon: any = L.Icon.extend({
  options: {
    shadowUrl: 'assets/markers/marker-shadow.png',
    iconSize:     [25, 40], // size of the icon
    shadowSize:   [41, 41], // size of the shadow
    iconAnchor:   [12, 40], // point of the icon which will correspond to marker's location
    shadowAnchor: [13, 40],  // the same for the shadow
    popupAnchor:  [13, 40] // point from which the popup should open relative to the iconAnchor
  }
});

const greenMarker = new MarkerIcon({iconUrl: 'assets/markers/marker-green.png'});
const orangeMarker = new MarkerIcon({iconUrl: 'assets/markers/marker-orange.png'});
const redMarker = new MarkerIcon({iconUrl: 'assets/markers/marker-red.png'});
const stateToIcon = {
  "DEAD": redMarker,
  "ACTIVE": greenMarker,
  "NO_SIGNAL": orangeMarker
}


@Component({
  selector: 'app-sensor-map',
  templateUrl: './sensor-map.component.html',
  styleUrls: ['./sensor-map.component.scss']
})
export class SensorMapComponent implements OnInit {
  private map: L.Map;
  private sensorsSubscription: Subscription;
  private sensorsMetadata: SensorMetadataExtended[] = [];

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private router: Router
    ) { }

  private initMap(): void {
    this.map = L.map('map', {
      center: [48.856614, 2.3522219],
      zoom: 12
    })

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 1,
      attribution: "&copy; <a href='http://www.openstreetmap.org/copyright'>OpenStreetMap</a>"
    });

    tiles.addTo(this.map);
  }


  ngOnInit(): void {
    this.initMap();
    this.sensorsSubscription = this.sensorMetadataService.observeAll().subscribe(
      sensors => {
        this.sensorsMetadata = sensors;
        for (let sensorMetadata of this.sensorsMetadata) {
          for (let sensor of sensorMetadata.sensors) {
            this.addSensorToMap(sensor);
          }
        }
      },
      err => console.error(err)
    );
    this.sensorMetadataService.lazyLoad();
  }

  private addSensorToMap(s: Sensor) {
    const mark = L.marker([s.latitude, s.longitude], {icon: stateToIcon[s.state]});
    mark.on("click", (e: L.LeafletEvent) => {
      this.router.navigate(["sensors", s.id])
    });
    mark.addTo(this.map);
  }

  ngOnDestroy(): void {
    this.sensorsSubscription?.unsubscribe();
  }
}
