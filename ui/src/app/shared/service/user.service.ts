import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { environment } from "../../../environments/environment";
import { User } from '../model/user';

@Injectable()
export class UserService {
  private server_url: string;

  constructor(private http: HttpClient) {
    this.server_url = environment._SERVER_ENDPOINT;
  }

  public login(user: User): Observable<User> {
    const url = this.server_url + '/public/users/authentication';
    return this.http.post<User>(url, user, {withCredentials: true});
  }

  public logout(): Observable<Response> {
    const url = this.server_url + '/public/users/logout';
    return this.http.post<Response>(url, null, {withCredentials: true});
  }

  public register(newUser: User): Observable<User> {
    const url = this.server_url + '/public/users';
    return this.http.post<User>(url, newUser, {withCredentials: true});
  }
}
