import os
import uuid
from typing import List, Dict, Any
from pypdf import PdfReader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from app.db.vector_store import get_vector_store

class DocumentService:
    def __init__(self):
        self.vector_store = get_vector_store()
        self.text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=500,
            chunk_overlap=50,
            separators=["\n\n", "\n", ". ", " ", ""]
        )

    def extract_text_from_pdf(self, file_path: str) -> str:
        reader = PdfReader(file_path)
        extracted_text = []
        for i, page in enumerate(reader.pages):
            text = page.extract_text()
            if text:
                extracted_text.append(text)
        return "\n".join(extracted_text)

    def process_and_index_document(
        self,
        file_path: str,
        filename: str,
        source_type: str,
        user_id: str = "default_user",
        category: str = "General"
    ) -> Dict[str, Any]:
        document_id = str(uuid.uuid4())

        # Extract text based on file extension
        ext = os.path.splitext(filename)[1].lower()
        if ext == ".pdf":
            raw_text = self.extract_text_from_pdf(file_path)
        else:
            with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                raw_text = f.read()

        if not raw_text.strip():
            raise ValueError("Document contains no readable text.")

        # Semantic chunking
        chunks = self.text_splitter.split_text(raw_text)

        metadatas = []
        ids = []
        for index, chunk in enumerate(chunks):
            chunk_id = f"{document_id}_{index}"
            ids.append(chunk_id)
            metadatas.append({
                "document_id": document_id,
                "filename": filename,
                "source_type": source_type,
                "user_id": user_id,
                "category": category,
                "chunk_index": index,
                "total_chunks": len(chunks)
            })

        # Index chunks in vector DB
        self.vector_store.add_documents(
            user_id=user_id,
            documents=chunks,
            metadatas=metadatas,
            ids=ids
        )

        return {
            "document_id": document_id,
            "filename": filename,
            "source_type": source_type,
            "num_chunks": len(chunks),
            "raw_text_length": len(raw_text)
        }

_document_service_instance = None

def get_document_service() -> DocumentService:
    global _document_service_instance
    if _document_service_instance is None:
        _document_service_instance = DocumentService()
    return _document_service_instance
