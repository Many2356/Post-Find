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
  applying = false;
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
    private cdr: ChangeDetectorRef   //Se añade para que la pagina no este todo el tiempo cargando
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
      next: () => {
        this.hasSolicited = true;
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