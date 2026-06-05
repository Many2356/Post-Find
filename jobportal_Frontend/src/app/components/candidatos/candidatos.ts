import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from '../../services/application-service';
import { JobOfferService } from '../../services/job-offer-service';
import { AuthService } from '../../services/auth-service';
import { Application } from '../../models/application';
import { JobOffer } from '../../models/job-offer';

@Component({
  selector: 'app-candidatos',
  standalone: false,
  templateUrl: './candidatos.html'
})
export class Candidatos implements OnInit {
  oferta: JobOffer | null = null;
  applications: Application[] = [];
  loading = true;
  error = '';
  updatingId: number | null = null;  // id de la solicitud que está actualizándose

  readonly ESTADOS = ['PENDIENTE', 'REVISADO', 'ACEPTADO', 'RECHAZADO'];

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private applicationService: ApplicationService,
    private jobOfferService: JobOfferService,
    public auth: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    // Solo empresarios pueden entrar
    if (!this.auth.isEmpresario) {
      this.router.navigate(['/']);
      return;
    }

    const offerId = Number(this.route.snapshot.paramMap.get('id'));

    if (!offerId || isNaN(offerId)) {
      this.error = 'Oferta no válida.';
      this.loading = false;
      return;
    }

    // Cargar datos de la oferta para mostrar el título
    this.jobOfferService.getById(offerId).subscribe({
      next: (oferta) => {
        // Verificar que la oferta pertenece a este empresario
        if (oferta.employerId !== this.auth.currentUser!.id) {
          this.error = 'No tienes permiso para ver los candidatos de esta oferta.';
          this.loading = false;
          this.cdr.detectChanges();
          return;
        }
        this.oferta = oferta;

        // Cargar candidatos
        this.applicationService.getByOffer(offerId).subscribe({
          next: (apps) => {
            this.applications = apps;
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: () => {
            this.error = 'Error al cargar los candidatos.';
            this.loading = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: () => {
        this.error = 'Oferta no encontrada.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  cambiarEstado(app: Application, nuevoEstado: string) {
    if (app.status === nuevoEstado) return;
    this.updatingId = app.id;

    this.applicationService.updateStatus(app.id, this.auth.currentUser!.id, nuevoEstado).subscribe({
      next: (updated) => {
        // Actualizar el estado en la lista local sin recargar
        const idx = this.applications.findIndex(a => a.id === app.id);
        if (idx !== -1) this.applications[idx].status = updated.status;
        this.updatingId = null;
        this.cdr.detectChanges();
      },
      error: (err) => {
        alert(err.error?.error || 'Error al cambiar el estado.');
        this.updatingId = null;
        this.cdr.detectChanges();
      }
    });
  }

  getStatusClass(status: string): string {
    const map: any = {
      PENDIENTE: 'warning',
      REVISADO: 'info',
      ACEPTADO: 'success',
      RECHAZADO: 'danger'
    };
    return map[status] || 'secondary';
  }

  getStatusIcon(status: string): string {
    const map: any = {
      PENDIENTE: 'bi-clock',
      REVISADO: 'bi-eye',
      ACEPTADO: 'bi-check-circle-fill',
      RECHAZADO: 'bi-x-circle-fill'
    };
    return map[status] || 'bi-circle';
  }

  volver() {
    this.router.navigate(['/ofertas', this.oferta?.id]);
  }
}