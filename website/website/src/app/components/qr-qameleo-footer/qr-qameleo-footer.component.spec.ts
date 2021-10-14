import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QrQameleoFooterComponent } from './qr-qameleo-footer.component';

describe('QrQameleoFooterComponent', () => {
  let component: QrQameleoFooterComponent;
  let fixture: ComponentFixture<QrQameleoFooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QrQameleoFooterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QrQameleoFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
