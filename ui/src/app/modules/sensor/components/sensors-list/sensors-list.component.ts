import { SensorService } from "../../../../shared/services/sensor.service";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";

@Component({
  selector: "app-sensors-list",
  templateUrl: "./sensors-list.component.html",
  styleUrls: ["./sensors-list.component.scss"],
})
export class SensorsListComponent implements OnInit, OnDestroy {
  private sensorsSubscription: Subscription;
  sensors: SensorCyclic[] = [];

  constructor(private sensorService: SensorService) {}

  ngOnInit(): void {
    this.sensorsSubscription = this.sensorService.observeAll().subscribe(
      (sensors) => {
        this.sensors = sensors;
      },
      (err) => console.error(err)
    );
    this.sensorService.lazyLoad();
  }

  ngOnDestroy(): void {
    this.sensorsSubscription?.unsubscribe();
  }
}
