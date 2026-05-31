import client from './client';

export const getAllArticles  = ()         => client.get('/articles');
export const getArticleById = (id)        => client.get(`/articles/${id}`);
export const searchArticles = (q)         => client.get(`/articles/search?q=${encodeURIComponent(q)}`);
export const createArticle  = (data)      => client.post('/articles', data);
export const updateArticle  = (id, data)  => client.put(`/articles/${id}`, data);
export const deleteArticle  = (id)        => client.delete(`/articles/${id}`);
