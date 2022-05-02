import {Injectable} from "@angular/core";
import {MD} from "../../constantes";


function isLargeScreen() {
  return window.innerWidth >= MD;
}

@Injectable()
export class StateService {
  private openned: boolean = isLargeScreen();

  isLargeScreen() {
    return isLargeScreen();
  }

  constructor() { }

  widthNav() {
    return this.isOpen()? 170: 49;
  }

  refreshMargin() {
    (<any>document).querySelector('.mat-drawer-content').style.marginLeft = this.widthNav() + 'px';
  }

  isOpen() {
    return this.openned && isLargeScreen();
  }

  swapOpen() {
    if (isLargeScreen()) {
      this.openned = !this.openned;
      this.refreshMargin();
    }
  }

}
