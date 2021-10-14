import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SensorVersionPage } from './sensor-version.component';

describe('DashboardComponent', () => {
  let component: SensorVersionPage;
  let fixture: ComponentFixture<SensorVersionPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SensorVersionPage ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SensorVersionPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
