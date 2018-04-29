import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'
import { User } from '../shared/model/user'
import { UserService } from '../shared/service/user.service'
import { repeat } from 'rxjs/operator/repeat';
import { LoginService } from '../shared/service/login.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  newUser: User;
  retypedPassword: string;
  alertMessage: string;
  closeErrorAlert: Boolean = true;

  constructor(private userService: UserService, private loginService: LoginService, private router: Router) { }

  ngOnInit() {
    this.newUser = new User();
    this.retypedPassword = '';
    this.alertMessage = '';
  }

  register(nweUser: User) {
    if (this.comparePasswrods(this.newUser.password, this.retypedPassword)) {
      this.userService.register(this.newUser).subscribe(
        (response) => {
          this.loginService.loggedIn.next(true);
          this.router.navigate(['/home']);
        }, (error) => {
          if (error.status == 400) {
            this.showErrorAlert('Please enter valid username and password!');
          } else {
            console.log(error);
          }
        });
    } else {
      this.showErrorAlert('The two passwords don\'t match');
    }
  }

  private comparePasswrods(pass: string, rePass: string): Boolean {
    return pass === rePass;
  }

  private showErrorAlert(message: string) {
    this.closeErrorAlert = false;
    this.alertMessage = message;
    setTimeout(() => {
      this.closeErrorAlert = true;
      this.alertMessage = '';
    }, 5000);
  }
}
