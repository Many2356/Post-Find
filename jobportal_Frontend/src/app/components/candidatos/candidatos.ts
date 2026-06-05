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
  updatingId: number | null = null;

  
  selectedStatus: { [id: number]: string } = {};

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

    this.jobOfferService.getById(offerId).subscribe({
      next: (oferta) => {
        if (oferta.employerId !== this.auth.currentUser!.id) {
          this.error = 'No tienes permiso para ver los candidatos de esta oferta.';
          this.loading = false;
          this.cdr.detectChanges();
          return;
        }
        this.oferta = oferta;

        this.applicationService.getByOffer(offerId).subscribe({
          next: (apps) => {
            this.applications = apps;
            // Inicializar el mapa con el estado real de cada solicitud
            apps.forEach(a => this.selectedStatus[a.id] = a.status);
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
        // Actualizar tanto el objeto como el mapa
        app.status = updated.status;
        this.selectedStatus[app.id] = updated.status;
        this.updatingId = null;
        this.cdr.detectChanges();
      },
      error: (err) => {
        // Revertir el select al valor anterior si falla
        this.selectedStatus[app.id] = app.status;
        alert(err.error?.error || 'Error al cambiar el estado.');
        this.updatingId = null;
        this.cdr.detectChanges();
      }
    });
  }

  getStatusClass(status: string): string {
    const map: any = { PENDIENTE: 'warning', REVISADO: 'info', ACEPTADO: 'success', RECHAZADO: 'danger' };
    return map[status] || 'secondary';
  }

  volver() {
    this.router.navigate(['/ofertas', this.oferta?.id]);
  }
}