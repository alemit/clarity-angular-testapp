import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from "rxjs/Observable";
import { UserService } from './shared/service/user.service';
import { LoginService } from './shared/service/login.service';

@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    isLoggedIn$: Observable<boolean>;

    constructor(private userService: UserService, private loginService: LoginService, private router: Router) { }

    ngOnInit() {
        this.isLoggedIn$ = this.loginService.isLoggedIn;
    }

    logout() {
        this.userService.logout().subscribe(
            (response) => {
                this.loginService.loggedIn.next(false);
                this.router.navigate(['/login']);
            }, (error) => {
                console.log(error);
            });
    }
}
