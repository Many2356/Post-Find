import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.html'
})
export class Navbar {
  menuOpen = false;

  constructor(public auth: AuthService, private router: Router) {}

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  closeMenu() {
    this.menuOpen = false;
  }
}