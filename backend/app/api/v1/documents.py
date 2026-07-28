import os
import shutil
import tempfile
from fastapi import APIRouter, UploadFile, File, Form, HTTPException
from app.models.schemas import DocumentUploadResponse
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
