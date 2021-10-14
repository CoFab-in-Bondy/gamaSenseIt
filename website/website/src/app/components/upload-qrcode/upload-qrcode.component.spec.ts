import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadQrcodeComponent } from './upload-qrcode.component';

describe('UploadQrcodeComponent', () => {
  let component: UploadQrcodeComponent;
  let fixture: ComponentFixture<UploadQrcodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadQrcodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadQrcodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
