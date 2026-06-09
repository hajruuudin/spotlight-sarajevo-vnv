import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HamburgerIcon } from './hamburger-icon';

describe('HamburgerIcon', () => {
  let component: HamburgerIcon;
  let fixture: ComponentFixture<HamburgerIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HamburgerIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HamburgerIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
