import { Routes } from '@angular/router';
import { Auth } from './interfaces/auth/auth';
import { Login } from './pages/auth/login/login';
import { Signup } from './pages/auth/signup/signup';
import { User } from './interfaces/home/user';
import { Homepage } from './pages/home/homepage/homepage';
import { SpotSearch } from './pages/home/spot-search/spot-search';
import { SpotOverview } from './pages/home/spot-overview/spot-overview';
import { SpotMapOverview } from './pages/home/spot-map-overview/spot-map-overview';
import { EventSearch } from './pages/home/event-search/event-search';
import { EventOverview } from './pages/home/event-overview/event-overview';
import { EventCalendarOverview } from './pages/home/event-calendar-overview/event-calendar-overview';
import { Discover } from './pages/home/discover/discover';
import { Profile } from './pages/home/profile/profile';
import { TouristGuide } from './pages/home/tourist-guide/tourist-guide';
import { TouristGuideOverview } from './pages/home/tourist-guide-overview/tourist-guide-overview';
import { Transport } from './pages/home/transport/transport';
import { Collections } from './pages/home/collections/collections';
import { Admin } from './interfaces/admin/admin';
import { AdminSpotOverview } from './pages/admin/admin-spot-overview/admin-spot-overview';
import { AdminEventOverview } from './pages/admin/admin-event-overview/admin-event-overview';
import { AdminGuideOverview } from './pages/admin/admin-guide-overview/admin-guide-overview';
import { AdminRequestOverview } from './pages/admin/admin-request-overview/admin-request-overview';
import { AdminUserOverview } from './pages/admin/admin-user-overview/admin-user-overview';
import { AdminTransportOverview } from './pages/admin/admin-transport-overview/admin-transport-overview';
import { AdminAddSpots } from './pages/admin/admin-add-spots/admin-add-spots';
import { AdminAddEvents } from './pages/admin/admin-add-events/admin-add-events';
import { AdminAddGuides } from './pages/admin/admin-add-guides/admin-add-guides';
import { NotFound } from './interfaces/error/not-found';
import { CommunityRequests } from './pages/home/community-requests/community-requests';
import { spotResolver } from './core/resolvers/spot.resolver';
import { eventResolver } from './core/resolvers/event.resolver';
import { collectionsResolver } from './core/resolvers/collection.resolver';
import { AuthBenefits } from './pages/home/auth-benefits/auth-benefits';
import { PremiumBenefits } from './pages/home/premium-benefits/premium-benefits';
import { authGuard } from './core/guards/auth.guard';
import { premiumGuard } from './core/guards/premium.guard';
import { discoverResolver } from './core/resolvers/discover.resolver';
import { homepageResolver } from './core/resolvers/homepage.resolver';
import { touristGuideResolver } from './core/resolvers/tourist.guide.resolver';
import { touristGuideOverviewResolver } from './core/resolvers/tourist.guide.overview.resolver';
import { transportResolver } from './core/resolvers/transport.resolver';
import { profileEnd } from 'node:console';
import { profilePageResolver } from './core/resolvers/profile.page.resolver';
import { communityRequestResolver } from './core/resolvers/community.request.resolver';
import { spotMapResolver } from './core/resolvers/spot.map.resolver';
import { eventCalendarResolver } from './core/resolvers/event.calendar.resolver';
import { adminGuard } from './core/guards/admin.guard';
import { adminRedirectGuard } from './core/guards/admin-redirect.guard';
import { AdminDashboard } from './pages/admin/dashboard/dashboard';
import { unauthGuard } from './core/guards/unauth.guard';

export const routes: Routes = [
  {
    path: 'auth',
    component: Auth,
    canActivate: [unauthGuard],
    children: [
      { path: 'login', canActivate: [unauthGuard], component: Login, title: 'Login - SpotlightSarajevo' },
      { path: 'register', canActivate: [unauthGuard], component: Signup, title: 'Login - SpotlightSarajevo' },
    ],
  },
  {
    path: '',
    canActivate: [adminRedirectGuard],
    component: User,
    children: [
      { path: '', redirectTo: 'homepage', pathMatch: 'full' },
      { path: 'homepage', resolve: {homepageData: homepageResolver}, component: Homepage, title: 'Homepage - SpotlightSarajevo' },
      { path: 'spots', component: SpotSearch, title: 'Browse Spots - SpotlightSarajevo' },
      { path: 'spots/map', canActivate: [premiumGuard], resolve: {spotMapData: spotMapResolver}, component: SpotMapOverview, title: 'Spot Map - SpotlightSarajevo' },
      { path: 'spots/:spotSlug', resolve: {spotData: spotResolver}, component: SpotOverview, title: 'Spot Overview - SpotlightSarajevo',},
      { path: 'events', component: EventSearch, title: 'Browse Events - SpotlightSarajevo' },
      { path: 'events/calendar', canActivate: [premiumGuard], resolve: {eventCalendarData: eventCalendarResolver}, component: EventCalendarOverview, title: 'Event Calendar - SpotlightSarajevo' },
      { path: 'events/:eventSlug', resolve: {eventData: eventResolver}, component: EventOverview, title: 'Event - SpotlightSarajevo',},
      { path: 'discover', resolve: { discoverData: discoverResolver }, component: Discover, title: 'Discover - SpotlightSarajevo' },
      { path: 'profile', resolve: { userInfo: profilePageResolver }, component: Profile, title: 'Profile - SpotlightSarajevo' },
      { path: 'guide', resolve: { touristGuides: touristGuideResolver }, component: TouristGuide, title: 'Browse Guides - SpotlightSarajevo' },
      { path: 'guide/:slug', resolve: { guideOverviewData: touristGuideOverviewResolver }, component: TouristGuideOverview,title: 'Guide Overview - SpotlightSarajevo' },
      { path: 'transport', resolve: { transportData: transportResolver }, component: Transport, title: 'Public Transport - SpotlightSarajevo' },
      { path: 'collections', canMatch: [authGuard], resolve: { collectionData: collectionsResolver }, component: Collections, title: 'Your Collections - SpotlightSarajevo' },
      { path: 'requests', canMatch: [authGuard], resolve: { requestData: communityRequestResolver }, component: CommunityRequests, title: 'Community Requests - SpotlightSarajevo' },
      { path: 'auth-benefits', component: AuthBenefits, title: 'Login for more! Extra Functions'},
      { path: 'premium-benefits', component: PremiumBenefits, title: 'Upgrade to Premium! - SpotlightSarajevo'}
    ],
  },
  {
    path: 'admin',
    canActivate: [adminGuard],
    component: Admin,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: AdminDashboard, title: 'Admin - Dashboard' },
      { path: 'spots-overview', component: AdminSpotOverview, title: 'Admin - Spots Overview' },
      { path: 'events-overview', component: AdminEventOverview, title: 'Admin - Events Overview' },
      { path: 'guide-overview', component: AdminGuideOverview, title: 'Admin - Guides Overview' },
      { path: 'user-overview', component: AdminUserOverview, title: 'Admin - User Overview' },
      { path: 'transport-overview', component: AdminTransportOverview, title: 'Admin - Transport Overview' },
      { path: 'requests-overview', component: AdminRequestOverview, title: 'Admin - Requests Overview' },
      { path: 'add-spot', component: AdminAddSpots, title: 'Admin - Add Spot' },
      { path: 'add-event', component: AdminAddEvents, title: 'Admin - Add Event' },
      { path: 'add-guide', component: AdminAddGuides, title: 'Admin - Add Guide' }
    ],
  },
  {
    path: '**',
    component: NotFound,
    title: 'Page Not Found - SpotlightSarajevo',
  },
];
