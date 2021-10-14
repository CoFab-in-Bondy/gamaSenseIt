import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SensorVersionListComponent } from './sensor-version-list.component';

describe('SensorVersionListComponent', () => {
  let component: SensorVersionListComponent;
  let fixture: ComponentFixture<SensorVersionListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SensorVersionListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SensorVersionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
