import { TestBed } from '@angular/core/testing';

import { QrcodeReaderService } from './qrcode-reader.service';

describe('QrcodeReaderService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: QrcodeReaderService = TestBed.get(QrcodeReaderService);
    expect(service).toBeTruthy();
  });
});
