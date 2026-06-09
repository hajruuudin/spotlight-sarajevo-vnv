import { CanActivateFn, Router } from "@angular/router";
import { SessionService } from "../services/session.service";
import { inject } from "@angular/core";
import { map } from "rxjs";

export const adminGuard: CanActivateFn = (route, state) => {
  const session = inject(SessionService);
  const router = inject(Router);

  const currentUser = session.user();
  
  if (currentUser) {
    if (currentUser.isAdmin) {
      return true;
    }

    // User is authenticated but not admin - redirect to home
    return router.createUrlTree(['/home']);
  }

  return session.restoreSession().pipe(
    map(isValid => {
      if (!isValid) {
    
        return router.createUrlTree(['/auth/login'], {
          queryParams: { returnUrl: state.url }
        });
      }
      
      const user = session.user();
      if (user?.isAdmin) {
        return true;
      }
      
      // User is authenticated but not admin - redirect to home
      return router.createUrlTree(['/home']);
    })
  );
};
