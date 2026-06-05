import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth-service';

@Component({ selector: 'app-login', standalone: false, templateUrl: './login.html' })
export class Login {
  form = { 
    username: '', 
    password: '' 
  };
  errors: { 
    username?: string; 
    password?: string; 
    general?: string 
  } = {};
  loading = false;

  constructor(
    private userService: UserService,
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  onSubmit() {
    this.errors = {};
    if (!this.form.username.trim()) this.errors.username = 'El usuario es obligatorio.';
    if (!this.form.password) this.errors.password = 'La contraseña es obligatoria.';
    if (this.errors.username || this.errors.password) return;
    this.loading = true;
    this.userService.login(this.form).subscribe({
      next: (user) => {
        this.loading = false;
        this.auth.setUser(user);
        this.router.navigate([user.role === 'ADMIN' ? '/admin/usuarios' : '/ofertas']);
      },
      error: (err) => {
        this.loading = false;
        this.errors.general = err.error?.error || 'Usuario o contraseña incorrectos.';
        this.cdr.detectChanges();
      }
    });
  }
}