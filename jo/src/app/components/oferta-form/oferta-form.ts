import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JobOfferService } from '../../services/job-offer-service';
import { AuthService } from '../../services/auth-service';
import { JobOfferRequest } from '../../models/job-offer';

@Component({ selector: 'app-oferta-form', standalone: false, templateUrl: './oferta-form.html' })
export class OfertaForm implements OnInit {
  isEdit = false;
  offerId: number | null = null;
  loading = false;
  loadingData = false;
  errors: { title?: string; company?: string; general?: string } = {};
  form: JobOfferRequest = {
    title: '', description: '', company: '', location: '',
    salary: '', contractType: '', experienceLevel: '', technologies: '', remote: ''
  };
  contractTypes    = ['Indefinido', 'Temporal', 'Prácticas', 'Freelance', 'Media jornada'];
  experienceLevels = ['Sin experiencia', 'Junior (0-2 años)', 'Mid (2-5 años)', 'Senior (5+ años)', 'Lead'];
  remoteOptions    = ['Presencial', 'Remoto', 'Híbrido'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jobOfferService: JobOfferService,
    public auth: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    if (!this.auth.isEmpresario) { this.router.navigate(['/ofertas']); return; }
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.offerId = Number(id);
      this.loadingData = true;
      this.jobOfferService.getById(this.offerId).subscribe({
        next: (data) => {
          this.form = {
            title: data.title,
            description: data.description || '',
            company: data.company,
            location: data.location || '',
            salary: data.salary || '',
            contractType: data.contractType || '',
            experienceLevel: data.experienceLevel || '',
            technologies: data.technologies || '',
            remote: data.remote || ''
          };
          this.loadingData = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.errors.general = 'Error al cargar la oferta.';
          this.loadingData = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.form.company = this.auth.currentUser?.company || '';
    }
  }

  onSubmit() {
    this.errors = {};
    if (!this.form.title.trim()) this.errors.title = 'El título del puesto es obligatorio.';
    if (!this.form.company.trim()) this.errors.company = 'El nombre de la empresa es obligatorio.';
    if (this.errors.title || this.errors.company) return;
    this.loading = true;
    const userId = this.auth.currentUser!.id;
    const req = this.isEdit
      ? this.jobOfferService.update(this.offerId!, userId, this.form)
      : this.jobOfferService.create(userId, this.form);
    req.subscribe({
      next: (offer) => {
        this.loading = false;
        this.router.navigate(['/ofertas', offer.id]);
      },
      error: (err) => {
        this.loading = false;
        this.errors.general = err.error?.error || 'Error al guardar la oferta.';
        this.cdr.detectChanges();
      }
    });
  }

  cancelar() { this.router.navigate(['/ofertas']); }
}