import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { JobOfferService } from '../../services/job-offer-service';
import { AuthService } from '../../services/auth-service';
import { JobOffer } from '../../models/job-offer';

@Component({ selector: 'app-ofertas', standalone: false, templateUrl: './ofertas.html' })
export class Ofertas implements OnInit {
  todasOfertas: JobOffer[] = [];
  ofertas: JobOffer[] = [];
  estado: 'idle' | 'loading' | 'done' | 'error' = 'idle';
  search = '';
  error = '';

  constructor(
    public auth: AuthService,
    private jobOfferService: JobOfferService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.cargarTodas();
  }

  cargarTodas() {
    this.estado = 'loading';
    this.error = '';
    this.jobOfferService.getAll().subscribe({
      next: (data) => {
        this.todasOfertas = data;
        this.ofertas = data;
        this.estado = 'done';
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudo conectar con el servidor. Comprueba tu conexión.';
        this.estado = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  buscarEnServidor() {
    const q = this.search.trim();
    if (!q) { this.cargarTodas(); return; }
    this.estado = 'loading';
    this.error = '';
    this.jobOfferService.getAll(q).subscribe({
      next: (data) => {
        this.todasOfertas = data;
        this.ofertas = data;
        this.estado = 'done';
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al buscar ofertas. Comprueba tu conexión.';
        this.estado = 'error';
        this.cdr.detectChanges();
      }
    });
  }

  onSearchKey(event: KeyboardEvent) {
    if (event.key === 'Enter') this.buscarEnServidor();
  }

  limpiar() {
    this.search = '';
    this.ofertas = [...this.todasOfertas];
    this.cdr.detectChanges();
  }

  verDetalle(id: number) { this.router.navigate(['/ofertas', id]); }
  nuevaOferta()           { this.router.navigate(['/ofertas/nueva']); }

  getInicial(company: string): string {
    return company ? company.charAt(0).toUpperCase() : '?';
  }

  getTechs(technologies: string | undefined): string[] {
    if (!technologies) return [];
    return technologies.split(',').map(t => t.trim()).filter(t => t).slice(0, 4);
  }
}