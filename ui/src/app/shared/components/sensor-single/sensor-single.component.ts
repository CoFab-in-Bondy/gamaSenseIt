import { Component, Input, OnChanges, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { HumanService } from "../../services/human.service";
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
  sensor: SensorExtended|null = null;

  constructor(
    private sensorService: SensorService,
    public humanService: HumanService
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

  format(index: number, value: string|number): string {
    if (this.sensor?.parameters.metadata.formats[index] === "DOUBLE")
      return Number(value).toExponential(3);
    return value.toString();
  }

  sizeNeeded(index: number): number {
    switch (this.sensor?.parameters.metadata.formats[index]) {
      case "DATE":
        return 1;
      case "DOUBLE":
        return 1;
      case "INTEGER":
        return 1
      case "STRING":
        return 3;
      default:
        return 3;
    }
  }
}
