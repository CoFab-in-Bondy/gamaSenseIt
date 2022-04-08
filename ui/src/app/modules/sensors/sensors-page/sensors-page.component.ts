import { ViewportScroller } from "@angular/common";
import {
  AfterContentInit,
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit,
  ViewChild,
} from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { AuthService } from "@services/auth.service";
import { SensorsMapComponent } from "../sensors-map/sensors-map.component";
import {MD, SM} from "../../../constantes";

@Component({
  selector: "app-sensors-page",
  templateUrl: "./sensors-page.component.html",
  styleUrls: ["./sensors-page.component.scss"],
})
export class SensorsPageComponent implements OnInit {
  id: number = NaN;
  state: "map" | "list" = "map";
  width: number;
  init: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private ref: ChangeDetectorRef,
    private scroller: ViewportScroller,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params["id"];
  }

  @ViewChild(SensorsMapComponent) map: SensorsMapComponent;

  @HostListener("window:resize", ["$event"])
  onResize(event: any) {
    this.width = window.innerWidth;
  }

  select(id: number) {
    // TODO: fix delay
    setTimeout(() => {
      this.scroller.scrollToAnchor("sensorSingleRef");
    }, 200);
  }

  hasId() {
    return !Number.isNaN(this.id);
  }

  onMap() {
    this.state = "map";

    // map crash if use list than map
    this.ref.detectChanges();
  }

  onList() {
    this.state = "list";
  }

  getSize() {
    return window.innerWidth >= SM? (window.innerHeight - 200): (window.innerHeight - 240);
  }
}
