import { ChangeDetectorRef, Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ViewMapComponent } from '../view-map/view-map.component';

@Component({
  selector: 'app-view-page',
  templateUrl: './view-page.component.html',
  styleUrls: ['./view-page.component.scss']
})
export class ViewPageComponent implements OnInit {
  id: number = NaN;
  state: "map" | "list" = "map";
  width: number;

  constructor(private route: ActivatedRoute, private d: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params["id"];
  }

  @ViewChild(ViewMapComponent) map: ViewMapComponent;

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
      this.width = window.innerWidth;
  }

  hasId() {
    return !Number.isNaN(this.id);
  }

  onMap() {
    this.state = "map";

    // map crash if use list than map
    this.d.detectChanges();
  }

  onList() {
    this.state = "list";
  }
}
