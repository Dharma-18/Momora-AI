from fastapi import APIRouter, HTTPException
from app.models.schemas import ChatRequest, ChatResponse
from app.services.rag_service import get_rag_service

router = APIRouter(prefix="/chat", tags=["Chat"])

@router.post("", response_model=ChatResponse)
async def chat_rag(request: ChatRequest):
    if not request.query.strip():
        raise HTTPException(status_code=400, detail="Query cannot be empty")

    try:
        rag_service = get_rag_service()
        response = rag_service.query(
            user_id=request.user_id,
            query=request.query
        )
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"RAG query error: {str(e)}")
