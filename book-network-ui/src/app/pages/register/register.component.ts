import { Component } from '@angular/core';
import { RegistrationRequest } from '../../services/models';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}
  registerRequest: RegistrationRequest = {
    email: '',
    password: '',
    firstname: '',
    lastname: '',
  };
  errorMsg: Array<string> = [];
  login(): void {
    this.router.navigate(['/login']);
  }
  register(): void {
    this.errorMsg = [];
    this.authService
      .register({
        body: this.registerRequest,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['activate-account']);
        },
        error: (error) => {
          console.log(error);
          if (error.error.validationErrors) {
            this.errorMsg = error.error.validationErrors;
          } else {
            this.errorMsg.push(error.error.error);
          }
        },
      });
  }
}
