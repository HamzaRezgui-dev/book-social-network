import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss',
})
export class MenuComponent implements OnInit {
  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach((l) => {
      l.addEventListener('click', () => {
        linkColor.forEach((link) => link.classList.remove('active'));
        l.classList.add('active');
      });
    });
  }
  logout() {
    throw new Error('Method not implemented.');
  }
}
