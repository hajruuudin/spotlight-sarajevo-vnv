import { Component, OnInit } from '@angular/core';
import { TouristGuideService } from '../../../services/tourist.guide.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { SpinnerService } from '../../../core/services/spinner.service';
import {
  TouristGuideOverviewModel,
  TouristGuideSectionModel,
} from '../../../shared/models/tourist.guide.model';
import { SessionService } from '../../../core/services/session.service';
import { PageHeader } from '../../../components/page-header/page-header';
import { Subheading } from '../../../components/subheading/subheading';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-tourist-guide-overview',
  imports: [PageHeader, Subheading, TranslocoPipe],
  templateUrl: './tourist-guide-overview.html',
  styleUrl: './tourist-guide-overview.css',
  host: {
    class: 'flex flex-col w-full justify-start items-center',
  },
})
export class TouristGuideOverview implements OnInit {
  protected guideOverview!: TouristGuideOverviewModel;

  constructor(
    private guideService: TouristGuideService,
    private router: Router,
    protected session: SessionService,
    private activatedRoute: ActivatedRoute,
    private toastr: HotToastService,
    private spinner: SpinnerService,
  ) {}

  ngOnInit(): void {
    const data = this.activatedRoute.snapshot.data[
      'guideOverviewData'
    ] as TouristGuideOverviewModel;
    this.guideOverview = data;
  }
}
