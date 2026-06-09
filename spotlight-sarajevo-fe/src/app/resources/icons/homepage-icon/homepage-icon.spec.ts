import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomepageIcon } from './homepage-icon';

describe('HomepageIcon', () => {
  let component: HomepageIcon;
  let fixture: ComponentFixture<HomepageIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomepageIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomepageIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
