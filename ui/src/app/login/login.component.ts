import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'
import { User } from '../shared/model/user'
import { UserService } from '../shared/service/user.service'
import { LoginService } from '../shared/service/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  user: User;
  closeErrorAlert: Boolean = true;

  constructor(private userService: UserService, private loginService: LoginService, private router: Router) { }

  ngOnInit() {
    this.user = new User();
  }

  login() {
    this.userService.login(this.user).subscribe(
      (response) => {
        this.loginService.loggedIn.next(true);
        this.router.navigate(['/home']);
      }, (error) => {
        this.loginService.loggedIn.next(false);
        if (error.status == 400) {
          this.showErrorAlert();
        } else {
          console.log(error);
        }
      });
  }

  private showErrorAlert() {
    this.closeErrorAlert = false;
    setTimeout(() => {
      this.closeErrorAlert = true;
    }, 5000);
  }
}
