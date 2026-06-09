import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommunityRequestCard } from './community-request-card';

describe('CommunityRequestCard', () => {
  let component: CommunityRequestCard;
  let fixture: ComponentFixture<CommunityRequestCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommunityRequestCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommunityRequestCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
