import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../services/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {}

  register() {
    this.router.navigate(['/register']);
  }

  login() {
    this.errorMsg = [];
    this.authService
      .login({
        body: this.authRequest,
      })
      .subscribe({
        next: (response) => {
          this.tokenService.token = response.token as string;
          this.router.navigate(['books']);
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
