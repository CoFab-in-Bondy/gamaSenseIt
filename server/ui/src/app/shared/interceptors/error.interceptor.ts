import { ErrorHandler, Injectable } from '@angular/core';
import {
  HttpErrorResponse
} from '@angular/common/http';
import { ErrorService } from '@services/error.service';

@Injectable()
export class ErrorInterceptor implements ErrorHandler {

  constructor(private error: ErrorService) {}

  handleError(error: any) {
    if (error?.message?.startsWith("NG0100: ExpressionChangedAfterItHasBeenCheckedError: Expression has changed after it was checked.")) {
      console.warn(error);
      return;
    }
    console.error(error);

    if (error instanceof TypeError) {

    } else if (!(error instanceof HttpErrorResponse)) {
      error = error.rejection; // get the error object
    }
    this.error.setError(error?.title || error?.name || 'Unknow Error', error?.message, error?.status);
  }

}
