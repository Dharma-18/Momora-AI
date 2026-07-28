import os
import chromadb
from chromadb.config import Settings as ChromaSettings
from typing import List, Dict, Any
from app.config import settings
from app.services.embedding_service import get_embedding_service

class VectorStore:
    def __init__(self):
        os.makedirs(settings.CHROMA_PERSIST_DIR, exist_ok=True)
        self.client = chromadb.PersistentClient(path=settings.CHROMA_PERSIST_DIR)
        self.embedding_service = get_embedding_service()

    def get_user_collection(self, user_id: str):
        collection_name = f"user_{user_id.replace('-', '_')}"
        return self.client.get_or_create_collection(
            name=collection_name,
            metadata={"hnsw:space": "cosine"}
        )

    def add_documents(
        self,
        user_id: str,
        documents: List[str],
        metadatas: List[Dict[str, Any]],
        ids: List[str]
    ):
        collection = self.get_user_collection(user_id)
        embeddings = self.embedding_service.embed_documents(documents)
        collection.add(
            documents=documents,
            embeddings=embeddings,
            metadatas=metadatas,
            ids=ids
        )

    def similarity_search(
        self,
        user_id: str,
        query: str,
        top_k: int = 5
    ) -> List[Dict[str, Any]]:
        collection = self.get_user_collection(user_id)
        if collection.count() == 0:
            return []

        query_embedding = self.embedding_service.embed_query(query)
        results = collection.query(
            query_embeddings=[query_embedding],
            n_results=min(top_k, collection.count())
        )

        formatted_results = []
        if results and results['documents']:
            docs = results['documents'][0]
            metas = results['metadatas'][0]
            distances = results['distances'][0]
            ids = results['ids'][0]

            for doc, meta, dist, doc_id in zip(docs, metas, distances, ids):
                # Convert cosine distance to similarity score
                similarity = max(0.0, min(1.0, 1.0 - dist))
                formatted_results.append({
                    "id": doc_id,
                    "content": doc,
                    "metadata": meta,
                    "score": round(similarity, 3)
                })

        return formatted_results

    def delete_document(self, user_id: str, document_id: str):
        collection = self.get_user_collection(user_id)
        collection.delete(where={"document_id": document_id})

_vector_store_instance = None

def get_vector_store() -> VectorStore:
    global _vector_store_instance
    if _vector_store_instance is None:
        _vector_store_instance = VectorStore()
    return _vector_store_instance
