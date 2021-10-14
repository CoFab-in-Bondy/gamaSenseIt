import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

import { Sensor } from '../../Sensor';
import { SensorVersionService } from '../../pages/sensor-version/sensor-version-service';

declare let L;

@Component({
  selector: 'app-maps',
  templateUrl: './maps.component.html',
  styleUrls: ['./maps.component.scss']
})

export class MapsComponent implements OnInit {

  lat: number;
  lng: number;
  SearchCapteurForm: FormGroup;
  // sensorMap: Sensor;
  tabSensor: Sensor[];
  map;
  myControl: FormControl = new FormControl();
  filteredOptions: Observable<any>;

  // test autocompletion
  fruits = [
    { name: 'apple', selected: true },
    { name: 'orange', selected: false },
    { name: 'pear', selected: true },
    { name: 'naartjie', selected: false },
    { name: 'apple1', selected: true },
    { name: 'orange1', selected: false },
    { name: 'pear1', selected: true },
    { name: 'naartjie1', selected: false }
  ];
  sensorChecked: any[];
  // Fin test

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private sensorService: SensorVersionService) {
    this.SearchCapteurForm = this.fb.group({
      name: ['', Validators.required],
    });
  }

  getsensorChecked() {
    return this.sensorChecked;
  }

  onSearchSensor() {

    const sensorName = this.myControl.value;
    console.log(sensorName);
    // creer un moteur de recherche par le biais de leaflet
    // this.map.flyTo([43.6316, 3.89706], 15);

    for (const sensor of this.tabSensor) {
      const tabAdress = sensor.subDisplayName.toLowerCase().split(' ');
      // console.log(tabAdress);
      if (sensor.name === sensorName) {
        console.log(sensor.latitude + ' ' + sensor.longitude);
        this.map.flyTo([sensor.latitude, sensor.longitude], 15);
      } else {
        for (const item of tabAdress) {
          if (sensorName.toLowerCase() === item ) {
            console.log('adresse');
            console.log(sensor.latitude + ' ' + sensor.longitude);
            this.map.flyTo([sensor.latitude, sensor.longitude], 10);
          }
        }
      }
    }
  }

  ngOnInit() {
    const myIcon = L.icon({
      iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.2.0/images/marker-icon.png'
    });
    this.autoCompletInit();
    this.tabSensor = this.sensorService.loadSensors();
    this.initSensorChecked();

    // tslint:disable-next-line: one-variable-per-declaration
    const mapboxUrl = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      mapboxAttribution = ' UMMISCO Dashboard Sensor\'s Maps';

    // tslint:disable-next-line: one-variable-per-declaration
    const soilSensor = L.marker([14.731812, -17.433000], { icon: myIcon }).bindPopup("This is Soil Sensor <br> <button (click)='visualuser()'>Visualiser</button>"),
      humidity = L.marker([14.741995, -17.433543], { icon: myIcon }).bindPopup('This is humidity sensor '),
      temperature = L.marker([14.731095, -17.435143], { icon: myIcon }).bindPopup('This is temperature sensor'),
      rain = L.marker([14.721995, -17.437343], { icon: myIcon }).bindPopup('This is rain Detection Sensor');
    const sensors = L.layerGroup([soilSensor, humidity, temperature, rain]);
    // const sensors = L.layerGroup([]);
    // tslint:disable-next-line: one-variable-per-declaration
    const field = L.tileLayer(mapboxUrl, { id: 'mapbox.satellite', attribution: mapboxAttribution }),
      streets = L.tileLayer(mapboxUrl, { id: 'mapbox.streets', attribution: mapboxAttribution });

    // Ajout des positions des capteurs du serveur local
    this.sensorService.getSensors().subscribe(
      (data: Sensor[]) => {
        for (const sensor of data) {
          L.marker([
            JSON.parse(JSON.stringify(sensor)).latitude,
            JSON.parse(JSON.stringify(sensor)).longitude
          ], { icon: myIcon }).bindPopup(sensor.sensorMetadataName).
            addTo(this.map);
          // utiliser bindPopup si bindTooltip ne fonctionne pas avec les ecrans tactiles
        }
      }
    );

    // tslint:disable-next-line: variable-name
    let index_lat = 0;
    // tslint:disable-next-line: variable-name
    let index_lng = 0;
    if (this.router.url !== '/maps' && this.router.url.includes('&')) {
      index_lat = this.router.url.lastIndexOf('/') + 1;
      index_lng = this.router.url.lastIndexOf('&') + 1;
    }
    if (index_lat > 0) {
      this.lat = Number(this.router.url.slice(index_lat, index_lng - 1));
    } else {
      this.lat = 0;
    }
    if (index_lng > 0) {
      this.lng = Number(this.router.url.slice(index_lng));
    } else {
      this.lng = 0;
    }
    this.map = L.map('mapid', {
      center: [this.lat, this.lng],
      zoom: 5,
      layers: [field, sensors]
    });
    this.map.on('click', (e) => {
      L.popup()
        .setLatLng(e.latlng)
        .setContent('latitude: ' + e.latlng.lat + '<br>longitude: ' + e.latlng.lng)
        .openOn(this.map);
    });

  }

  autoCompletInit() {
    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => typeof value === 'string' ? value : value.name),
        map(name => name ? this._filter(name) : this.fruits.slice())
      );
  }

  displayFn(user?: any): string | undefined {
    return user ? user.name : undefined;
  }

  private _filter(name: string): any {
    const filterValue = name.toLowerCase();

    return this.fruits.filter(option => option.name.toLowerCase().indexOf(filterValue) === 0);
  }

  initSensorChecked() {
    for (const item of this.tabSensor) {
      const value = { sensor: item, selected: false };
      this.sensorChecked.push(value);
    }
  }

  visualiser() {
    console.log('je visualise')
  }

}
// export const  sensorCheckedOnMap: Sensor[] = [];
