import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from "@angular/core";

enum SortEnum {
  SERVER = 2,
  CLIENT = 1,
  NO = 0,
}

enum PageEnum {
  SERVER = 2,
  CLIENT = 1,
  NO = 0,
}


@Component({
  selector: "app-data-table",
  templateUrl: "./data-table.component.html",
  styleUrls: ["./data-table.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DataTableComponent<D> {
  SortEnum = SortEnum;
  PageEnum = PageEnum;

  isIcon(value: any): value is Icon {
    return value.url != undefined && value.width != undefined && value.height != undefined;
  }

  /* perfom cast in template */
  any(v: any): any {
    return v;
  }

  @Input() height: number = 250;

  @Input() length: number | undefined = undefined;
  @Input() scales = [25, 50, 100];

  @Input() sortable: "SERVER" | "CLIENT" | "NO" = "NO";
  @Input() pagable: "SERVER" | "CLIENT" | "NO" = "NO";

  @Input() minSizes: number[] = [];
  @Input() headers: string[] = [];
  @Input() data: D[] = [];
  @Input() formater: (d: D) => DTValue[];
  @Input() linker: (d: D) => (string|number|null)[];
  @Input() empty = "Aucune données";

  @Output() navigate = new EventEmitter<DataTableNatigateEvent>();
  @Output() select = new EventEmitter<D>();

  loading = true;
  failed = false;
  scroll = 0;

  sort = 0;
  asc = true;
  page = 0;
  count = 0;

  constructor() {
    this.count = this.scales[0];
  }

  default() {
    this.sort = 0;
    this.asc = true;
    this.page = 0;
    this.count = this.scales[0];
  }

  onScroll(ev: Event) {
    if (!ev.target) return;
    this.scroll = Number((<any>ev).target.scrollLeft);
  }

  onSelect(d: D) {
    this.select.emit(d);
  }

  onSort(index: number) {
    if (this.sort == index) {
      if (this.asc) {
        this.asc = false;
        this.sort = index;
      } else {
        this.sort = 0;
        this.asc = true;
      }
    } else {
      this.asc = true;
      this.sort = index;
    }
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
    return (this.page = page);
  }

  isValidPage(page: number) {
    return page >= 0 && page * this.count < (this.length || 0);
  }

  onCount(count: number) {
    if (this.count == count) return;
    this.page = Math.floor((this.page * this.count) / count);
    this.count = count;
    this.onChange();
  }

  onPage(dif: number) {
    if (!this.isValidPage(this.page + dif)) return;
    this.page += dif;
    this.onChange();
  }

  onChange() {
    this.navigate.emit({
      sort: this.sort,
      asc: this.asc,
      page: this.page,
      count: this.count,
    });
  }

  format(value: any): string | Icon {
    if (value === null || value === undefined || value === NaN)
      return "";
    if (this.isIcon(value))
      return value;
    return value.toString();
  }
}
