from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, Field

# --- Document Schemas ---

class DocumentMetadata(BaseModel):
    filename: str
    source_type: str = "PDF"  # PDF, WhatsApp, Email, Note, Image
    upload_date: str = Field(default_factory=lambda: datetime.now().isoformat())
    user_id: str = "default_user"
    category: Optional[str] = "General"

class DocumentUploadResponse(BaseModel):
    document_id: str
    filename: str
    source_type: str
    num_chunks: int
    message: str

class DocumentInfo(BaseModel):
    document_id: str
    filename: str
    source_type: str
    upload_date: str
    num_chunks: int

# --- RAG & Chat Schemas ---

class SourceCitation(BaseModel):
    type: str
    detail: str
    confidence: int

class ChatRequest(BaseModel):
    user_id: str = "default_user"
    query: str
    session_id: Optional[str] = "default_session"

class ChatResponse(BaseModel):
    query: str
    answer: str
    sources: List[SourceCitation]
    timestamp: str = Field(default_factory=lambda: datetime.now().isoformat())

# --- Search Schemas ---

class SearchRequest(BaseModel):
    user_id: str = "default_user"
    query: str
    top_k: int = 5

class SearchResultItem(BaseModel):
    content: str
    source_type: str
    filename: str
    score: float
    metadata: dict

class SearchResponse(BaseModel):
    query: str
    results: List[SearchResultItem]
