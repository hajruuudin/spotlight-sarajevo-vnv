import { Component } from '@angular/core';
import { ButtonPrimary } from "../../../components/button-primary/button-primary";
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-benefits',
  imports: [ButtonPrimary],
  templateUrl: './auth-benefits.html',
  styleUrl: './auth-benefits.css',
  host: {
    class: 'max-w-application w-full h-screen mx-auto flex flex-col justify-start items-center px-4'
  }
})
export class AuthBenefits {
  constructor(private router: Router){}

  navigateToRegister(){
    this.router.navigate(['/auth/register'])
  }
}
