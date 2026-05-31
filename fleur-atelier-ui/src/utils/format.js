export const formatPrice = (amount) =>
  new Intl.NumberFormat('fr-MA', {
    style: 'currency',
    currency: 'MAD'
  }).format(amount ?? 0);

export const formatDate = (isoString) => {
  if (!isoString) return '—';
  return new Intl.DateTimeFormat('fr-FR', {
    day: '2-digit', month: 'long', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  }).format(new Date(isoString));
};

export const STATUS_LABEL = {
  EN_ATTENTE: 'En attente',
  PAYEE:      'Payée',
  ANNULEE:    'Annulée',
};

export const STATUS_CLASS = {
  EN_ATTENTE: 'badge--warning',
  PAYEE:      'badge--success',
  ANNULEE:    'badge--error',
};

export const DELIVERY_STATUS_LABEL = {
  EN_PREPARATION: 'En préparation',
  EXPEDIEE:       'Expédiée',
  EN_ROUTE:       'En route',
  LIVREE:         'Livrée',
};

export const DELIVERY_STATUS_CLASS = {
  EN_PREPARATION: 'badge--warning',
  EXPEDIEE:       'badge--info',
  EN_ROUTE:       'badge--info',
  LIVREE:         'badge--success',
};

export const DELIVERY_STEPS = ['EN_PREPARATION', 'EXPEDIEE', 'EN_ROUTE', 'LIVREE'];

