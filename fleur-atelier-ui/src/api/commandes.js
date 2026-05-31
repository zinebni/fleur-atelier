import client from './client';

export const createCommande  = ()     => client.post('/commandes');
export const getMyCommandes  = ()     => client.get('/commandes');
export const getCommandeById = (id)   => client.get(`/commandes/${id}`);

/** payload: { commandeId, modePaiement, adresse, ville, codePostal, telephone, pays } */
export const payerCommande = (payload) => client.post('/paiements', payload);
