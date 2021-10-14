import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SensorVersionComponent } from './sensor-version.component';

describe('SensorVersionComponent', () => {
  let component: SensorVersionComponent;
  let fixture: ComponentFixture<SensorVersionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SensorVersionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SensorVersionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
