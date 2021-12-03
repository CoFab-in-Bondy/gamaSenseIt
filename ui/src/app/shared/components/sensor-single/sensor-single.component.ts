import { Component, Input, OnChanges, OnDestroy, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";
import { ParameterService } from "../../services/parameter.service";
import { SensorService } from "../../services/sensor.service";

@Component({
  selector: "app-sensor-single",
  templateUrl: "./sensor-single.component.html",
  styleUrls: ["./sensor-single.component.scss"],
})
export class SensorSingleComponent implements OnDestroy, OnChanges, OnInit {
  @Input() id: number;
  private parametersSub: Subscription;
  private sensorSub: Subscription;
  sensor: SensorCyclic|null = null;
  parameters: Parameter[] = [];

  constructor(
    private parameterService: ParameterService,
    private sensorService: SensorService
  ) {}

  ngOnInit(): void {
    this.ngOnChanges();
  }


  ngOnChanges(): void {
    this.ngOnDestroy();
    this.sensorSub = this.sensorService.observeBySensorId(this.id).subscribe(
      (sensor) => {
        if (sensor === undefined) {
          this.sensor = null;
          this.parameters = [];
          return;
        }

        this.sensor = sensor;
        let observable = this.parameterService.observeBySensorId(
          this.sensor.id
        );
        if (observable === undefined) {
          this.sensor = null;
          this.parameters = [];
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
    return this.sensor?.sensorMetadata.parametersMetadata.find(
      (p) => p.id === parameter.parameterMetadataId
    );
  }

  onDownloadCSV() {
    if (!this.sensor) return;
    this.parameterService.download({ sensorId: this.sensor.id, type: "csv" });
  }

  onDownloadJSON() {
    if (!this.sensor) return;
    this.parameterService.download({ sensorId: this.sensor.id, type: "json" });
  }

  ngOnDestroy(): void {
    this.sensorSub?.unsubscribe();
    this.parametersSub?.unsubscribe();
  }
}
