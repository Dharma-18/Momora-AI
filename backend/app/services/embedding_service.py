import os
from typing import List
from langchain_community.embeddings import HuggingFaceEmbeddings
from app.config import settings

class EmbeddingService:
    def __init__(self):
        # Load high-quality semantic model bge-small-en-v1.5
        model_name = settings.EMBEDDING_MODEL
        print(f"Loading embedding model: {model_name}...")
        self.embeddings = HuggingFaceEmbeddings(
            model_name=model_name,
            model_kwargs={'device': 'cpu'},
            encode_kwargs={'normalize_embeddings': True}
        )
        print("Embedding model loaded successfully.")

    def embed_query(self, text: str) -> List[float]:
        return self.embeddings.embed_query(text)

    def embed_documents(self, texts: List[str]) -> List[List[float]]:
        return self.embeddings.embed_documents(texts)

_embedding_service_instance = None

def get_embedding_service() -> EmbeddingService:
    global _embedding_service_instance
    if _embedding_service_instance is None:
        _embedding_service_instance = EmbeddingService()
    return _embedding_service_instance
