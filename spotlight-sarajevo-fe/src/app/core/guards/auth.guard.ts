import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionService } from '../services/session.service';
import { map } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const session = inject(SessionService);
  const router = inject(Router);

  if (session.user()) return true;

  return session.restoreSession().pipe(
    map(isValid => {
      if (isValid) return true;
      return router.createUrlTree(['/auth-benefits'], {
        queryParams: { returnUrl: state.url }
      });
    })
  );
};


