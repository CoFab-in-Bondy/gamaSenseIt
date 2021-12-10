import { Component, Input, OnChanges, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { SensorService } from "../../services/sensor.service";
import { SensorMetadataService } from "../../services/sensorMetadata.service";

@Component({
  selector: "app-sensor-single",
  templateUrl: "./sensor-single.component.html",
  styleUrls: ["./sensor-single.component.scss"],
})
export class SensorSingleComponent implements OnDestroy, OnChanges, OnInit {
  @Input() id: number;
  private parametersSub: Subscription;
  private sensorSub: Subscription;
  sensor: SensorExtended|null = null;

  constructor(
    private sensorMetadataService: SensorMetadataService,
    private sensorService: SensorService
  ) {}

  ngOnInit(): void {
    this.ngOnChanges();
  }


  ngOnChanges(): void {
    this.ngOnDestroy();
    this.sensorSub = this.sensorService.observeById(this.id).subscribe(
      sensor => {
        this.sensor = sensor;
      },
      err => {
        console.error(err);
      }
    );

    this.sensorService.lazyLoadById(this.id);
  }

  /*
  metadataOf(parameter: Parameter): ParameterMetadataCyclic | undefined {
    return this.sensor?.sensorMetadata.parametersMetadata.find(
      (p) => p.id === parameter.parameterMetadataId
    );
  }*/

  onDownloadCSV() {
    if (!this.sensor) return;
    this.sensorService.download({ sensorId: this.sensor.id, type: "csv" });
  }

  onDownloadJSON() {
    if (!this.sensor) return;
    this.sensorService.download({ sensorId: this.sensor.id, type: "json" });
  }

  ngOnDestroy(): void {
    this.sensorSub?.unsubscribe();
    this.parametersSub?.unsubscribe();
  }
}
