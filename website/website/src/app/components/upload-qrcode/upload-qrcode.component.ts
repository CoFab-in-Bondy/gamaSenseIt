import { Component, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { QrcodeReaderService } from './qrcode-reader.service';

@Component({
  selector: 'app-upload-qrcode',
  templateUrl: './upload-qrcode.component.html',
  styleUrls: ['./upload-qrcode.component.scss']
})
export class UploadQrcodeComponent implements OnDestroy {

  subscription: Subscription;
  errorMessage: string = "The QR Code decoder cannot find the QR Code.\nPlease try again.";
  failed: boolean;

  constructor(private qrReader: QrcodeReaderService) { }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onFileChange(event): void {
    const file = event.target.files[0];
    this.subscription = this.qrReader.decodeService(file).subscribe(
      url => {
        if(url == 'error decoding QR Code') {
          this.failed = true;
        } else {
          window.open(url, '_blank');
          this.failed = false;
        }
      }
    );
  }
}
