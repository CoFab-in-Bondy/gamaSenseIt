import {
  AfterContentInit,
  ChangeDetectionStrategy,
  Component,
  ContentChildren,
  ElementRef,
  HostListener,
  QueryList,
  ViewChild,
} from "@angular/core";
import {StateService} from "@services/state.service";

@Component({
  selector: "app-dialog",
  templateUrl: "./dialog.component.html",
  styleUrls: ["./dialog.component.scss"],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class DialogComponent implements AfterContentInit {
  constructor(public state: StateService) {}

  public displayed = false;

  @ViewChild("dialogBg") bg: ElementRef;

  @ContentChildren("close", { descendants: true })
  closeElements: QueryList<ElementRef>;

  ngAfterContentInit(): void {
    if (this.closeElements == undefined) return;
    const close = () => this.onClose();
    this.closeElements.forEach(e => {
      if (!e.nativeElement) return;
      e.nativeElement.onclick = close;
    });
  }

  @HostListener("document:click", ["$event"])
  onOut(event: PointerEvent) {
    if (this.bg.nativeElement && this.bg.nativeElement == event.target) {
      this.onClose();
    }
  }

  onOpen() {
    console.log("Open dialog");
    this.displayed = true;
  }

  onClose() {
    console.log("Close dialog");
    this.displayed = false;
  }
}
