import { createContext, useContext, useState, useCallback } from 'react';
import * as bouquetApi from '../api/bouquet';

const BouquetContext = createContext(null);

export function BouquetProvider({ children }) {
  const [bouquet, setBouquet] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchBouquet = useCallback(async () => {
    setLoading(true);
    try {
      const { data } = await bouquetApi.getBouquet();
      setBouquet(data);
    } catch { setBouquet(null); }
    finally { setLoading(false); }
  }, []);

  const addItem = async (articleId, quantite = 1) => {
    const { data } = await bouquetApi.addToBouquet(articleId, quantite);
    setBouquet(data);
    return data;
  };

  const updateItem = async (ligneId, quantite) => {
    const { data } = await bouquetApi.updateBouquetQuantite(ligneId, quantite);
    setBouquet(data);
    return data;
  };

  const removeItem = async (ligneId) => {
    const { data } = await bouquetApi.removeFromBouquet(ligneId);
    setBouquet(data);
    return data;
  };

  const clearBouquet = () => setBouquet(null);

  const itemCount = bouquet?.lignes?.reduce((s, l) => s + l.quantite, 0) ?? 0;

  return (
    <BouquetContext.Provider value={{ bouquet, loading, itemCount, fetchBouquet, addItem, updateItem, removeItem, clearBouquet }}>
      {children}
    </BouquetContext.Provider>
  );
}

export const useBouquet = () => {
  const ctx = useContext(BouquetContext);
  if (!ctx) throw new Error('useBouquet must be used within BouquetProvider');
  return ctx;
};
