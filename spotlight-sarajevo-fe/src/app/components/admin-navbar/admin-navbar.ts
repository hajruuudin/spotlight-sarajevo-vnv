import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TranslocoPipe } from '@ngneat/transloco';
import { SessionService } from '../../core/services/session.service';
import { AuthService } from '../../core/services/auth.service';
import { HomepageIcon } from '../../resources/icons/homepage-icon/homepage-icon';
import { StackIcon } from '../../resources/icons/stack-icon/stack-icon';
import { AddIcon } from '../../resources/icons/add-icon/add-icon';
import { SpotsIcon } from '../../resources/icons/spots-icon/spots-icon';
import { EventsIconComponent } from '../../resources/icons/events-icon/events-icon';
import { ButtonPrimary } from '../button-primary/button-primary';
import { GuidesIcon } from "../../resources/icons/guides-icon/guides-icon";
import { TransportIcon } from "../../resources/icons/transport-icon/transport-icon";
import { ProfileIcon } from "../../resources/icons/profile-icon/profile-icon";

@Component({
  selector: 'app-admin-navbar',
  imports: [
    RouterLink,
    TranslocoPipe,
    HomepageIcon,
    StackIcon,
    AddIcon,
    SpotsIcon,
    ButtonPrimary,
    EventsIconComponent,
    GuidesIcon,
    TransportIcon,
    ProfileIcon
],
  templateUrl: './admin-navbar.html',
  styleUrl: './admin-navbar.css',
  host: {
    class: 'sticky top-0 z-50',
  },
})
export class AdminNavbar {
  constructor(
    protected session: SessionService,
    protected router: Router,
    protected authService: AuthService
  ) {}

  toggleTheme(): void {
    this.session.setTheme(this.session.theme() === 'dark' ? 'light' : 'dark');
  }

  toggleLanguage() {
    this.session.setLanguage(this.session.language() === 'en' ? 'ba' : 'en');
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.session.clearSession();
        this.router.navigate(['/auth/login']);
      }
    });
  }
}
