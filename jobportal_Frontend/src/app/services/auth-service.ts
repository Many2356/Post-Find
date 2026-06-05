import { Injectable }      from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { User }            from '../models/user';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private sub = new BehaviorSubject<User | null>(this.load());
  currentUser$ = this.sub.asObservable();

  private load(): User | null {
    try {
      const raw = sessionStorage.getItem('pf_user');
      return raw ? JSON.parse(raw) : null;
    } catch { return null; }
  }

  get currentUser(): User | null  { return this.sub.value; }
  get isLoggedIn(): boolean       { return !!this.sub.value; }
  get isAdmin(): boolean          { return this.sub.value?.role === 'ADMIN'; }
  get isEmpresario(): boolean     { return this.sub.value?.role === 'EMPRESARIO'; }
  get isTrabajador(): boolean     { return this.sub.value?.role === 'TRABAJADOR'; }

  setUser(user: User): void {
    sessionStorage.setItem('pf_user', JSON.stringify(user));
    this.sub.next(user);
  }

  updateUser(user: User): void { this.setUser(user); }

  logout(): void {
    sessionStorage.removeItem('pf_user');
    this.sub.next(null);
  }
}