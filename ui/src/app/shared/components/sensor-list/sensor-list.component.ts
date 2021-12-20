import { SensorMetadataService } from "../../services/sensorMetadata.service";
import { Component, OnDestroy, OnInit, Output, EventEmitter, ElementRef } from "@angular/core";
import { Subscription } from "rxjs";
import { HumanService } from "../../services/human.service";

@Component({
  selector: "app-sensor-list",
  templateUrl: "./sensor-list.component.html",
  styleUrls: ["./sensor-list.component.scss"],
})
export class SensorListComponent implements OnInit, OnDestroy {
  private sensorsSubscription: Subscription;
  private sensorsMetadata: SensorMetadataExtended[] = [];

  @Output() selected = new EventEmitter<number>();

  constructor(
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService
  ) {}

  ngOnInit(): void {
    this.sensorsSubscription = this.sensorMetadataService.observeAll().subscribe(
      sensors => {
        this.sensorsMetadata = sensors;
      },
      err => console.error(err)
    );
    this.sensorMetadataService.lazyLoad();
  }

  ngOnDestroy(): void {
    this.sensorsSubscription?.unsubscribe();
  }

  zipSensorsWithSensorsMetadata(): [Sensor, SensorMetadataExtended][] {
    let zipped: [Sensor, SensorMetadataExtended][] = [];
    for (let sensorMetadata of this.sensorsMetadata)
      for (let sensor of sensorMetadata.sensors)
        zipped.push([sensor, sensorMetadata]);
    return zipped;
  }
}
