import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PmProgressBarComponent } from './pm-progress-bar.component';

describe('PmProgressBarComponent', () => {
  let component: PmProgressBarComponent;
  let fixture: ComponentFixture<PmProgressBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PmProgressBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PmProgressBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
