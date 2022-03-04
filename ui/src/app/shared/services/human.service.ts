import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, Subscriber } from "rxjs";
import { DEFAULT_LAT, DEFAULT_LNG } from "src/app/constantes";

@Injectable()
export class HumanService {

  constructor(private http: HttpClient) {}

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

  getLocation() {
    return new Observable<Pos>(
      o => {
        if ("geolocation" in navigator) {
          navigator.geolocation.getCurrentPosition(
            pos => {
              console.log(`ACCURACY : ${pos.coords.accuracy}`);
              console.log(pos.coords.heading);
              o.next({
                lng: pos.coords.longitude,
                lat: pos.coords.latitude
              });
              o.complete();
            },
            err => {
              this.forwardToGetApproximateLocation(o);
            });
        } else {
          this.forwardToGetApproximateLocation(o);
        }
      }
    );
  }

  private forwardToGetApproximateLocation(o: Subscriber<Pos>) {
    return this.http.get<Pos>("/public/geo").subscribe(
      res => o.next(res),
      _ => o.next({lat: DEFAULT_LAT, lng: DEFAULT_LNG}),
      o.complete
    );
  }

  getApproximateLocation(): Observable<Pos> {
    return new Observable(o=>this.forwardToGetApproximateLocation(o));
  }
}
