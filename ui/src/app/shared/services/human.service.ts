import { Injectable } from "@angular/core";

@Injectable()
export class HumanService {

  constructor() {}

  coordsToHumain(lat: number, long: number, precision = 3) {
    return this.coord(lat, "N", "S", precision) + " " + this.coord(long, "E", "W", precision);
  }

  private coord(value: number, positive: string, negative: string, precision = 3): string {

    let direction = value < 0 ? negative : positive;
    let tmp = Math.abs(value);

    let degrees = Math.trunc(tmp);
    let humanReadable = ` ${degrees}°`;
    if (precision >= 2) {

      tmp = (tmp - degrees) * 60;
      let minutes = Math.trunc(tmp);
      humanReadable += ` ${minutes}'`;

      if (precision >= 3) {
        tmp = (tmp - minutes) * 60;
        let seconds = Math.trunc(tmp);
        humanReadable += ` ${seconds}"`;
      }
    }
    humanReadable += `${direction}`;
    return humanReadable;
  }
}
