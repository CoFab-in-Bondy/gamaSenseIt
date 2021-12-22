import { AfterViewInit, Component, Input, OnChanges, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource} from "@angular/material/table";
import { ActivatedRoute } from "@angular/router";
import { merge, Subscription } from "rxjs";
import { catchError, map, startWith, switchMap } from "rxjs/operators";
import { HumanService } from "../../services/human.service";
import { SensorService } from "../../services/sensor.service";
import { ApiService } from "../../services/api.service";


@Component({
  selector: "app-sensor-single",
  templateUrl: "./sensor-single.component.html",
  styleUrls: ["./sensor-single.component.scss"],
})
export class SensorSingleComponent implements OnDestroy, OnInit {
  id: number;
  sensor: SensorExtended|null = null;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = [];
  loading = true;
  failed = false;
  length = 0;

  constructor(
    private sensorService: SensorService,
    public humanService: HumanService,
    private api: ApiService,
    private route: ActivatedRoute
  ) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit(): void {
    console.log("Reloaded");
    console.log(`Sort : ${this.sort} - Page : ${this.paginator}`)

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page, this.route.params)
    .subscribe(_ => {
      this.loading = true;
      this.failed = false;

      this.id = +this.route.snapshot.params["id"];
      let index = this.displayedColumns.indexOf(this.sort.active);
      this.api.getSensorByIdExtended(this.id, {
        page: this.paginator.pageIndex || 0,
        count: this.paginator.pageSize || 50,
        sort: index == -1 ? 0 : index,
        asc: this.sort.direction != 'desc'
      }).toPromise().then(
        sensor => {
          this.sensor = sensor;

          let data = [];
          for (let record of sensor.parameters.values) {
            let row: any = {};
            for (let i = 0; i < sensor.parameters.metadata.width; i++) {
              let k = this.sensor.parameters.metadata.headers[i];
              let v = record[i];
              row[k] = v;
            }
            data.push(row);
          }
          this.dataSource.data = data;
          this.displayedColumns = this.sensor.parameters.metadata.headers;
          this.length = this.sensor.parameters.total;

          this.loading = false;
        }
      ).catch(
        err => {
          this.sensor = null;
          this.loading = false;
          this.failed = true;
          this.dataSource.data = [];
          this.displayedColumns = [];
        }
      )
    });
  }

  ngOnDestroy(): void {
    //this.sensorSub?.unsubscribe();
    // this.routeSub?.unsubscribe();
  }

  onDownloadCSV() {
    if (!this.sensor) return;
    this.sensorService.download({ sensorId: this.sensor.id, type: "csv" });
  }

  onDownloadJSON() {
    if (!this.sensor) return;
    this.sensorService.download({ sensorId: this.sensor.id, type: "json" });
  }

  format(index: number, value: string|number): string {
    if (this.sensor?.parameters.metadata.formats[index] === "DOUBLE")
      return Number(value).toExponential(3);
    if (value === null || value === undefined)
      return "";
    return value.toString();
  }

  type(index: number): "INTEGER"|"DOUBLE"|"STRING"|"DATE"|undefined {
    return this.sensor?.parameters.metadata.formats[index];
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

