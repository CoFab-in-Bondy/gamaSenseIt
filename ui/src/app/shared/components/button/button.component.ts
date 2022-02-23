import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';


@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {

  @Output() click = new EventEmitter<MouseEvent>();
  @Input() width = "80";
  @Input() url = "";
  @Input() type: "button"|"sumbit"|string = "button";

  constructor() { }

  ngOnInit(): void {
  }

}
