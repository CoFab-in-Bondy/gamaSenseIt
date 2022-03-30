import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';


@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {

  @Output() click = new EventEmitter<MouseEvent>();
  @Input() width = "80";
  @Input() url = "";
  @Input() fa = "";
  @Input() type: "button"|"sumbit"|string = "button";
  @Input() href?: string|null|SafeUrl = undefined;
  @Input() blank: boolean|string = false;

  constructor() { }

  ngOnInit(): void {
  }

  @HostListener('click', ['$event']) onClick(event: MouseEvent) {
  }


}
