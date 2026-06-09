import { ActivatedRoute, ActivatedRouteSnapshot, ResolveFn, RouterState, RouterStateSnapshot } from "@angular/router";
import { CommunityRequestModel } from "../../shared/models/community.request.model";
import { CommunityRequestService } from "../../services/community.request.service";
import { inject } from "@angular/core";
import { catchError, map, of } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";
import { HotToastService } from "@ngxpert/hot-toast";

interface CommunityRequestPageData {
    communityRequests: CommunityRequestModel[];
}

export const communityRequestResolver: ResolveFn<CommunityRequestPageData> = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
) => {
    const communityRequestService = inject(CommunityRequestService)
    const toastr = inject(HotToastService)

    return communityRequestService.getUserRequests().pipe(
        map(response => ({
            communityRequests: response
        })),
        catchError((error : HttpErrorResponse) => {
            toastr.error(error.message)
            return of({
                communityRequests: []
            });
        })
    )
}