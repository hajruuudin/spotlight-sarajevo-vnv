import { CanActivateFn, Router } from "@angular/router";
import { SessionService } from "../services/session.service";
import { inject } from "@angular/core";
import { map } from "rxjs";

/**
 * Guard that prevents authenticated users from accessing auth pages (login, register).
 * Authenticated users are redirected to the homepage instead.
 */
export const unauthGuard: CanActivateFn = (route, state) => {
  const session = inject(SessionService);
  const router = inject(Router);

  const currentUser = session.user();
  
  // If user is already loaded in session
  if (currentUser) {
    // User is authenticated - redirect to homepage
    return router.createUrlTree(['/homepage']);
  }

  // User not in session yet, try to restore
  return session.restoreSession().pipe(
    map(isValid => {
      if (isValid) {
        // User is authenticated - redirect to homepage
        return router.createUrlTree(['/homepage']);
      }
      
      // User is not authenticated - allow access to auth pages
      return true;
    })
  );
};
