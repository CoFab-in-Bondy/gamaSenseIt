import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '@services/api.service';
import { SensorService } from '@services/sensor.service';
import * as L from "leaflet";
import { CLICK_MARKER } from '@models/icon.model';
import { HumanService } from '@services/human.service';

@Component({
  selector: 'app-sensor-form',
  templateUrl: './sensor-form.component.html',
  styleUrls: ['./sensor-form.component.scss']
})
export class SensorFormComponent implements OnInit {
  sensorForm: FormGroup;
  sensorsMetadata: SensorMetadataExtended[] = [];
  marker: L.Marker;
  previewUrl: string = '';
  private map: L.Map;

  constructor(private formBuilder: FormBuilder,
              private apiService: ApiService,
              private sensorService: SensorService,
              private humanService: HumanService,
              private router: Router) { }

  onSelectFile(event: any) {
    console.log(event);
    if (event.target.files && event.target.files[0]) {
      var reader = new FileReader();

      reader.readAsDataURL(event.target.files[0]); // read file as data url

      reader.onload = (event) => { // called once readAsDataURL is completed
        this.previewUrl = event.target?.result?.toString() || '';
      }
    }
  }

  ngOnInit(): void {
    this.initForm();
    this.humanService.getApproximateLocation().subscribe(
      res=>this.initMap(res)
    );
  }

  initMap({lat, lng}: {lat: number, lng: number}) {
    this.map = L.map("map-sensor-form", {
      center: [lat, lng],
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
    this.map.on('click', (event: any) => {
      const { lat, lng } = event.latlng;
      this.mark({lat: Math.round(lat * 100000) / 100000, lng: Math.round(lng * 100000) / 100000})
    });
  }

  mark({lat, lng}: {lat: number, lng: number}) {
    if (this.marker)
      this.map.removeLayer(this.marker);
    this.marker = L.marker({lat, lng}, { icon: CLICK_MARKER });
    this.marker.addTo(this.map);
    this.sensorForm.controls['latitude'].setValue(lat);
    this.sensorForm.controls['longitude'].setValue(lng);

  }

  initForm() {
    this.sensorsMetadata = [];
    this.apiService.getSensorsMetadataExtended().subscribe(
      res=>{
        res.forEach(smd => this.sensorsMetadata.push(smd));
      },
      console.error
    );
    this.sensorForm = this.formBuilder.group({
      sensorMetadataId: ['', Validators.required],
      name: ['', Validators.required],
      displayName: ['', Validators.required],
      subDisplayName: ['', Validators.required],
      hiddenMessage: ['', Validators.required],
      isHidden: [''],
      latitude: ['', Validators.required],
      longitude: ['', Validators.required]
    });
  }

  onSubmitForm() {
    const form = Object.assign({}, this.sensorForm.value);
    this.sensorService.addSensor({
      sensorMetadataId: +form['sensorMetadataId'],
      name: form['name'],
      displayName: form['displayName'],
      subDisplayName: form['subDisplayName'],
      longitude: form['longitude'],
      latitude: form['latitude'],
      hiddenMessage: form['hiddenMessage'],
      isHidden: !!form['isHidden']
    }).subscribe(
      res=>{
        this.router.navigate(["/sensors", res.id]);
      },
      err=>{
        if (err.status == 404)
          this.sensorForm.controls['sensorMetadataId'].setErrors({'incorrect': true});
        if (err.status == 409)
          this.sensorForm.controls["name"].setErrors({'incorrect': true});
        console.error(err);
      }
    );
  }

  onPosition() {
    this.humanService.getLocation().subscribe(latlng => {
      this.map.flyTo(latlng, 14, {
        animate: true,
        duration: 1
      });
      this.mark(latlng);
    });
  }
}
