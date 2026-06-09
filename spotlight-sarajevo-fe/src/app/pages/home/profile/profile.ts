import { Component, OnInit } from '@angular/core';
import { PageHeader } from "../../../components/page-header/page-header";
import { ButtonRegular } from "../../../components/button-regular/button-regular";
import { AuthService } from '../../../core/services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionService } from '../../../core/services/session.service';
import { TranslocoPipe } from '@ngneat/transloco';
import { ButtonPrimary } from "../../../components/button-primary/button-primary";
import { TextInput } from "../../../components/text-input/text-input";
import { UserInfo } from 'node:os';
import { UserInfoModel } from '../../../shared/models/auth.model';
import { TextHolder } from "../../../components/text-holder/text-holder";
import { ProfileIcon } from "../../../resources/icons/profile-icon/profile-icon";
import { Subheading } from "../../../components/subheading/subheading";

@Component({
  selector: 'app-profile',
  imports: [PageHeader, ButtonRegular, TranslocoPipe, TextHolder, ProfileIcon, Subheading, ButtonPrimary],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
  host: {
    class: "flex flex-col w-full justify-start items-center"
  }
})
export class Profile implements OnInit{
  userInfo!: UserInfoModel

  constructor(
    protected authService: AuthService,
    protected session: SessionService,
    protected router: Router,
    protected activatedRoute: ActivatedRoute
  ){}
  
  ngOnInit(): void {
    this.userInfo = this.activatedRoute.snapshot.data['userInfo'];
    console.log(this.userInfo)
  }

  logout(){
    this.authService.logout().subscribe({
      next: (response: any) => {
        this.session.clearSession()
        this.router.navigate(['/auth/login'])
      }
    })
  }
}
