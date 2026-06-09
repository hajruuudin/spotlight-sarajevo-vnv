import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestIcon } from './request-icon';

describe('RequestIcon', () => {
  let component: RequestIcon;
  let fixture: ComponentFixture<RequestIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
