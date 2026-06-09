import { inject } from "@angular/core";
import { UserInfoModel } from "../../shared/models/auth.model";
import { AuthService } from "../services/auth.service";
import { UserService } from "../../services/user.service";
import { ResolveFn } from "@angular/router";
import { map } from "rxjs";

export const profilePageResolver: ResolveFn<UserInfoModel> = (route, state) => {
  const userService = inject(UserService);

  return userService.getUserDetails().pipe(
    map((userInfo: UserInfoModel) => {
      return userInfo;
    })
  );
};