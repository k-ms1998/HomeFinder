import os

from dotenv import load_dotenv
from fastapi import FastAPI
from langchain.chains import RetrievalQAWithSourcesChain
from langchain.chat_models import ChatOpenAI
from langchain.embeddings.openai import OpenAIEmbeddings
from langchain.vectorstores import Chroma
from pydantic import BaseModel

load_dotenv("../.env")
os.environ['OPENAI_API_KEY']

########################################################################

app = FastAPI(
  title="LangChain Server",
  version="1.0",
  description="A simple api server using Langchain's Runnable interfaces",
)

class Item(BaseModel):
    question: str
    datas: dict = {}

########################################################################

chat_model = ChatOpenAI(
    model_name = 'gpt-3.5-turbo',
    temperature = 0.2
)

embeddings_model = OpenAIEmbeddings()

########################################################################

@app.post("/homefinder/apartment/news/v1")
async def create_item(item: Item):
    news = item.datas.values()
    question = item.question

    db = Chroma.from_texts(
        news, embeddings_model, metadatas=[{"source": f"{i}-pl"} for i in range(len(news))]
    )
    retriever = db.as_retriever()
    qa_chain = RetrievalQAWithSourcesChain.from_chain_type(
        chat_model,
        retriever=retriever
    )

    result = qa_chain({"question" : question})
    print(result)
    return result

########################################################################

if __name__ == "__main__":
    import uvicorn

    print("!! LangChain Backend Server started !!")
    uvicorn.run(app, host="localhost", port=8000)