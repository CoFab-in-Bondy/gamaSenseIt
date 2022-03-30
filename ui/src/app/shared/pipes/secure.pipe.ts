import { HttpClient } from "@angular/common/http";
import { Pipe, PipeTransform } from "@angular/core";
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";
import { Observable } from "rxjs";
import { DEFAULT_IMG } from "src/app/constantes";

@Pipe({
  name: "secure",
  pure: true,
})

export class SecurePipe implements PipeTransform {
  constructor(private http: HttpClient, private sanitizer: DomSanitizer) { }

  transform(value: string): Observable<SafeUrl> {
    return new Observable<SafeUrl>((o) => {
      o.next(DEFAULT_IMG);
      this.http
        .get(value, { responseType: "blob" })
        .subscribe(
          (res) => {
            const reader = new FileReader();
            reader.onloadend = () => {
              const url = <string>reader.result;
              o.next(this.sanitizer.bypassSecurityTrustUrl(url));
            };
            reader.readAsDataURL(res);
          },
          o.error,
          o.complete
        );
    });
  }
}
