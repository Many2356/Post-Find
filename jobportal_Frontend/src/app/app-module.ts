import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Navbar } from './components/navbar/navbar';
import { Portada } from './components/portada/portada';
import { Registro } from './components/registro/registro';
import { Login } from './components/login/login';
import { Ofertas } from './components/ofertas/ofertas';
import { OfertaDetalle } from './components/oferta-detalle/oferta-detalle';
import { OfertaForm } from './components/oferta-form/oferta-form';
import { Perfil } from './components/perfil/perfil';
import { provideHttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Candidatos } from './components/candidatos/candidatos';


@NgModule({
  declarations: [
    App,
    Navbar,
    Portada,
    Registro,
    Login,
    Ofertas,
    OfertaDetalle,
    OfertaForm,
    Perfil,
    Candidatos
    
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(),
  ],
  bootstrap: [App]
})
export class AppModule { }
