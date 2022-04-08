import { EventEmitter, Injectable } from "@angular/core";


@Injectable()
export class ErrorService {
  private title: string = '';
  private message: string = '';
  private status: number|null = null;
  readonly errors = new EventEmitter<ErrorValue>();

  constructor() {

  }

  setError(title?: string, message?: string, status?: number) {
    this.title = title || '';
    this.message = message || '';
    this.status = status || null;
    this.errors.emit(this.getError());
  }

  getError() {
    return {
      title: this.title,
      message: this.message,
      status: this.status
    }
  }

  getTitle(): string {
    return this.title;
  }

  getMessage(): string {
    return this.message;
  }

  getStatus(): number|null {
    return this.status;
  }

}
