import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { AuthService } from '../../services/auth-service';
import { ApplicationService } from '../../services/application-service';
import { JobOfferService } from '../../services/job-offer-service';
import { UpdateProfileRequest } from '../../models/user';
import { Application } from '../../models/application';
import { JobOffer } from '../../models/job-offer';

@Component({ selector: 'app-perfil', standalone: false, templateUrl: './perfil.html' })
export class Perfil implements OnInit {
  editMode = false;
  loading = false;
  success = '';
  deleteConfirm = '';
  profileErrors: { email?: string; general?: string } = {};
  deleteError = '';
  form: UpdateProfileRequest = {};
  applications: Application[] = [];
  myOffers: JobOffer[] = [];

  constructor(
    public auth: AuthService,
    private userService: UserService,
    private applicationService: ApplicationService,
    private jobOfferService: JobOfferService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    if (!this.auth.isLoggedIn) { this.router.navigate(['/login']); return; }
    this.resetForm();
    if (this.auth.isTrabajador) {
      this.applicationService.getByUser(this.auth.currentUser!.id).subscribe({
        next: (data) => { this.applications = data; this.cdr.detectChanges(); }
      });
    }
    if (this.auth.isEmpresario) {
      this.jobOfferService.getByEmployer(this.auth.currentUser!.id).subscribe({
        next: (data) => { this.myOffers = data; this.cdr.detectChanges(); }
      });
    }
  }

  resetForm() {
    const u = this.auth.currentUser!;
    this.form = {
      fullName: u.fullName || '',
      bio:      u.bio      || '',
      location: u.location || '',
      website:  u.website  || '',
      skills:   u.skills   || '',
      company:  u.company  || '',
      email:    u.email
    };
  }

  startEdit()  { this.editMode = true;  this.profileErrors = {}; this.success = ''; }
  cancelEdit() { this.editMode = false; this.resetForm(); this.profileErrors = {}; }

  saveProfile() {
    this.profileErrors = {};
    if (!this.form.email?.trim()) {
      this.profileErrors.email = 'El email es obligatorio.';
    } else if (!this.form.email.includes('@')) {
      this.profileErrors.email = 'Introduce un email válido.';
    }
    if (this.profileErrors.email) return;
    this.loading = true;
    this.userService.updateProfile(this.auth.currentUser!.id, this.form).subscribe({
      next: (user) => {
        this.auth.updateUser(user);
        this.editMode = false;
        this.loading = false;
        this.success = 'Perfil actualizado correctamente.';
        this.cdr.detectChanges();
        setTimeout(() => { this.success = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (err) => {
        this.loading = false;
        const msg: string = err.error?.error || '';
        if (msg.toLowerCase().includes('email')) {
          this.profileErrors.email = msg;
        } else {
          this.profileErrors.general = msg || 'Error al actualizar el perfil.';
        }
        this.cdr.detectChanges();
      }
    });
  }

  deleteAccount() {
    this.deleteError = '';
    if (this.deleteConfirm !== this.auth.currentUser!.username) {
      this.deleteError = 'El nombre de usuario no coincide.';
      return;
    }
    this.userService.deleteAccount(this.auth.currentUser!.id).subscribe({
      next: () => { this.auth.logout(); this.router.navigate(['/']); },
      error: (err) => {
        this.deleteError = err.error?.error || 'Error al eliminar la cuenta.';
        this.cdr.detectChanges();
      }
    });
  }

  getInicial(username: string): string { return username ? username.charAt(0).toUpperCase() : '?'; }
  getStatusClass(status: string): string {
    const map: any = { PENDIENTE: 'warning', REVISADO: 'info', ACEPTADO: 'success', RECHAZADO: 'danger' };
    return map[status] || 'secondary';
  }
  verOferta(id: number)    { 
    this.router.navigate(['/ofertas', id]); 
  }
  editarOferta(id: number) { 
    this.router.navigate(['/ofertas/editar', id]); 
  }
  eliminarOferta(id: number) {
    if (!confirm('¿Eliminar esta oferta?')) return;
    this.jobOfferService.delete(id, this.auth.currentUser!.id).subscribe({
      next: () => { 
        this.myOffers = this.myOffers.filter(o => o.id !== id); this.cdr.detectChanges(); 
      },
      error: (err) => { 
        alert(err.error?.error || 'Error al eliminar'); 
      }
    });
  }
}