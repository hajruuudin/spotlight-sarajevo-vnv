import { Component, OnInit } from '@angular/core';
import { ButtonPrimary } from "../../../components/button-primary/button-primary";
import { Router, ActivatedRoute } from '@angular/router';
import { SessionService } from '../../../core/services/session.service';
import { TranslocoPipe } from '@ngneat/transloco';

@Component({
  selector: 'app-premium-benefits',
  imports: [ButtonPrimary, TranslocoPipe],
  templateUrl: './premium-benefits.html',
  styleUrl: './premium-benefits.css',
  host: {
    class: 'max-w-application w-full h-screen mx-auto flex flex-col justify-start items-center px-4'
  }
})
export class PremiumBenefits implements OnInit {
  returnUrl: string | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    protected session: SessionService
  ) {}

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
  }

  navigateToProfile() {
    this.router.navigate(['/profile']);
  }

  getCurrentLanguage(): string {
    return this.session.language();
  }
}
