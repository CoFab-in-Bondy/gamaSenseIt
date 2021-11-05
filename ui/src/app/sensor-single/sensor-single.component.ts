import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SensorService } from '../services/sensor.service';

@Component({
  selector: 'app-sensor-single',
  templateUrl: './sensor-single.component.html',
  styleUrls: ['./sensor-single.component.scss']
})
export class SensorSingleComponent implements OnInit {

  @Input() name: string;

  constructor(private sensorsService: SensorService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    let sensor = this.sensorsService.getSensorsById(+id);
    if (!sensor) {
      this.router.navigate(["/error404"]);
      return;
    }
    this.name = sensor.name;
  }
}
