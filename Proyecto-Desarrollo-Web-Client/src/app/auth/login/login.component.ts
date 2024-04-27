import { PageType } from './../../shared/background/pageType';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../services/auth/login.service';
import { LoginRequest } from '../../services/auth/loginRequest';
import { TimeService } from '../../services/time.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  error: string ="";
  pageType : PageType = {page : "login"};

  loginForm = this.formBuilder.group({
    username: ['', [Validators.required]],
    password: ['', Validators.required]
  });
  constructor(private formBuilder: FormBuilder, private router:Router, private loginService: LoginService, private timeService: TimeService) { 
    this.timeService.restartValues();
}
  ngOnInit(): void {
  }
  get username(){
    return this.loginForm.controls.username;
  }
  get password(){
    return this.loginForm.controls.password;
  }
  login(){
    if(this.loginForm.valid){
      //imprimimos en consola 
      console.log(this.loginForm.value);
      this.loginService.login(this.loginForm.value as LoginRequest).subscribe({
        next: (userData) => {
          console.log(userData);
        },
        error: (errorData) => {
          console.error(errorData);
          this.error = errorData;
        },
        complete: () => {
          console.log('Login complete');
          this.router.navigateByUrl('/home');
          this.loginForm.reset();
        }
      });
    }
    else{
      //usamos clases de control de estado para mostrarlo de forma más amigable css
      this.loginForm.markAllAsTouched();
    }
  }
}
