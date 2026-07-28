from fastapi import APIRouter, HTTPException
from app.models.schemas import SearchRequest, SearchResponse, SearchResultItem
from app.db.vector_store import get_vector_store

router = APIRouter(prefix="/search", tags=["Search"])

@router.post("", response_model=SearchResponse)
async def vector_search(request: SearchRequest):
    if not request.query.strip():
        raise HTTPException(status_code=400, detail="Query cannot be empty")

    try:
        vector_store = get_vector_store()
        raw_results = vector_store.similarity_search(
            user_id=request.user_id,
            query=request.query,
            top_k=request.top_k
        )

        formatted_results = []
        for r in raw_results:
            meta = r.get("metadata", {})
            formatted_results.append(SearchResultItem(
                content=r.get("content", ""),
                source_type=meta.get("source_type", "Document"),
                filename=meta.get("filename", "Unknown"),
                score=r.get("score", 0.0),
                metadata=meta
            ))

        return SearchResponse(
            query=request.query,
            results=formatted_results
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Search error: {str(e)}")
