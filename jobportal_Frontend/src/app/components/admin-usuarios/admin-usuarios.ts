import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { UserService } from '../../services/user-service';
import { User } from '../../models/user';
import { AdminUserRequest } from '../../models/admin';

@Component({
  selector: 'app-admin-usuarios',
  standalone: false,
  templateUrl: './admin-usuarios.html'
})
export class AdminUsuarios implements OnInit {

  users: User[] = [];
  loading = true;
  error = '';

  // crear/editar
  showModal = false;
  modalMode: 'crear' | 'editar' = 'crear';
  selectedUser: User | null = null;
  saving = false;
  modalError = '';
  modalSuccess = '';

  form: AdminUserRequest & { passwordConfirm?: string } = {};

  // Búsqueda / filtro
  search = '';
  filterRole = '';

  constructor(
    public auth: AuthService,
    private userService: UserService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    if (!this.auth.isAdmin) { this.router.navigate(['/']); return; }
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (data) => { this.users = data; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.error = 'Error al cargar los usuarios.'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  get filteredUsers(): User[] {
    return this.users.filter(u => {
      const matchSearch = !this.search ||
        u.username.toLowerCase().includes(this.search.toLowerCase()) ||
        u.email.toLowerCase().includes(this.search.toLowerCase()) ||
        (u.fullName || '').toLowerCase().includes(this.search.toLowerCase());
      const matchRole = !this.filterRole || u.role === this.filterRole;
      return matchSearch && matchRole;
    });
  }

  
  openCrear() {
    this.modalMode = 'crear';
    this.selectedUser = null;
    this.form = { role: 'TRABAJADOR' };
    this.modalError = '';
    this.modalSuccess = '';
    this.showModal = true;
  }

  openEditar(user: User) {
    this.modalMode = 'editar';
    this.selectedUser = user;
    this.form = {
      username: user.username,
      email:    user.email,
      role:     user.role,
      fullName: user.fullName || '',
      company:  user.company  || '',
      password: '',
      passwordConfirm: ''
    };
    this.modalError = '';
    this.modalSuccess = '';
    this.showModal = true;
  }

  closeModal() { this.showModal = false; }

  save() {
    this.modalError = '';
    // Validaciones básicas
    if (!this.form.username?.trim()) { this.modalError = 'El username es obligatorio.'; return; }
    if (!this.form.email?.trim())    { this.modalError = 'El email es obligatorio.'; return; }
    if (this.modalMode === 'crear' && !this.form.password?.trim()) {
      this.modalError = 'La contraseña es obligatoria al crear un usuario.'; return;
    }
    if (this.form.password && this.form.password !== this.form.passwordConfirm) {
      this.modalError = 'Las contraseñas no coinciden.'; return;
    }

    const payload: AdminUserRequest = {
      username: this.form.username,
      email:    this.form.email,
      role:     this.form.role,
      fullName: this.form.fullName,
      company:  this.form.company
    };
    if (this.form.password) payload.password = this.form.password;

    this.saving = true;
    const req = this.modalMode === 'crear'
      ? this.userService.adminCreateUser(payload)
      : this.userService.adminUpdateUser(this.selectedUser!.id, payload);

    req.subscribe({
      next: (user) => {
        if (this.modalMode === 'crear') {
          this.users = [user, ...this.users];
        } else {
          this.users = this.users.map(u => u.id === user.id ? user : u);
        }
        this.saving = false;
        this.modalSuccess = this.modalMode === 'crear' ? 'Usuario creado correctamente.' : 'Usuario actualizado.';
        this.cdr.detectChanges();
        setTimeout(() => { this.showModal = false; this.modalSuccess = ''; this.cdr.detectChanges(); }, 1200);
      },
      error: (err) => {
        this.saving = false;
        this.modalError = err.error?.error || 'Error al guardar.';
        this.cdr.detectChanges();
      }
    });
  }

  // ─ Eliminar
  eliminar(user: User) {
    if (!confirm(`¿Eliminar al usuario "${user.username}"? Esta acción no se puede deshacer.`)) return;
    this.userService.adminDeleteUser(user.id).subscribe({
      next: () => { this.users = this.users.filter(u => u.id !== user.id); this.cdr.detectChanges(); },
      error: (err) => { alert(err.error?.error || 'Error al eliminar.'); }
    });
  }

  getRoleBadge(role: string): string {
    const map: Record<string, string> = {
      TRABAJADOR: 'primary',
      EMPRESARIO: 'warning',
      ADMIN:      'danger'
    };
    return map[role] || 'secondary';
  }

  getRoleLabel(role: string): string {
    const map: Record<string, string> = { TRABAJADOR: 'Trabajador', EMPRESARIO: 'Empresario', ADMIN: 'Admin' };
    return map[role] || role;
  }

  getInicial(username: string): string { return username ? username.charAt(0).toUpperCase() : '?'; }
}