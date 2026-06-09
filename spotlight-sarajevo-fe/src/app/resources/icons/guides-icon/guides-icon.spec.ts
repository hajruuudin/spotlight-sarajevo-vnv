import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuidesIcon } from './guides-icon';

describe('GuidesIcon', () => {
  let component: GuidesIcon;
  let fixture: ComponentFixture<GuidesIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuidesIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuidesIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
