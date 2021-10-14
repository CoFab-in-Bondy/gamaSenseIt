import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PMLayoutComponent } from './pm-layout.component';

describe('PMLayoutComponent', () => {
  let component: PMLayoutComponent;
  let fixture: ComponentFixture<PMLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PMLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PMLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
