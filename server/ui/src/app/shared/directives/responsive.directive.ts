import {Directive, ElementRef, HostListener, Input, OnInit} from '@angular/core';
import {StateService} from "@services/state.service";

@Directive({
  selector: '[appResponsive]'
})
export class ResponsiveDirective implements OnInit {

  @Input() gap: number = 0;

  constructor(private el: ElementRef, private stateService: StateService) {
  }

  @HostListener('window:resize')
  onResize() {
    this.el.nativeElement.style.height = this.stateService.getHeight() + this.gap + 'px';
  }

  ngOnInit(): void {
    this.onResize();
  }

}
