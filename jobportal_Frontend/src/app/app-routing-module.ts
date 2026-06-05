import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Portada } from './components/portada/portada';
import { Registro } from './components/registro/registro';
import { Login } from './components/login/login';
import { Ofertas } from './components/ofertas/ofertas';
import { OfertaForm } from './components/oferta-form/oferta-form';
import { OfertaDetalle } from './components/oferta-detalle/oferta-detalle';
import { Perfil } from './components/perfil/perfil';
import { Candidatos } from './components/candidatos/candidatos';

const routes: Routes = [
  { 
    path: '', 
    component: Portada
  },
  { 
    path: 'registro', 
    component: Registro
  },
  { 
    path: 'login', 
    component: Login 
  },
  { 
    path: 'ofertas', 
    component: Ofertas
  },
  { 
    path: 'ofertas/nueva', 
    component: OfertaForm 
  },
  { 
    path: 'ofertas/editar/:id', 
    component: OfertaForm 
  },
  { 
    path: 'ofertas/:id', 
    component: OfertaDetalle
  },
  {
    path: 'ofertas/:id/candidatos',
    component: Candidatos
  },
  { 
    path: 'perfil', 
    component: Perfil
  },
  { 
    path: '**', 
    redirectTo: '' 
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
