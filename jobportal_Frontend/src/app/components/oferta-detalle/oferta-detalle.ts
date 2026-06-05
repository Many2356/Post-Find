import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JobOfferService } from '../../services/job-offer-service';
import { ApplicationService } from '../../services/application-service';
import { AuthService } from '../../services/auth-service';
import { JobOffer } from '../../models/job-offer';

@Component({
  selector: 'app-oferta-detalle',
  standalone: false,
  templateUrl: './oferta-detalle.html'
})
export class OfertaDetalle implements OnInit {
  oferta: JobOffer | null = null;
  loading = true;
  error = '';
  hasSolicited = false;
  applicationId: number | null = null;   // guardar el id de la solicitud
  applying = false;
  withdrawing = false;                   // estado de carga al retirar
  applySuccess = false;
  applyError = '';
  coverLetter = '';
  deletingOffer = false;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private jobOfferService: JobOfferService,
    private applicationService: ApplicationService,
    public auth: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id || isNaN(id)) {
      this.error = 'Oferta no encontrada.';
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    this.jobOfferService.getById(id).subscribe({
      next: (data) => {
        this.oferta = data;
        this.loading = false;
        this.cdr.detectChanges();

        if (this.auth.isTrabajador && this.auth.currentUser) {
          this.applicationService.hasApplied(this.auth.currentUser.id, id).subscribe({
            next: (res) => {
              this.hasSolicited = res.applied;
              // ← NUEVO: si ya solicitó, buscamos el id de la solicitud
              if (res.applied) {
                this.applicationService.getByUser(this.auth.currentUser!.id).subscribe({
                  next: (apps) => {
                    const found = apps.find(a => a.jobOfferId === id);
                    if (found) this.applicationId = found.id;
                    this.cdr.detectChanges();
                  }
                });
              }
              this.cdr.detectChanges();
            },
            error: () => { this.hasSolicited = false; }
          });
        }
      },
      error: () => {
        this.error = 'Oferta no encontrada o no disponible.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  solicitar() {
    if (!this.auth.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }
    this.applying = true;
    this.applyError = '';
    this.applicationService.apply(
      this.oferta!.id,
      this.auth.currentUser!.id,
      { coverLetter: this.coverLetter }
    ).subscribe({
      next: (app) => {
        this.hasSolicited = true;
        this.applicationId = app.id;   // ← NUEVO: guardamos el id devuelto por el backend
        this.applying = false;
        this.applySuccess = true;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.applyError = err.error?.error || 'Error al solicitar. Inténtalo de nuevo.';
        this.applying = false;
        this.cdr.detectChanges();
      }
    });
  }

  // método para retirar la solicitud
  retirarSolicitud() {
    if (!this.applicationId) return;
    if (!confirm('¿Retirar tu solicitud para esta oferta?')) return;
    this.withdrawing = true;
    this.applyError = '';
    this.applicationService.withdraw(this.applicationId, this.auth.currentUser!.id).subscribe({
      next: () => {
        this.hasSolicited = false;
        this.applicationId = null;
        this.applySuccess = false;
        this.coverLetter = '';
        this.withdrawing = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.applyError = err.error?.error || 'Error al retirar la solicitud.';
        this.withdrawing = false;
        this.cdr.detectChanges();
      }
    });
  }

  editarOferta() {
    this.router.navigate(['/ofertas/editar', this.oferta!.id]);
  }

  eliminarOferta() {
    if (!confirm('¿Eliminar esta oferta? Esta acción no se puede deshacer.')) return;
    this.deletingOffer = true;
    this.jobOfferService.delete(this.oferta!.id, this.auth.currentUser!.id).subscribe({
      next: () => this.router.navigate(['/ofertas']),
      error: (err) => {
        alert(err.error?.error || 'Error al eliminar');
        this.deletingOffer = false;
        this.cdr.detectChanges();
      }
    });
  }

  getTechs(): string[] {
    if (!this.oferta?.technologies) return [];
    return this.oferta.technologies.split(',').map(t => t.trim()).filter(t => t);
  }

  isOwner(): boolean {
    return this.auth.isEmpresario && this.auth.currentUser?.id === this.oferta?.employerId;
  }
}