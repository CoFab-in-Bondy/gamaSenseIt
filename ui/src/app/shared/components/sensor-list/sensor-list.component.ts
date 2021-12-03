import { SensorService } from "../../services/sensor.service";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";

@Component({
  selector: "app-sensor-list",
  templateUrl: "./sensor-list.component.html",
  styleUrls: ["./sensor-list.component.scss"],
})
export class SensorListComponent implements OnInit, OnDestroy {
  private sensorsSubscription: Subscription;
  sensors: SensorCyclic[] = [];

  constructor(private sensorService: SensorService) {}

  ngOnInit(): void {
    this.sensorsSubscription = this.sensorService.observeAll().subscribe(
      (sensors) => {
        this.sensors = sensors;
      },
      (err) => console.error("An error occured " + err)
    );
    this.sensorService.lazyLoad();
  }

  ngOnDestroy(): void {
    this.sensorsSubscription?.unsubscribe();
  }
}
