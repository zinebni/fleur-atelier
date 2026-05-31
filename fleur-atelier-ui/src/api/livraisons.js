import client from './client';

export const getLivraisonByCommande = (commandeId) =>
  client.get(`/livraisons/commande/${commandeId}`);

export const updateLivraisonStatut = (livraisonId, statut) =>
  client.patch(`/livraisons/${livraisonId}/statut`, null, { params: { statut } });

export const getAllLivraisons = () => client.get('/livraisons');
