import { CanActivateFn, Router } from "@angular/router";
import { SessionService } from "../services/session.service";
import { inject } from "@angular/core";
import { map } from "rxjs";

/**
 * Guard that redirects admin users away from public pages (like homepage) to the admin dashboard.
 * This ensures admin users go directly to their dashboard when accessing public pages.
 */
export const adminRedirectGuard: CanActivateFn = (route, state) => {
  const session = inject(SessionService);
  const router = inject(Router);

  const currentUser = session.user();
  
  // If user is already loaded in session
  if (currentUser) {
    if (currentUser.isAdmin) {
      // User is admin - redirect to admin dashboard
      return router.createUrlTree(['/admin/dashboard']);
    }
    // User is regular user - allow access to public page
    return true;
  }

  // User not in session yet, try to restore
  return session.restoreSession().pipe(
    map(isValid => {
      if (!isValid) {
        // Not authenticated - allow access to public page
        return true;
      }
      
      const user = session.user();
      if (user?.isAdmin) {
        // User is admin - redirect to admin dashboard
        return router.createUrlTree(['/admin/dashboard']);
      }
      
      // User is regular user - allow access to public page
      return true;
    })
  );
};
