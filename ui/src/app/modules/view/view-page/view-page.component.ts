import { ViewportScroller } from "@angular/common";
import {
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit,
  ViewChild,
} from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { AuthService } from "@services/auth.service";
import { ViewMapComponent } from "../view-map/view-map.component";

@Component({
  selector: "app-view-page",
  templateUrl: "./view-page.component.html",
  styleUrls: ["./view-page.component.scss"],
})
export class ViewPageComponent implements OnInit {
  id: number = NaN;
  state: "map" | "list" = "map";
  width: number;

  constructor(
    private route: ActivatedRoute,
    private ref: ChangeDetectorRef,
    private scroller: ViewportScroller,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params["id"];
  }

  @ViewChild(ViewMapComponent) map: ViewMapComponent;

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
}
