import os
import shutil
import tempfile
from fastapi import APIRouter, UploadFile, File, Form, HTTPException
from app.models.schemas import DocumentUploadResponse, TextIngestRequest, TextIngestResponse
from app.services.document_service import get_document_service

router = APIRouter(prefix="/documents", tags=["Documents"])

@router.post("/upload", response_model=DocumentUploadResponse)
async def upload_document(
    file: UploadFile = File(...),
    source_type: str = Form("PDF"),
    user_id: str = Form("default_user"),
    category: str = Form("General")
):
    if not file.filename:
        raise HTTPException(status_code=400, detail="Filename missing")

    # Save uploaded file to temporary file
    temp_dir = tempfile.mkdtemp()
    temp_path = os.path.join(temp_dir, file.filename)

    try:
        with open(temp_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)

        doc_service = get_document_service()
        result = doc_service.process_and_index_document(
            file_path=temp_path,
            filename=file.filename,
            source_type=source_type,
            user_id=user_id,
            category=category
        )

        return DocumentUploadResponse(
            document_id=result["document_id"],
            filename=result["filename"],
            source_type=result["source_type"],
            num_chunks=result["num_chunks"],
            message=f"Successfully processed and indexed {result['num_chunks']} chunks into personal memory database."
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to process document: {str(e)}")
    finally:
        shutil.rmtree(temp_dir, ignore_errors=True)


@router.post("/text", response_model=TextIngestResponse)
async def ingest_text(request: TextIngestRequest):
    """Ingest raw text content directly (e.g., from .txt or .md files read on-device)."""
    try:
        doc_service = get_document_service()
        result = doc_service.process_raw_text(
            text=request.text,
            source_name=request.source_name,
            source_type=request.source_type,
            user_id=request.user_id,
        )

        return TextIngestResponse(
            document_id=result["document_id"],
            source_name=result["source_name"],
            num_chunks=result["num_chunks"],
            message=f"Successfully ingested {result['num_chunks']} chunks from '{request.source_name}'."
        )
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to ingest text: {str(e)}")
