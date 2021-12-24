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



const RANGE_COUNT = [25, 50, 100];
const ASC = true;
const DESC = false;

@Component({
  selector: "app-sensor-single",
  templateUrl: "./sensor-single.component.html",
  styleUrls: ["./sensor-single.component.scss"],
})
export class SensorSingleComponent implements OnInit, OnDestroy {
  id: number = 0;
  sensor: SensorExtended|null = null;

  loading = true;
  failed = false;

  sort = 0;
  asc = ASC;
  page = 0;
  count = RANGE_COUNT[0];

  scroll = 0;

  RANGE_COUNT = RANGE_COUNT;

  private routeSub: Subscription


  constructor(
    private sensorService: SensorService,
    public humanService: HumanService,
    private api: ApiService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      let id = +params["id"];
      if (this.id != id)
        this.default();
      this.id = id;
      this.onChange();
    })
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  default() {
    this.sort = 0;
    this.asc = ASC;
    this.page = 0;
    this.count = RANGE_COUNT[0];
  }

  onScroll(ev: Event) {
    if (!ev.target) return;
    this.scroll = Number((<any>ev).target.scrollLeft);
  }

  onSort(index: number ) {
    if (this.sort == index) {
      if (this.asc) {
        this.asc = DESC;
        this.sort = index;
      } else {
        this.sort = 0;
        this.asc = ASC;
      }
    } else {
      this.asc = true;
      this.sort = index;
    }
    console.log(`${this.sort} - ${this.asc}`);
    this.page = 0;
    this.onChange();
  }

  isSortBy(index: number) {
    return this.sort == index;
  }

  isAsc(index: number) {
    return this.isSortBy(index) && this.asc;
  }

  isDesc(index: number) {
    return this.isSortBy(index) && !this.asc;
  }

  isPage(page: number) {
    return this.page = page;
  }

  isValidPage(page: number) {
    return page >= 0 && (page) * this.count < (this.sensor?.parameters?.total || 0);
  }

  onCount(count: number) {
    if (this.count == count)
      return;
    this.page = Math.floor(this.page * this.count / count);
    this.count = count;
    this.onChange();
  }

  onPage(dif: number) {
    if (!this.isValidPage(this.page + dif))
      return;
    this.page += dif;
    this.onChange();
  }

  onChange() {
    this.loading = true;
    this.failed = false;

    this.api.getSensorByIdExtended(this.id, {
      page: this.page,
      count: this.count,
      sort: this.sort,
      asc: this.asc
    })
    .toPromise()
    .then(
      sensor => {
        this.sensor = sensor;
        this.loading = false;
      }
    ).catch(
      err => {
        this.sensor = null;
        this.loading = false;
        this.failed = true;
      }
    )
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

  classOf(index: number): string {
    switch (this.sensor?.parameters.metadata.formats[index]) {
      case "DATE":
        return "col-data col-date";
      case "DOUBLE":
        return "col-data col-double";
      case "INTEGER":
        return "col-data col-integer";
      case "STRING":
        return "col-data col-string";
      default:
        return "col-data col-other";
    }
  }

  coordLink() {
    let base = "https://www.google.com/maps/search/?api=1";
    if (!this.sensor)
      return base;
    return `${base}&query=${this.sensor.latitude},${this.sensor.longitude}`;
  }
}

