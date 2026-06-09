import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GoogleIcon } from './google-icon';

describe('GoogleIcon', () => {
  let component: GoogleIcon;
  let fixture: ComponentFixture<GoogleIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoogleIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GoogleIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
