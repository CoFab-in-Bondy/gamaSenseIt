import { Component, OnInit, Input } from '@angular/core';

import { NgCircleProgressModule } from 'ng-circle-progress';

@Component({
  selector: 'pm-progress-bar',
  templateUrl: './pm-progress-bar.component.html',
  styleUrls: ['./pm-progress-bar.component.scss']
})
export class PmProgressBarComponent  implements OnInit {

  DANGER_COLOR: string = "#C21A00";//"#f5365c";
  WARNING_COLOR: string = "#FF6D58";
  OK_COLOR: string = "#00C269";//"#B3D158";//"#2dce89";

  @Input()
  warmLevel: number = 0;
  @Input()
  dangerLevel: number = 0;
  @Input()
  maxLevel: number = 0;
  @Input()
  title: string = "";
  @Input()
  value: number;

  constructor() {
    this.title = "PM ?";
    this.warmLevel = 50;
    this.dangerLevel = 75;
    this.maxLevel = 100;
    this.DANGER_COLOR = "#C21A00";//"#f5365c";
    this.WARNING_COLOR = "#FF6D58";
    this.OK_COLOR = "#00C269"; //"#B3D158"; //"#2dce89";
   }

   getColor(): string {
    if(this.value < this.warmLevel) {
     return this.OK_COLOR ;
    } else if(this.value >= this.warmLevel && this.value < this.dangerLevel) {
      return this.WARNING_COLOR ;
    } else {
      return this.DANGER_COLOR ;
    }
  }

  formatTitle(percent: number): string {
    if(percent >= 75) {
      return "Bad";
    } else if (percent > 50) {
      return "Medium";
    } else {
      return "Good";
    }
  }

  ngOnInit() {
   // NgCircleProgressModule.forRoot({});
  }
}
