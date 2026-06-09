import { Component, OnInit } from '@angular/core';
import { PageHeader } from "../../../components/page-header/page-header";
import { TouristGuideShorthandModel } from '../../../shared/models/tourist.guide.model';
import { SessionService } from '../../../core/services/session.service';
import { TouristGuideService } from '../../../services/tourist.guide.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslocoPipe } from '@ngneat/transloco';
import { TouristGuideCard } from "../../../components/tourist-guide-card/tourist-guide-card";
import { Subheading } from "../../../components/subheading/subheading";

@Component({
  selector: 'app-tourist-guide',
  imports: [PageHeader, TranslocoPipe, TouristGuideCard, Subheading],
  templateUrl: './tourist-guide.html',
  styleUrl: './tourist-guide.css',
  host: {
    class: "flex flex-col w-full justify-start items-center"
  }
})
export class TouristGuide implements OnInit {
  mainGuides: TouristGuideShorthandModel[] = [];
  newGuides: TouristGuideShorthandModel[] = [];
  touristRelatedGuides: TouristGuideShorthandModel[] = [];
  allGuides: TouristGuideShorthandModel[] = [];

  constructor(
    private guideService: TouristGuideService,
    private toastr: HotToastService,
    protected session: SessionService,
    private route: ActivatedRoute,
    private router: Router
  ){}

  ngOnInit(): void {
    const resolvedData = this.route.snapshot.data['touristGuides'];
    
    this.mainGuides = resolvedData.mainGuides;
    this.newGuides = resolvedData.newGuides;
    this.touristRelatedGuides = resolvedData.touristRelatedGuides;
    this.allGuides = resolvedData.allGuides;
  }

  navigateToGuide(slug: string): void {
    this.router.navigate([`/guide/${slug}`]);
  }
}
