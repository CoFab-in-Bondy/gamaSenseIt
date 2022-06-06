import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {BehaviorSubject, Observable, ObservableInput, throwError} from 'rxjs';
import {AuthService} from '@services/auth.service';
import {catchError, filter, switchMap, take} from "rxjs/operators";
import {Router} from "@angular/router";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private auth: AuthService, private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<Object>> {
    return next.handle(req).pipe(catchError(error => {
      if (
        error instanceof HttpErrorResponse &&
        !req.url.includes("auth/login") &&
        !req.url.includes("auth/refresh") &&
        !req.url.includes("auth/logout") &&
        error.status === 401
      ) {
        console.log("Handle refresh token outer");
        return this.handle401Error(req, next);
      }
      return throwError(error);
    }));
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): ObservableInput<any> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(false);
      console.log("Handle refresh token inner");
      return this.auth.refresh().pipe(
        switchMap((body: any) => {
          this.isRefreshing = false;
          console.log("Success refresh");
          this.refreshTokenSubject.next(true);
          return next.handle(request);
        }),
        catchError((err) => {
          this.isRefreshing = false;
          this.auth.logout().subscribe(
            _ => {
              this.router.navigate(["/login"]);
            },
            err => console.error(err)
          );
          console.log("Failed refresh");
          return throwError(err);
        })
      );
    }

    // Redo request after refresh is finish
    console.log("Already refreshing");
    return this.refreshTokenSubject.pipe(
      filter(isRefresh => isRefresh),
      take(1),
      switchMap(_ => next.handle(request))
    );
  }
}
