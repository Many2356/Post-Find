import { Component } from '@angular/core';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-portada',
  standalone: false,
  templateUrl: './portada.html'
})
export class Portada {
  constructor(public auth: AuthService) {}

  phrases = [
    { icon: 'bi-rocket-takeoff-fill', text: 'Conectamos talento con oportunidades reales' },
    { icon: 'bi-lightning-charge-fill', text: 'El trabajo de tus sueños está a un clic' },
    { icon: 'bi-people-fill', text: 'Miles de empresas buscan tu perfil hoy' }
  ];

  stats = [
    { value: '2,400+', label: 'Ofertas activas', icon: 'bi-briefcase-fill', color: '#60a5fa' },
    { value: '850+', label: 'Empresas', icon: 'bi-building-fill', color: '#c4b5fd' },
    { value: '12,000+', label: 'Desarrolladores', icon: 'bi-people-fill', color: '#6ee7b7' }
  ];

  techs = ['Angular', 'React', 'Spring Boot', 'Node.js', 'Python', 'Java', 'Docker', 'AWS'];
}
