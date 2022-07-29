import {Injectable} from "@angular/core";
import {DEFAULT_CENTER, MD} from "../../constantes";
import {LatLng, LatLngLiteral} from "leaflet";
import {StorageModel} from "@models/storage.model";


function isLargeScreen() {
  return window.innerWidth >= MD;
}

@Injectable()
export class StateService {
  private openned: boolean = isLargeScreen();
  readonly lastViewLngLat = new StorageModel('lastViewLngLat', {center: DEFAULT_CENTER, zoom: 3});

  isLargeScreen() {
    return isLargeScreen();
  }

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

  getHeight() {
    const h = window.innerHeight;
    const w = window.innerWidth;
    if (w < 400)
      return h - 80
    if (w  < 576)
      return h - 100
    return h - 145
  }

}
