import client from './client';

export const getBouquet            = ()                  => client.get('/bouquet');
export const addToBouquet          = (articleId, quantite) => client.post('/bouquet/ajouter', { articleId, quantite });
export const updateBouquetQuantite = (ligneId, quantite)  => client.put(`/bouquet/${ligneId}`, { quantite });
export const removeFromBouquet     = (ligneId)            => client.delete(`/bouquet/${ligneId}`);
