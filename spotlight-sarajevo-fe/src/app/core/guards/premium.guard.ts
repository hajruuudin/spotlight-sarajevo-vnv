import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionService } from '../services/session.service';
import { map } from 'rxjs';

export const premiumGuard: CanActivateFn = (route, state) => {
  const session = inject(SessionService);
  const router = inject(Router);

  // Check if user is already loaded in session
  const currentUser = session.user();
  
  if (currentUser) {
    // User is logged in, check if premium
    if (currentUser.isPremium) {
      return true;
    }
    // User is logged in but not premium - redirect to premium benefits
    return router.createUrlTree(['/premium-benefits'], {
      queryParams: { returnUrl: state.url }
    });
  }

  // User is not logged in, try to restore session
  return session.restoreSession().pipe(
    map(isValid => {
      if (!isValid) {
        // Not authenticated - redirect to auth benefits
        return router.createUrlTree(['/auth-benefits'], {
          queryParams: { returnUrl: state.url }
        });
      }
      
      // User is authenticated, check if premium
      const user = session.user();
      if (user?.isPremium) {
        return true;
      }
      
      // User is authenticated but not premium - redirect to premium benefits
      return router.createUrlTree(['/premium-benefits'], {
        queryParams: { returnUrl: state.url }
      });
    })
  );
};
