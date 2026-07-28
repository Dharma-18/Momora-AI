import os
from typing import List, Dict, Any
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_core.messages import SystemMessage, HumanMessage
from app.config import settings

MOMORA_SYSTEM_PROMPT = """You are Momora AI — a personal AI memory assistant ("Your Second Brain – Remember Everything, Miss Nothing").

Your core purpose is to help the user recall their own personal information, documents, commitments, chats, and notices.

Instructions:
1. Always base your answers primarily on the retrieved context provided in the user prompt.
2. Be concise, direct, helpful, and natural.
3. If multiple sources contradict each other (e.g. an email says Aug 10, a chat says Aug 12, a faculty notice says Aug 13), reconcile them clearly and highlight the most recent/authoritative source.
4. If the retrieved context does not contain enough information to answer the question, politely inform the user that it isn't recorded in their memory base yet.
5. Provide a friendly, intelligent summary.
"""

class LLMService:
    def __init__(self):
        api_key = settings.GEMINI_API_KEY or os.environ.get("GEMINI_API_KEY", "")
        if not api_key:
            print("WARNING: GEMINI_API_KEY is not set. LLM calls will fail until configured.")

        # Initialize Gemini via LangChain
        self.llm = ChatGoogleGenerativeAI(
            model="gemini-2.5-flash",
            google_api_key=api_key,
            temperature=0.3,
            max_output_tokens=1024,
        )

    def generate_rag_response(
        self,
        query: str,
        context_chunks: List[Dict[str, Any]]
    ) -> str:
        if not context_chunks:
            return (
                "I searched your personal memory base, but couldn't find any relevant "
                "notes, documents, or messages matching your query."
            )

        # Build context string with source metadata
        context_str = ""
        for i, chunk in enumerate(context_chunks, 1):
            meta = chunk.get("metadata", {})
            source = meta.get("source_type", "Document")
            filename = meta.get("filename", "Unknown")
            context_str += f"\n--- Source [{i}]: {source} ({filename}) ---\n{chunk.get('content', '')}\n"

        prompt = f"""User Question: {query}

Retrieved Personal Context:
{context_str}

Please answer the user's question using the context above. Cite sources clearly if applicable."""

        try:
            messages = [
                SystemMessage(content=MOMORA_SYSTEM_PROMPT),
                HumanMessage(content=prompt)
            ]
            response = self.llm.invoke(messages)
            return response.content
        except Exception as e:
            print(f"Error calling Gemini API: {e}")
            return f"I found relevant personal data, but encountered an error generating the response: {str(e)}"

_llm_service_instance = None

def get_llm_service() -> LLMService:
    global _llm_service_instance
    if _llm_service_instance is None:
        _llm_service_instance = LLMService()
    return _llm_service_instance
