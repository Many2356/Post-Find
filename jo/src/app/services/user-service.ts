import { Injectable }   from '@angular/core';
import { HttpClient }   from '@angular/common/http';
import { Observable }   from 'rxjs';
import { User, RegisterRequest, LoginRequest, UpdateProfileRequest } from '../models/user';

@Injectable({ providedIn: 'root' })
export class UserService {
  private api = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  register(data: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.api}/register`, data);
  }

  login(data: LoginRequest): Observable<User> {
    return this.http.post<User>(`${this.api}/login`, data);
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.api}/${id}`);
  }

  updateProfile(id: number, data: UpdateProfileRequest): Observable<User> {
    return this.http.put<User>(`${this.api}/${id}`, data);
  }

  deleteAccount(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }
}