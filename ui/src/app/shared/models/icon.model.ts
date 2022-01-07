export class Icon {

  readonly url: string;
  readonly width: number|null;
  readonly height: number|null;

  constructor(url: string, width: number|null = null, heigth: number|null = null) {
    this.url = url;
    this.width = width;
    this.height = heigth;
  }
}
