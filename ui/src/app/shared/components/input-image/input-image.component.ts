import { Component, ElementRef, EventEmitter, Input, OnInit, Output, SecurityContext, SimpleChanges, ViewChild } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-input-image',
  templateUrl: './input-image.component.html',
  styleUrls: ['./input-image.component.scss']
})
export class InputImageComponent implements OnInit {

  @Input()
  default: SafeUrl;

  defaultSrc: SafeUrl;

  @Output()
  image = new EventEmitter<File|undefined>();


  imageSrc?: SafeUrl;

  @Input()
  name: string = "image";

  @Input()
  editable: boolean;

  width = 0;
  height = 0;

  @ViewChild("imageRef", {static: true})
  public imageRef: ElementRef;

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.onImageReset()
  }

  onImagePreview(): void {
    if (this.imageRef == undefined) return;
    const files = this.imageRef.nativeElement.files || [];
    console.log(files);
    if (files && files[0]) {
      const file = files[0];
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        const url = <string>reader.result;
        this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(url);
      }
      this.image.emit(file);
    } else {
      this.image.emit(undefined);
    }
  }

  onLoad(event: any) {
    this.width = event.target.naturalWidth || 0;
    this.height = event.target.naturalHeight || 0;
  }

  onDownload() {
    if (this.imageSrc) {
      const url = this.sanitizer.sanitize(SecurityContext.URL, this.imageSrc);
      if (url) {
        saveAs(url, "photo.png");
      }
    }
  }

  onImageRemove(): void {
    if (this.imageRef == undefined) return;
    this.imageRef.nativeElement.value = "";
    this.imageSrc = undefined;
  }

  onImageReset(): void {
    if (this.imageRef == undefined) return;
    this.imageRef.nativeElement.value = "";
    this.imageSrc = this.default;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['default']) {
      if (this.imageSrc == changes.default.previousValue || this.imageSrc == undefined) {
        this.imageSrc = changes.default.currentValue;
      }
    }
  }

  get url() {
    return this.imageSrc? this.sanitizer.sanitize(SecurityContext.URL, this.imageSrc) || undefined: undefined;
  }
}
