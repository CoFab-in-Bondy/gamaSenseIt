import { AfterContentInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ContentChildren, ElementRef, EventEmitter, forwardRef, HostListener, Input, OnChanges, OnInit, Output, QueryList, SimpleChanges, ViewChild } from '@angular/core';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class DialogComponent implements AfterContentInit {

  constructor() {}

  public displayed = false;

  @ViewChild('dialogBg') bg: ElementRef;

  @ContentChildren('close', { descendants: true }) closeElements: QueryList<ElementRef>;

  ngAfterContentInit(): void {
    this.closeElements.forEach(element => element.nativeElement.onclick = () => this.onClose());
  }

  @HostListener('document:click', ['$event'])
  onOut(event: PointerEvent) {
    if (this.bg.nativeElement == event.target) {
      this.onClose();
    }
  }

  onOpen() {
    this.displayed = true;
  }

  onClose() {
    this.displayed = false;
  }

}
