from typing import List, Dict, Any
from app.db.vector_store import get_vector_store
from app.services.llm_service import get_llm_service
from app.models.schemas import SourceCitation, ChatResponse

class RAGService:
    def __init__(self):
        self.vector_store = get_vector_store()
        self.llm_service = get_llm_service()

    def query(self, user_id: str, query: str) -> ChatResponse:
        # Step 1: Semantic similarity search in ChromaDB
        search_results = self.vector_store.similarity_search(
            user_id=user_id,
            query=query,
            top_k=5
        )

        # Step 2: Extract sources & calculate confidence
        sources: List[SourceCitation] = []
        seen_sources = set()

        for result in search_results:
            meta = result.get("metadata", {})
            source_type = meta.get("source_type", "PDF")
            filename = meta.get("filename", "Document")
            score = result.get("score", 0.8)

            # Calculate confidence percentage (e.g. 0.85 score -> 88%)
            confidence = int(min(99, max(50, score * 100)))

            source_key = f"{source_type}:{filename}"
            if source_key not in seen_sources:
                seen_sources.add(source_key)
                sources.append(SourceCitation(
                    type=source_type,
                    detail=filename,
                    confidence=confidence
                ))

        # Step 3: LLM generation with retrieved context
        answer = self.llm_service.generate_rag_response(
            query=query,
            context_chunks=search_results
        )

        return ChatResponse(
            query=query,
            answer=answer,
            sources=sources
        )

_rag_service_instance = None

def get_rag_service() -> RAGService:
    global _rag_service_instance
    if _rag_service_instance is None:
        _rag_service_instance = RAGService()
    return _rag_service_instance
