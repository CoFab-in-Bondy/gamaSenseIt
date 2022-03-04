import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Subscription } from "rxjs";
import { HumanService } from "@services/human.service";
import { SensorService } from "@services/sensor.service";
import { ApiService } from "@services/api.service";
import { DataTableComponent } from "@components/data-table/data-table.component";
import { Icon } from "@models/icon.model";


const widths = {
  "INTEGER": 150,
  "DOUBLE": 150,
  "STRING": 300,
  "DATE": 150
}

@Component({
  selector: "app-view-single",
  templateUrl: "./view-single.component.html",
  styleUrls: ["./view-single.component.scss"],
})
export class ViewSingleComponent implements OnInit, OnDestroy {
  id: number = 0;
  sensor: SensorExtended|null = null;
  records: string[][];

  private routeSub: Subscription;

  constructor(
    private sensorService: SensorService,
    public humanService: HumanService,
    private api: ApiService,
    private route: ActivatedRoute
  ) {}

  @ViewChild(DataTableComponent, {static: true})
  public tb: DataTableComponent;

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      let id = +params["id"];
      this.id = id;
      this.tb.onChange();
    })
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  onChange(info: DataTableEvent) {
    this.api.getSensorByIdExtended(this.id, info)
    .toPromise()
    .then(
      sensor => {
        this.sensor = sensor;
        this.records = []
        for (let record of this.sensor.parameters.values) {
          let sRecord = [];
          for (let v of record)
            sRecord.push(v.toString());
          this.records.push(sRecord);
        }
      }
    ).catch(
      err => this.sensor = null
    )
  }

  sizes() {
    let width = this.sensor?.parameters.metadata.width || 0;
    let type = this.sensor?.parameters.metadata.formats || [];
    let arr = [];
    for (let i = 0; i < width; i++) {
      arr.push(widths[type[i]]);
    }
    return arr;
  }

  onDownloadCSV() {
    if (!this.sensor) return;
    this.sensorService
      .download({ sensorId: this.sensor.id, type: "csv" })
      .subscribe(()=>{}, console.error);
  }

  onDownloadJSON() {
    if (!this.sensor) return;
    this.sensorService
      .download({ sensorId: this.sensor.id, type: "json" })
      .subscribe(()=>{}, console.error);
  }

  format(index: number, value: string|number): string|Icon {
    if (this.type(index) === "DOUBLE")
      return Number(value).toExponential(3);
    if (value === null || value === undefined)
      return "";
    return value.toString();
  }

  type(index: number): "INTEGER"|"DOUBLE"|"STRING"|"DATE"|undefined {
    return this.sensor?.parameters.metadata.formats[index];
  }

  coordLink() {
    let base = "https://www.google.com/maps/search/?api=1";
    if (!this.sensor)
      return base;
    return `${base}&query=${this.sensor.latitude},${this.sensor.longitude}`;
  }
}

