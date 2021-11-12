import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ParameterService } from '../services/parameter.service';
import { SensorService } from '../services/sensor.service';

@Component({
  selector: 'app-sensor-single',
  templateUrl: './sensor-single.component.html',
  styleUrls: ['./sensor-single.component.scss']
})
export class SensorSingleComponent implements OnInit {

  @Input() name: string;
  id: number;
  parameters: any[] = [];

  constructor(private sensorsService: SensorService,
              private parametersService: ParameterService,
              private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) { }

  ngOnInit(): void {
    this.id = +this.route.snapshot.params['id'];
    let sensor = this.sensorsService.getSensorsById(this.id);
    if (!sensor) {
      this.router.navigate(["/error404"]);
      return;
    }
    this.name = sensor.name;
    this.parametersService.getSensorParameters(this.id).subscribe(
        (res) => this.parameters = res,
        (err: any) => console.error(err)
    );
  }

  onDownloadCSV() {
    this.parametersService.dowloadSensorParameters(this.id, "csv");
  }

  onDownloadJSON() {
    this.parametersService.dowloadSensorParameters(this.id, "json");
  }
}
