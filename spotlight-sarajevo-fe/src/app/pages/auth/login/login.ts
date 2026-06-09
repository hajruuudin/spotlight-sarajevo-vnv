import { Component, OnInit } from '@angular/core';
import { TextInput } from "../../../components/text-input/text-input";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms"
import { LoggedUserModel, LoginModel } from '../../../shared/models/auth.model';
import { ButtonRegular } from "../../../components/button-regular/button-regular";
import { HotToastService } from '@ngxpert/hot-toast';
import { SpinnerService } from '../../../core/services/spinner.service';
import { ActivatedRoute, Route, Router, RouterLink } from "@angular/router";
import { TranslocoPipe } from '@ngneat/transloco';
import { AuthService } from '../../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-login',
  imports: [TextInput, ReactiveFormsModule, ButtonRegular, RouterLink, TranslocoPipe],
  templateUrl: './login.html',
  styleUrl: './login.css',
  host: {
    class: "dark:bg-(--primary-200) bg-(--primary-700) md:rounded-2xl w-full md:w-3/5 xl:w-2/5 max-w-5xl h-full md:h-auto hover:outline-4 dark:hover:outline-(--primary-600) hover:outline-(--primary-200) transition-all flex flex-col jusitfy-center items-center space-y-2 px-12 py-4 shadow-xl"
  }
})
export class Login implements OnInit {
  protected loginForm!: FormGroup;

  constructor(
    public authService: AuthService,
    public session: SessionService,
    public spinner: SpinnerService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private toast: HotToastService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    })
    this.initializeGoogleLogIn()
  }

  loginWithGoogle(): void {
    this.spinner.showNavigateSpinner()
    window.google.accounts.id.prompt();
  }

  loginWithSystem() {
    if (!this.loginForm.valid) {
      this.toast.error("Email and password are required!")
    } else {
      const loginObject = new LoginModel(
        this.loginForm.get('email')?.value,
        this.loginForm.get('password')?.value
      )

      this.spinner.showGlobalSpinner()
      this.authService.loginWithSystem(loginObject).subscribe({
        next: (response: LoggedUserModel) => {
          this.spinner.hideNavigateSpinner()
          this.session.setUser(response)
          this.router.navigate(['/homepage'])
        },
        error: (error: HttpErrorResponse) => {
          this.spinner.hideNavigateSpinner()
          this.toast.error(error.error.message)
        }
      })
    }
  }

  initializeGoogleLogIn(): void {
    window.google.accounts.id.initialize({
      client_id: this.authService.googleClientId,
      callback: (credentialResponse: any) => {
        const idToken = credentialResponse?.credential;
        if (idToken) {
          this.authService.loginWithGoogle(idToken).subscribe({
            next: (response: LoggedUserModel) => {
              this.spinner.hideNavigateSpinner()
              this.session.setUser(response)
              const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
              this.router.navigateByUrl(returnUrl);
              
            },
            error: (error: HttpErrorResponse) => {
              this.spinner.hideNavigateSpinner()
              this.toast.error(error.error.message)
            }
          });
        } else {
          console.error('No ID token received.');
        }
      }
    });
  }
}
