import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TriState, TriEvent } from '@models/tributton';


@Component({
  selector: 'app-tributton',
  templateUrl: './tributton.component.html',
  styleUrls: ['./tributton.component.scss']
})
export class TributtonComponent implements OnInit {

  @Input() color: RGB = "rgb(0, 255, 0)";
  @Input() left = '';
  @Input() right = '';
  @Output() swap = new EventEmitter<TriEvent>();
  state: TriState = TriState.MIDDLE;
  TriState = TriState;


  constructor() { }

  ngOnInit(): void {
  }

  onClick() {
    this.swap.emit(new TriEvent(this.state));
  }

  onLeftClick(event: MouseEvent) {
    if (this.state == TriState.LEFT) {
      this.onMiddleClick(event);
    } else {
      event.preventDefault();
      this.state = TriState.LEFT;
      this.onClick();
    }
  }

  onMiddleClick(event: MouseEvent) {
    if (this.state == TriState.MIDDLE) return;
    event.preventDefault();
    this.state = TriState.MIDDLE;
    this.onClick();
  }

  onRightClick(event: MouseEvent) {
    if (this.state == TriState.RIGHT) {
      this.onMiddleClick(event);
    } else {
      event.preventDefault();
      this.state = TriState.RIGHT;
      this.onClick();
    }
  }
}
