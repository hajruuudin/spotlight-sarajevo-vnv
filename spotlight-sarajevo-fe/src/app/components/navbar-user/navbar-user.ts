import { Component, computed } from '@angular/core';
import { HomepageIcon } from '../../resources/icons/homepage-icon/homepage-icon';
import { ProfileIcon } from '../../resources/icons/profile-icon/profile-icon';
import { DiscoverIcon } from '../../resources/icons/discover-icon/discover-icon';
import { SpotsIcon } from '../../resources/icons/spots-icon/spots-icon';
import { EventsIconComponent } from '../../resources/icons/events-icon/events-icon';
import { CollectionIcon } from '../../resources/icons/collection-icon/collection-icon';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { GuidesIcon } from '../../resources/icons/guides-icon/guides-icon';
import { RequestIcon } from '../../resources/icons/request-icon/request-icon';
import { TransportIcon } from '../../resources/icons/transport-icon/transport-icon';
import { HamburgerIcon } from '../../resources/icons/hamburger-icon/hamburger-icon';
import { SessionService } from '../../core/services/session.service';
import { TranslocoPipe } from '@ngneat/transloco';
import { ButtonPrimary } from "../button-primary/button-primary";

@Component({
  selector: 'app-navbar-user',
  imports: [
    HomepageIcon,
    ProfileIcon,
    DiscoverIcon,
    SpotsIcon,
    EventsIconComponent,
    CollectionIcon,
    RouterLink,
    GuidesIcon,
    RequestIcon,
    TransportIcon,
    HamburgerIcon,
    RouterLinkActive,
    TranslocoPipe,
    ButtonPrimary
],
  templateUrl: './navbar-user.html',
  styleUrl: './navbar-user.css',
  host: {
    class: 'sticky top-0 z-50',
  },
})
export class NavbarUser {
  protected isMobileNavbarLoaded: Boolean = true;
  protected isMobileNavbarOpen = false;
  protected loggedUser = computed(() => this.session.user() !== null)

  constructor(protected session: SessionService, protected router: Router) {}

  toggleMobileNavbar() {
    this.isMobileNavbarOpen = !this.isMobileNavbarOpen;
  }

  toggleTheme(): void {
    this.session.setTheme(this.session.theme() === 'dark' ? 'light' : 'dark');
  }

  toggleLanguage() {
    this.session.setLanguage(this.session.language() === 'en' ? 'ba' : 'en')
  }

  navigateToLogin(){
    this.router.navigate(['/auth/login'])
  }
}
