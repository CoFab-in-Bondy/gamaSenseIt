import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QrQameleoComponent } from './qr-qameleo.component';

describe('QrQameleoComponent', () => {
  let component: QrQameleoComponent;
  let fixture: ComponentFixture<QrQameleoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QrQameleoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QrQameleoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
