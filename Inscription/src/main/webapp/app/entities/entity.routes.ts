import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'candidat',
    data: { pageTitle: 'inscriptionApp.candidat.home.title' },
    loadChildren: () => import('./candidat/candidat.routes'),
  },
  {
    path: 'piece-justificative',
    data: { pageTitle: 'inscriptionApp.pieceJustificative.home.title' },
    loadChildren: () => import('./piece-justificative/piece-justificative.routes'),
  },
  {
    path: 'parcours',
    data: { pageTitle: 'inscriptionApp.parcours.home.title' },
    loadChildren: () => import('./parcours/parcours.routes'),
  },
  {
    path: 'contact-urgence',
    data: { pageTitle: 'inscriptionApp.contactUrgence.home.title' },
    loadChildren: () => import('./contact-urgence/contact-urgence.routes'),
  },
  {
    path: 'dossier',
    data: { pageTitle: 'inscriptionApp.dossier.home.title' },
    loadChildren: () => import('./dossier/dossier.routes'),
  },
  {
    path: 'rendez-vous',
    data: { pageTitle: 'inscriptionApp.rendezVous.home.title' },
    loadChildren: () => import('./rendez-vous/rendez-vous.routes'),
  },
  {
    path: 'message',
    data: { pageTitle: 'inscriptionApp.message.home.title' },
    loadChildren: () => import('./message/message.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
