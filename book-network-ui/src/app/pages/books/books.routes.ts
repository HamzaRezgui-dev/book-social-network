import { Routes } from '@angular/router';
import { BooksComponent } from './books.component';

export const routes: Routes = [
  {
    path: '',
    component: BooksComponent,
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./pages/main/main.component').then((m) => m.MainComponent),
      },
    ],
  },
];
