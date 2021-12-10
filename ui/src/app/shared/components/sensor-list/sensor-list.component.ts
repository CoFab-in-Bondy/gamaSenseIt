import { SensorMetadataService } from "../../services/sensorMetadata.service";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";

@Component({
  selector: "app-sensor-list",
  templateUrl: "./sensor-list.component.html",
  styleUrls: ["./sensor-list.component.scss"],
})
export class SensorListComponent implements OnInit, OnDestroy {
  private sensorsSubscription: Subscription;
  sensorsMetadata: SensorMetadataExtended[] = [];

  constructor(private sensorMetadataService: SensorMetadataService) {}

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
}
