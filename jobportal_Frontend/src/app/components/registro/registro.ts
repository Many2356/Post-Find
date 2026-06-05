import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth-service';

@Component({ selector: 'app-registro', standalone: false, templateUrl: './registro.html' })
export class Registro {
  form = { username: '', email: '', password: '', role: 'TRABAJADOR', fullName: '', company: '' };
  errors: { username?: string; email?: string; password?: string; company?: string; general?: string } = {};
  loading = false;

  constructor(
    private userService: UserService,
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  onSubmit() {
    this.errors = {};
    if (!this.form.username.trim()) {
      this.errors.username = 'El nombre de usuario es obligatorio.';
    } else if (this.form.username.trim().length < 3) {
      this.errors.username = 'El usuario debe tener al menos 3 caracteres.';
    }
    if (!this.form.email.trim()) {
      this.errors.email = 'El email es obligatorio.';
    } else if (!this.form.email.includes('@')) {
      this.errors.email = 'Introduce un email válido.';
    }
    if (!this.form.password) {
      this.errors.password = 'La contraseña es obligatoria.';
    } else if (this.form.password.length < 6) {
      this.errors.password = 'La contraseña debe tener al menos 6 caracteres.';
    }
    if (this.form.role === 'EMPRESARIO' && !this.form.company.trim()) {
      this.errors.company = 'El nombre de la empresa es obligatorio.';
    }
    if (Object.keys(this.errors).length > 0) return;
    this.loading = true;
    this.userService.register(this.form).subscribe({
      next: (user) => {
        this.loading = false;
        this.auth.setUser(user);
        this.router.navigate(['/ofertas']);
      },
      error: (err) => {
        this.loading = false;
        const msg: string = err.error?.error || '';
        if (msg.toLowerCase().includes('usuario') || msg.toLowerCase().includes('username')) {
          this.errors.username = msg;
        } else if (msg.toLowerCase().includes('email')) {
          this.errors.email = msg;
        } else {
          this.errors.general = msg || 'Error al registrar. Inténtalo de nuevo.';
        }
        this.cdr.detectChanges();
      }
    });
  }
}