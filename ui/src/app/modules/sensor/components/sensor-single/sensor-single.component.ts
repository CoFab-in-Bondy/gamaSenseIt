import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";
import { ParameterService } from "../../../../shared/services/parameter.service";
import { SensorService } from "../../../../shared/services/sensor.service";

@Component({
  selector: "app-sensor-single",
  templateUrl: "./sensor-single.component.html",
  styleUrls: ["./sensor-single.component.scss"],
})
export class SensorSingleComponent implements OnDestroy, OnInit {
  @Input() id: number;
  private parametersSub: Subscription;
  private sensorSub: Subscription;
  sensor: SensorCyclic;
  parameters: Parameter[] = [];

  constructor(
    private parameterService: ParameterService,
    private sensorService: SensorService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.sensorSub = this.sensorService.observeBySensorId(this.id).subscribe(
      (sensor) => {
        if (sensor === undefined) {
          this.router.navigate(["/"]);
          return;
        }

        this.sensor = sensor;
        let observable = this.parameterService.observeBySensorId(
          this.sensor.id
        );
        if (observable === undefined) {
          this.router.navigate(["/"]);
          return;
        }

        this.parametersSub = observable.subscribe(
          (parameters) => (this.parameters = parameters)
        );
        this.parameterService.lazyLoadBySensorId(this.sensor.id);
      },
      (err) => console.error(err)
    );

    this.sensorService.lazyLoad();
  }

  metadataOf(parameter: Parameter): ParameterMetadataCyclic | undefined {
    return this.sensor.sensorMetadata.parametersMetadata.find(
      (p) => p.id === parameter.parameterMetadataId
    );
  }

  onDownloadCSV() {
    this.parameterService.download({ sensorId: this.sensor.id, type: "csv" });
  }

  onDownloadJSON() {
    this.parameterService.download({ sensorId: this.sensor.id, type: "json" });
  }

  ngOnDestroy(): void {
    this.sensorSub?.unsubscribe();
    this.parametersSub?.unsubscribe();
  }
}
