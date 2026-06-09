import { ChangeDetectorRef, Component, computed, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { SpinnerService } from '../../../core/services/spinner.service';
import { CategoryService } from '../../../services/category.service';
import { EventCategoryModel, SpotCategoryModel } from '../../../shared/models/category.model';
import { HttpErrorResponse } from '@angular/common/http';
import { NgClass } from '@angular/common';
import { TextInput } from "../../../components/text-input/text-input";
import { ButtonRegular } from "../../../components/button-regular/button-regular";
import { RouterLink } from "@angular/router";
import { CategorySelector } from '../../../components/category-selector/category-selector';
import { QuestionComponent } from "../../../components/question-component/question-component";
import { TranslocoPipe, TranslocoService } from '@ngneat/transloco';
import { AuthService } from '../../../core/services/auth.service';
import { LoggedUserModel, PreferencesModel, SystemUserModel } from '../../../shared/models/auth.model';
import { SessionService } from '../../../core/services/session.service';

declare global {
  interface Window {
    google: any;
  }
}

@Component({
  selector: 'app-signup',
  imports: [NgClass, ReactiveFormsModule, TextInput, ButtonRegular, RouterLink, CategorySelector, QuestionComponent, TranslocoPipe],
  templateUrl: './signup.html',
  styleUrl: './signup.css',
  host: {
    class: "dark:bg-(--primary-200) bg-(--primary-800) md:rounded-2xl md:w-3/5 w-full max-w-5xl h-full md:h-auto md:max-h-6/7 hover:outline-4 dark:hover:outline-(--primary-600) hover:outline-(--primary-600) transition-all flex flex-col justify-start items-center space-y-2 shadow-xl relative overflow-auto"
  }
})
export class Signup implements OnInit {
  protected progressBarWidth: String = 'w-1/5'
  protected isCredentialsSectionLoaded: Boolean = true;
  protected isSpotCategoriesSectionLoaded: Boolean = false;
  protected isEventCategoriesSectionLoaded: Boolean = false;
  protected isSurveySectionLoaded: Boolean = false;
  protected isCompleteSectionLoaded: Boolean = false;

  protected systemCredentialsForm!: FormGroup;

  protected allSpotCategories: SpotCategoryModel[] = [];
  protected allEventCategories: EventCategoryModel[] = [];
  protected selectedSpotCategories: number[] = [];
  protected selectedEventCategories: number[] = [];

  protected questions = [
    {
      id: 1,
      questionEn: "Have you visited Sarajevo's historical sites and iconic landmarks, focusing on exploring the hsitorical aspect of Sarajevo? (1/4)",
      questionBa: "Da li ste posjetili Sarajevsku kulturnu baštinu i ikonične historijske tačke te kultna, poznata mjesta? (1/4)",
      isASelected: false,
      isBSelected: false,
      isAnswred: false
    },
    {
      id: 2,
      questionEn: "Are you interested in exploring less popular spots instead of commonly accessed and vanilla-esque places? (2/4)",
      questionBa: "Da li ste zainteresovani u istraživanju i posjećivanju manje poznatih mjesta i lokala u sarajevu umjesto popularnih 'standardica'? (2/4)",
      isASelected: false,
      isBSelected: false,
      isAnswred: false
    },
    {
      id: 3,
      questionEn: "Do you enjoy loud and open audience events, like concerts, pubs, clubs and events which orientate around live performances and energy? (3/4)",
      questionBa: "Da li volite bučnu atmosferu, otvorene događaje i energične lokale kao što su pabovi, klubovi i muzika uživo? (3/4)",
      isASelected: false,
      isBSelected: false,
      isAnswred: false
    },
    {
      id: 4,
      questionEn: "Are you intereseted in attending educational and community events to improve Your social-educative status and learn/explore various topics? (4/4)",
      questionBa: "Da li ste zainteresovani u učestvovanju u raznim edukacionim, humanitarnim i socijalnim događajima u cilju poboljšanja Vašeg znanja i vještina u raznim tematikama? (4/4)",
      isASelected: false,
      isBSelected: false,
      isAnswred: false
    }
  ]

  protected registeredFirstName: String = "Hajrudin"
  protected currentCategoryDescription: String = ''

  constructor(
    private categoryService: CategoryService,
    private authService: AuthService,
    protected session: SessionService,
    private transloco: TranslocoService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    protected spinner: SpinnerService,
    private toastr: HotToastService
  ) { }

  ngOnInit(): void {
    this.systemCredentialsForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      repeatPassword: ['', Validators.required]
    })

    this.categoryService.getAllSpotCategories().subscribe({
      next: (response: SpotCategoryModel[]) => {
        this.allSpotCategories = response;
      },
      error: (response: HttpErrorResponse) => {
        console.error(response.message)
      }
    })

    this.categoryService.getAllEventCategories().subscribe({
      next: (response: EventCategoryModel[]) => {
        this.allEventCategories = response;
      },
      error: (response: HttpErrorResponse) => {
        console.error(response.message)
      }
    })

    this.initializeGoogleSignIn();
  }

  onCategoryHover(description: String): void {
    this.currentCategoryDescription = description;
  }


  updateCategoryDescription(newDescription: string) {
    this.currentCategoryDescription = newDescription
  }

  resetCategoryDescription() {
    this.currentCategoryDescription = 'Hover over a cateogry to see its description!'
  }

  addCategoryToSelected(categoryType: boolean, categoryId: number) {
    let selectedArray = categoryType ? this.selectedSpotCategories : this.selectedEventCategories;

    if (selectedArray.includes(categoryId)) {
      if (categoryType) {
        this.selectedSpotCategories = this.selectedSpotCategories.filter(id => id !== categoryId);
      } else {
        this.selectedEventCategories = this.selectedEventCategories.filter(id => id !== categoryId);
      }
      return;
    }

    if (selectedArray.length < 3) {
      if (categoryType) {
        this.selectedSpotCategories = [...this.selectedSpotCategories, categoryId];
      } else {
        this.selectedEventCategories = [...this.selectedEventCategories, categoryId];
      }
    } else {
      this.toastr.error("You've already selected three categories!");
    }
  }

  questionASelected(q: any, value: boolean) {
    let newQuestions = this.questions.map(question => {
      if (question.id === q.id) {
        question.isAnswred = true
        return { ...question, isASelected: value }
      } else {
        return question
      }
    })

    this.questions = newQuestions
    console.log(this.questions)
  }

  questionBSelected(q: any, value: boolean) {
    let newQuestions = this.questions.map(question => {
      if (question.id === q.id) {
        question.isAnswred = true
        return { ...question, isBSelected: value }
      } else {
        return question
      }
    })

    this.questions = newQuestions
    console.log(this.questions)
  }

  verifyQuestionsSelected() {
    let check = true;

    this.questions.forEach(question => {
      if (!question.isASelected && !question.isBSelected) {
        check = false;
      }
    })

    return check;
  }

  getQuestionAnswer(question: any) {
    if (question.isASelected) {
      return true;
    } else {
      return false;
    }
  }

  passwordMatchValidator(group: FormGroup) {
    const password = group.get('password')?.value;
    const repeatPassword = group.get('repeatPassword')?.value;

    console.log(password)
    console.log(repeatPassword)
    return password == repeatPassword ? true : false;
  }

  checkSurveyAnswers(): Record<string, boolean> {
    const answers: Record<string, boolean> = {};

    this.questions.forEach((q, index) => {
      answers[`question${index + 1}`] = q.isASelected;
    });

    console.log(answers)
    return answers;
  }


  /* Methods for communicating with the backend */
  signInWithGoogleCustom(): void {
    this.spinner.showNavigateSpinner()
    console.log(this.authService.googleClientId)
    window.google.accounts.id.prompt();
  }

  initializeGoogleSignIn(): void {
    console.log(this.authService.googleClientId)
    window.google.accounts.id.initialize({
      client_id: this.authService.googleClientId,
      callback: (credentialResponse: any) => {
        const idToken = credentialResponse?.credential;
        if (idToken) {
          this.authService.storeGoogleCredentials(idToken).subscribe({
            next: (response: any) => {
              this.spinner.hideNavigateSpinner()
              this.moveToSpotCategoriesSection(response)
            },
            error: (error: HttpErrorResponse) => {
              this.spinner.hideNavigateSpinner()
              this.toastr.error('Oops, there was an error registering... Try again later :(')
            }
          });
        } else {
          this.toastr.error("Internal error! Try again later :(", { style: { border: "2px solid red", padding: "20px" } })
          console.error('No ID token received.');
        }
      }
    });
  }

  initialiseSystemCredentials(): void {
    if (!this.systemCredentialsForm.valid) {
      this.toastr.error("Please fill in the form")
    } else {
      if (!this.passwordMatchValidator(this.systemCredentialsForm)) {
        this.toastr.error("Passwords do not match")
      } else {
        const password = this.systemCredentialsForm.get('password')?.value;

        if (!password || password.length < 6 || !/[A-Z]/.test(password) || !/[0-9]/.test(password)) {
          this.toastr.error(
            "Password must be at least 6 characters long, have one uppercase letter and one number"
          );
          return;
        }

        const userCredentials: SystemUserModel = new SystemUserModel(
          this.systemCredentialsForm.get('firstName')?.value,
          this.systemCredentialsForm.get('lastName')?.value,
          this.systemCredentialsForm.get('email')?.value,
          this.systemCredentialsForm.get('password')?.value,
        )

        this.spinner.showNavigateSpinner()
        this.authService.storeSystemCredentials(userCredentials).subscribe({
          next: (response: any) => {
            this.spinner.hideNavigateSpinner()
            this.moveToSpotCategoriesSection(response)
          },
          error: (error: HttpErrorResponse) => {
            this.spinner.hideNavigateSpinner()
            this.toastr.error(
              'Oops, there was an error registering:' + error.error.message,
              { style: { border: '2px red' } }
            )
          }
        })

      }
    }
  }

  moveToSpotCategoriesSection(response: any) {
    this.spinner.showNavigateSpinner()
    this.isCredentialsSectionLoaded = false;
    this.isSpotCategoriesSectionLoaded = true;
    this.progressBarWidth = 'w-2/5'
    this.registeredFirstName = response['firstName']
    this.cdr.detectChanges();
    this.spinner.hideNavigateSpinner()
  }

  moveToEventCategoriesSection(selectedSpotCategories: number[]) {
    if(selectedSpotCategories.length < 3){
      this.toastr.info("Please select exactly three categories.")
    } else {
      this.spinner.showNavigateSpinner()
      this.isSpotCategoriesSectionLoaded = false;
      this.isEventCategoriesSectionLoaded = true
      this.progressBarWidth = 'w-3/5'
      this.cdr.detectChanges();
      this.spinner.hideNavigateSpinner()
    }
  }

  moveToSurveySection(selectedEventCategories: number[]) {
    if(selectedEventCategories.length < 3){
      this.toastr.info(this.transloco.translate('info.CATEGORY_SELECTION'))
    } else {
      this.spinner.showNavigateSpinner()
      this.isEventCategoriesSectionLoaded = false
      this.isSurveySectionLoaded = true
      this.progressBarWidth = 'w-4/5'
      this.cdr.detectChanges();
      this.spinner.hideNavigateSpinner()
    }
   }

  moveToCompletionSection() {
    if(this.questions.every(q => q.isAnswred === true)){
      const surveyAnswers = this.checkSurveyAnswers();

      const preferencesModel = new PreferencesModel(
        this.selectedSpotCategories,
        this.selectedEventCategories,
        surveyAnswers['question1'],
        surveyAnswers['question2'],
        surveyAnswers['question3'],
        surveyAnswers['question4']
      )

      this.spinner.showNavigateSpinner()
      this.authService.registerToSystem(preferencesModel).subscribe({
        next: (response : LoggedUserModel) => {
          this.spinner.hideNavigateSpinner()
          this.isSurveySectionLoaded = false
          this.isCompleteSectionLoaded = true
          this.progressBarWidth = 'w-4/5'
          this.session.setUser(response)
          this.cdr.detectChanges();
        },
        error: (error : HttpErrorResponse) => {
          this.spinner.hideNavigateSpinner()
          this.toastr.error(this.transloco.translate('error.LOGIN_ERROR'))
        }
      })
    } else {
      this.toastr.info("Please answer all the questions.")
    }
  }

}

