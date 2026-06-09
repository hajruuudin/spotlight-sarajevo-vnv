import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscoverIcon } from './discover-icon';

describe('DiscoverIcon', () => {
  let component: DiscoverIcon;
  let fixture: ComponentFixture<DiscoverIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiscoverIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiscoverIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
